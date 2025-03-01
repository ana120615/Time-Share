package br.ufrpe.timeshare.negocio;

import br.ufrpe.timeshare.dados.IRepositorioEstadia;
import br.ufrpe.timeshare.dados.IRepositorioReservas;
import br.ufrpe.timeshare.dados.RepositorioEstadia;
import br.ufrpe.timeshare.dados.RepositorioReservas;
import br.ufrpe.timeshare.excecoes.*;
import br.ufrpe.timeshare.negocio.beans.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

//FALTA ISSO AQUI:
//Fazer tela de ajuda
//Problema nos bens: aparece lista vazia quando tento calcular deslocamento
//Os bens estao com problema em exibir imagem
//fazer date picker verificar periodos futuros a depender das cotas também para reservar

public class ControladorReservas {
    private IRepositorioReservas repositorioReservas;
    private IRepositorioEstadia repositorioEstadia;


    public ControladorReservas() {
        this.repositorioReservas = RepositorioReservas.getInstancia();
        this.repositorioEstadia = RepositorioEstadia.getInstancia();
    }

    private static List<Map.Entry<LocalDate, Long>> calcularContagemPorData(List<Reserva> reservas, List<Estadia> estadias) {
        Map<LocalDate, Long> contagemDias = new HashMap<>();
        // Contabiliza reservas
        for (Reserva reserva : reservas) {
            LocalDate data = reserva.getDataInicio().toLocalDate();
            while (!data.isAfter(reserva.getDataFim().toLocalDate())) {
                contagemDias.compute(data, (k, v) -> (v == null) ? 1L : v + 1);
                data = data.plusDays(1);
            }
        }
        // Contabiliza estadias
        for (Estadia estadia : estadias) {
            LocalDate data = estadia.getDataInicio().toLocalDate();
            while (!data.isAfter(estadia.getDataFim().toLocalDate())) {
                contagemDias.compute(data, (k, v) -> (v == null) ? 1L : v + 1);
                data = data.plusDays(1);
            }
        }
        return new ArrayList<>(contagemDias.entrySet());
    }

    public String checkin(int idReserva) throws EstadiaJaInicializadaException, ReservaNaoExisteException, ReservaJaCanceladaException, ForaPeriodoException {
        LocalDateTime dataInicio = LocalDateTime.now();
        Reserva reservaRelacionada = repositorioReservas.buscar(idReserva);
        Estadia verificadorEstadia = repositorioEstadia.buscarEstadiaPorReserva(idReserva);
        if (verificadorEstadia != null) {
            throw new EstadiaJaInicializadaException("Check-in ja realizado nesta reserva.");
        }
        if (reservaRelacionada == null) {
            throw new ReservaNaoExisteException("Reserva inexistente.");
        }
        //criacao de id aleatorio
        int idAleatorio = 0;
        for (int i = 0; i < 1; ) {
            idAleatorio = ThreadLocalRandom.current().nextInt(1001, 99999 + 1);
            if (repositorioEstadia.buscar(idAleatorio) == null) {
                i++; // Sai do loop se o ID for unico
            }
        }
        Estadia estadia = new Estadia(idAleatorio, reservaRelacionada);
        if (dataInicio.isBefore(reservaRelacionada.getDataInicio()) || dataInicio.isAfter(reservaRelacionada.getDataFim())) {
            throw new ForaPeriodoException("Data de inicio fora do periodo da reserva.");
        } else {
            estadia.setDataInicio(dataInicio);
            estadia.setDataFim(null); //a data final da estadia so vai ser alterada no check-out
            repositorioEstadia.cadastrar(estadia);
        }
        return gerarComprovanteEstadia(estadia);
    }

    public String prolongarEstadia(int idEstadia, int quantidadeDias) throws ReservaNaoExisteException, ReservaJaCanceladaException, ForaPeriodoException, PeriodoJaReservadoException, PeriodoNaoDisponivelParaReservaException, ReservaNaoExisteException, CotaJaReservadaException, ReservaJaExisteException, DadosInsuficientesException, EstadiaNaoExisteException, UsuarioNaoPermitidoException, OperacaoNaoPermitidaException {
        Estadia estadia = repositorioEstadia.buscar(idEstadia);
        if (estadia == null) {
            throw new EstadiaNaoExisteException("Estadia inexistente.");
        }

        Reserva reserva = estadia.getReserva();
        LocalDateTime dataFimOriginal = reserva.getDataFim();
        LocalDateTime novaDataFim = dataFimOriginal.plusDays(quantidadeDias);

        // Garante que só cobra pelos dias adicionais
        long diasExtras = quantidadeDias;
        if (LocalDateTime.now().isBefore(dataFimOriginal)) {
            diasExtras = Math.max(java.time.Duration.between(dataFimOriginal, novaDataFim).toDays(), 0);
        }

        // Calcula a taxa APENAS para os dias adicionais
        double taxaExtra = calcularTaxaExtra(reserva, (int) diasExtras);

        // Atualiza a reserva
        alterarPeriodoReserva(reserva.getId(), reserva.getDataInicio(), novaDataFim, reserva.getUsuarioComum());

        return "Estadia prolongada. Dias adicionais: " + diasExtras + "\nTaxa extra: R$" + taxaExtra;
    }


    public String checkout(int idEstadia, Usuario usuario) throws ReservaNaoExisteException, ReservaJaCanceladaException, EstadiaNaoExisteException, UsuarioNaoPermitidoException {
        LocalDateTime agora = LocalDateTime.now();
        Estadia estadia = repositorioEstadia.buscar(idEstadia);
        if (estadia == null) {
            throw new EstadiaNaoExisteException("Estadia inexistente.");
        }
        Reserva reserva = repositorioReservas.buscarReserva(estadia.getReserva());
        if (reserva == null) {
            throw new ReservaNaoExisteException("Reserva inexistente.");
        }
        if (!usuario.equals(reserva.getUsuarioComum())) {
            throw new UsuarioNaoPermitidoException("Usuário não permitido.");
        }
        estadia.setDataFim(agora);
        repositorioReservas.remover(reserva); //remove reserva do repositorio
        return gerarComprovanteEstadia(estadia);
    }

    //metodo para criar reserva/ reservar
    public String criarReserva(LocalDateTime dataInicio, LocalDateTime dataFim, Usuario usuarioComum, Bem bem) throws DadosInsuficientesException, ReservaJaExisteException, ForaPeriodoException, PeriodoJaReservadoException, PeriodoNaoDisponivelParaReservaException, ReservaNaoExisteException, CotaJaReservadaException {
        if (bem == null || dataInicio == null || dataFim == null || usuarioComum == null) {
            throw new DadosInsuficientesException("Bem, data de inicio, data final ou usuario nao podem ser nulos.");
        }
        //verificar se periodo valido e se ja existe reserva ou uma cota comprada naquele periodo
        verificarPeriodo(dataInicio, dataFim, bem, usuarioComum);
        //criacao de id aleatorio
        int idNumeroReserva = 0;
        for (int i = 0; i < 1; ) {
            idNumeroReserva = ThreadLocalRandom.current().nextInt(1001, 99999 + 1);
            if (repositorioReservas.buscar(idNumeroReserva) == null) {
                i++; //sai do loop se o ID for unico
            }
        }
        Reserva reserva = new Reserva(idNumeroReserva, dataInicio, dataFim, usuarioComum, bem);
        repositorioReservas.cadastrar(reserva);
        return gerarComprovanteReserva(reserva);
    }

    //metodo para reservar o periodo completo de 1 cota
    public String reservaPeriodoCota(Cota cota, Usuario usuario) throws UsuarioNaoPermitidoException, DadosInsuficientesException, ProprietarioNaoIdentificadoException, DadosInsuficientesException, ReservaJaExisteException, ForaPeriodoException, PeriodoJaReservadoException, PeriodoNaoDisponivelParaReservaException, ReservaNaoExisteException, CotaJaReservadaException {
        String reservaFeita;
        if (cota == null || usuario == null) {
            throw new DadosInsuficientesException("Cota inexistente.");
        }
        if (cota.getProprietario() != null && !cota.getProprietario().equals(usuario)) {
            throw new UsuarioNaoPermitidoException("Cota pertence a outro usuario.");
        }
        reservaFeita = criarReserva(cota.getDataInicio(), cota.getDataFim(), cota.getProprietario(), cota.getBemAssociado());
        return reservaFeita;
    }

    private void verificarPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim, Bem bem, Usuario usuario) throws ReservaJaExisteException, PeriodoNaoDisponivelParaReservaException, ForaPeriodoException {
        //verifica se datas sao validas
        if (dataInicio.isAfter(dataFim)) {
            throw new ForaPeriodoException("A data inicial nao pode ser depois da data final.");
        }
        //verifica se reserva ja existe dentro daquele periodo
        if (verificarConflitoDeDatasReservaBem(bem.getId(), dataInicio, dataFim)) {
            throw new ReservaJaExisteException("Conflito de Periodo. Reserva não realizada.");
        }
        //verifica se reserva esta dentro de uma cota que ja foi comprada e nao pertence ao usuario
        if (verificarConflitoDeDatasCota(bem, dataInicio, dataFim, usuario)) {
            throw new PeriodoNaoDisponivelParaReservaException("O periodo esta dentro de uma cota comprada por outro usuario.");
        }
    }


    public String cancelarReserva(int idReserva, Usuario usuario) throws ReservaNaoExisteException, ReservaJaCanceladaException, CotaJaReservadaException, UsuarioNaoPermitidoException, ReservaNaoReembolsavelException, DadosInsuficientesException, NullPointerException, OperacaoNaoPermitidaException {
        Reserva reservaCancelada = repositorioReservas.buscar(idReserva);
        double reembolso;
        if (reservaCancelada == null) {
            throw new ReservaNaoExisteException("Reserva inexistente");
        }
        Estadia estadia = repositorioEstadia.buscarEstadiaPorReserva((int) reservaCancelada.getId());
        if (estadia != null && estadia.getDataInicio() != null && estadia.getDataFim() == null) {
            throw new ReservaJaCanceladaException("Estadia ja iniciada. Nao pode ser cancelada.");
        }
        if (!reservaCancelada.getBem().getCadastradoPor().equals(usuario) && !reservaCancelada.getUsuarioComum().equals(usuario)) {
            throw new UsuarioNaoPermitidoException("Reserva nao vinculada a este usuario.");
        }
        reembolso = reembolsar(reservaCancelada);
        String comprovanteCancelamento = "FLEX SHARE\n" + "____________________________\n" + "COMPROVANTE DE CANCELAMENTO DE RESERVA: \n" + "__________________________________________________\n" + "RESERVA: \n" + reservaCancelada.getId() + "\n Periodo: " + reservaCancelada.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "-" + reservaCancelada.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n REEMBOLSO: R$" + reembolso;
        repositorioReservas.remover(reservaCancelada);
        return comprovanteCancelamento;
    }

    public void cancelarListaReservas(List<Reserva> aCancelar) {
        if (aCancelar != null) {
            for (Reserva reserva : aCancelar) {
                repositorioReservas.remover(reserva);
            }
        }
    }

    //metodo para reembolso apos cancelamento
    public double reembolsar(Reserva reserva) throws ReservaNaoReembolsavelException, NullPointerException, ReservaNaoExisteException, CotaJaReservadaException, DadosInsuficientesException, OperacaoNaoPermitidaException {
        double reembolso = 0.00;
        int dias = reserva.calcularDuracaoReserva();
        if (reserva.getDataFim().isBefore(LocalDateTime.now())) {
            throw new ReservaNaoReembolsavelException("Periodo da reserva expirado. Nao reembolsavel");
        }
        if (calcularTaxaExtra(reserva, dias) != 0) {
            reembolso += (calcularTaxaExtra(reserva, dias)) * 0.30;
        }
        return reembolso;
    }

    public String alterarPeriodoReserva(long idReserva, LocalDateTime novaDataInicio, LocalDateTime novaDataFim, Usuario usuario) throws ReservaJaCanceladaException, ForaPeriodoException, PeriodoJaReservadoException, PeriodoNaoDisponivelParaReservaException, ReservaNaoExisteException, CotaJaReservadaException, ReservaJaExisteException, DadosInsuficientesException, UsuarioNaoPermitidoException {
        //busca reserva pelo id
        Reserva reserva = repositorioReservas.buscar(idReserva);
        Bem bem = reserva.getBem();
        if (reserva == null) {
            throw new ReservaNaoExisteException("Reserva inexistente.");
        }
        if (!reserva.getUsuarioComum().equals(usuario)) {
            throw new UsuarioNaoPermitidoException("Reserva esta em nome de outro usuario");
        }
        //verifica se datas sao validas
        if (novaDataInicio.isAfter(novaDataFim)) {
            throw new ForaPeriodoException("A data inicial nao pode ser depois da data final.");
        }
        //verifica se a nova data ficara dentro do periodo de uma reserva outra
        //verificar a parte inicial nova
        if (novaDataInicio.isBefore(reserva.getDataInicio())) {
            if (verificarConflitoDeDatasReservaBem(bem.getId(), novaDataInicio, reserva.getDataInicio())) {
                throw new ReservaJaExisteException("Conflito de Periodo.");
            }
        }
        //verificar a parte final nova
        if (novaDataFim.isAfter(reserva.getDataFim())) {
            if (verificarConflitoDeDatasReservaBem(bem.getId(), reserva.getDataFim(), novaDataFim)) {
                throw new ReservaJaExisteException("Conflito de Periodo.");
            }
        }
        //verifica se a nova data ficara dentro do periodo de uma cota
        if (verificarConflitoDeDatasCota(reserva.getBem(), novaDataInicio, novaDataFim, reserva.getUsuarioComum())) {
            throw new PeriodoNaoDisponivelParaReservaException("O periodo esta dentro de uma cota comprada por outro usuario.");
        }
        reserva.setDataInicio(novaDataInicio); //atualiza dados da reserva
        reserva.setDataFim(novaDataFim);
        return gerarComprovanteReserva(reserva);
    }

    // Consulta de periodos disponiveis para reserva
    // Considerando o usuario que deseja reservar
    public List<String> consultarDisponibilidadeParaReserva(Bem bem, LocalDateTime inicioPeriodo, LocalDateTime dataFim, Usuario usuario) throws BemNaoExisteException {
        List<String> periodosDisponiveis = new ArrayList<>();
        LocalDateTime inicioAtual = inicioPeriodo;
        boolean existeReservaAtiva;
        boolean existeCotaOcupada;
        // Verifica se datas sao validas
        if (dataFim.isBefore(inicioPeriodo)) {
            throw new IllegalArgumentException("A data de inicio deve ser antes da data final.");
        }
        while (!inicioAtual.isAfter(dataFim)) {
            // Buscar todas as reservas para o bem
            List<Reserva> reservas = repositorioReservas.buscarReservasPorBem(bem.getId());
            // Buscar as cotas do bem
            ArrayList<Cota> cotasBemAssociado = bem.getCotas();
            existeCotaOcupada = false;
            existeReservaAtiva = false;
            periodosDisponiveis.add(inicioAtual.toString());
            //Verifica periodos em que ha cotas que ja foram vendidas e nao podem ser usadas para reservas de todos
            for (int i = 0; i < cotasBemAssociado.size() && !existeCotaOcupada; i++) {
                if ((cotasBemAssociado.get(i).getProprietario() != null && !cotasBemAssociado.get(i).getProprietario().equals(usuario) &&
                        !cotasBemAssociado.get(i).getStatusDeDisponibilidadeParaCompra())) {

                    existeCotaOcupada = verificarConflitoDeDatasCota(cotasBemAssociado.get(i), inicioAtual, inicioAtual);
                }
            }

            for (int i = 0; i < reservas.size() && !existeReservaAtiva; i++) {
                existeReservaAtiva = verificarConflitoDeDatasReserva(reservas.get(i), inicioAtual, inicioAtual);
            }
            if (existeCotaOcupada || existeReservaAtiva) {
                periodosDisponiveis.remove(inicioAtual.toString());
            }
            inicioAtual = inicioAtual.plusDays(1);
        }
        return periodosDisponiveis;
    }

    private boolean verificarConflitoDeDatasCota(Cota cotaAtual, LocalDateTime dataInicio, LocalDateTime dataFim) {
        boolean conflito = false;

        LocalDate dataInicioAtual = cotaAtual.getDataInicio().toLocalDate();
        LocalDate dataFimAtual = cotaAtual.getDataFim().toLocalDate();

        LocalDate dataInicioComparacao = dataInicio.toLocalDate();
        LocalDate dataFimComparacao = dataFim.toLocalDate();

        if (dataInicioAtual.isBefore(dataInicioComparacao) && dataFimAtual.isAfter(dataInicioComparacao)) {
            conflito = true;
        } else if (dataInicioAtual.isBefore(dataFimComparacao) && dataFimAtual.isAfter(dataFimComparacao)) {
            conflito = true;
        } else if (dataInicioAtual.isAfter(dataInicioComparacao) && dataFimAtual.isBefore(dataFimComparacao)) {
            conflito = true;
        } else if (dataInicioAtual.isEqual(dataInicioComparacao) || dataFimAtual.isEqual(dataFimComparacao)) {
            conflito = true;
        }

        return conflito;
    }

    private boolean verificarConflitoDeDatasCota(Bem bem, LocalDateTime dataInicio, LocalDateTime dataFim, Usuario usuario) {
        boolean conflito = false;
        LocalDate dataInicial = dataInicio.toLocalDate();
        LocalDate dataFinal = dataFim.toLocalDate();
        List<Cota> cotasBemAssociado = bem.getCotas();
        for (Cota cotaAtual : cotasBemAssociado) {
            LocalDate cotaAtualDataInicio = cotaAtual.getDataInicio().toLocalDate();
            LocalDate cotaAtualDataFim = cotaAtual.getDataFim().toLocalDate();
            if ((cotaAtual.getProprietario() != null && !cotaAtual.getProprietario().equals(usuario)) &&
                    !cotaAtual.getStatusDeDisponibilidadeParaCompra()) {
                if (cotaAtualDataInicio.isBefore(dataInicial) && cotaAtualDataFim.isAfter(dataInicial)) {
                    conflito = true;
                } else if (cotaAtualDataInicio.isBefore(dataFinal) && cotaAtualDataFim.isAfter(dataFinal)) {
                    conflito = true;
                } else if (cotaAtualDataInicio.isAfter(dataInicial) && cotaAtualDataFim.isBefore(dataFinal)) {
                    conflito = true;
                } else if (cotaAtualDataInicio.isEqual(dataInicial) || cotaAtualDataFim.isEqual(dataFinal)) {
                    conflito = true;
                }
            }
        }
        return conflito;
    }

    public List<Reserva> getReservas() {
        List<Reserva> resultado = this.repositorioReservas.listar();
        Collections.sort(resultado);
        return resultado;
    }

    public List<Estadia> getEstadias() {
        List<Estadia> resultado = this.repositorioEstadia.listar();
        Collections.sort(resultado);
        return resultado;
    }

    //Verifica se o periodo esta dentro da cota atual
    private boolean verificarConflitoDeDatasReserva(Reserva reservaAtual, LocalDateTime dataInicio, LocalDateTime dataFim) {
        LocalDate reservaAtualDataInicio = reservaAtual.getDataInicio().toLocalDate();
        LocalDate reservaAtualDataFim = reservaAtual.getDataFim().toLocalDate();

        LocalDate dataInicial = dataInicio.toLocalDate();
        LocalDate dataFinal = dataFim.toLocalDate();
        if (reservaAtualDataInicio.isBefore(dataInicial) && reservaAtualDataFim.isAfter(dataInicial)) {
            return true;
        } else if (reservaAtualDataInicio.isBefore(dataFinal) && reservaAtualDataFim.isAfter(dataFinal)) {
            return true;
        } else if (reservaAtualDataInicio.isAfter(dataInicial) && reservaAtualDataFim.isBefore(dataFinal)) {
            return true;
        } else if (reservaAtualDataInicio.isEqual(dataInicial) || reservaAtualDataFim.isEqual(dataFinal)) {
            return true;
        }
        return false;
    }

    public boolean verificarConflitoDeDatasReservaBem(long idBem, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return this.repositorioReservas.verificarConflitoNaReserva(idBem, dataInicio, dataFim);
    }

    //metodo para calcular taxa extra
    //tambem e usado quando prolongo uma estadia
    //verifica se cota pertence ao usuario e corresponde ao periodo que deseja reservar
    public double calcularTaxaExtra(Reserva reserva, int quantidadeDias) throws ReservaNaoExisteException, CotaJaReservadaException, DadosInsuficientesException {
        double taxa = 0.00;
        boolean reservaTaxada = true;
        Promocao promocao = new Promocao();

        if (reserva == null) {
            throw new ReservaNaoExisteException("Reserva nao pode ser nula.");
        }

        if (quantidadeDias < 1) {
            throw new DadosInsuficientesException("Quantidade de dias invalida.");
        }

        List<Cota> cotas = reserva.getBem().getCotas();

        for (Cota cota : cotas) {
            if (cota.getProprietario() != null && cota.getProprietario().equals(reserva.getUsuarioComum())) {
                // Verifica se a cota cobre exatamente o período da reserva
                if (cota.getDataInicio().isEqual(reserva.getDataInicio()) && cota.getDataFim().isEqual(reserva.getDataFim())) {
                    reservaTaxada = false;
                    break;
                }

                // Verifica se a cota cobre parte do período da reserva
                if (cota.getDataInicio().isBefore(reserva.getDataInicio()) && cota.getDataFim().isAfter(reserva.getDataInicio()) ||
                        cota.getDataInicio().isBefore(reserva.getDataFim()) && cota.getDataFim().isAfter(reserva.getDataFim()) ||
                        cota.getDataInicio().isAfter(reserva.getDataInicio()) && cota.getDataFim().isBefore(reserva.getDataFim())) {
                    reservaTaxada = false;
                    break;
                }
            }
        }

        if (reservaTaxada) {
            if (!cotas.isEmpty()) {
                taxa = cotas.getFirst().getPreco() * 0.05 * quantidadeDias;
            }
            //Aplica promocao na taxa extra
            double taxaPromocional = promocao.calcularTaxaPromocao(reserva.getDataInicio().withHour(0).withMinute(0), reserva.getUsuarioComum());
            taxa -= taxa * taxaPromocional;
        }
        return Math.max(taxa, 0.00);
    }


    public String gerarComprovanteReserva(Reserva reserva) throws CotaJaReservadaException, DadosInsuficientesException, ReservaNaoExisteException {
        int dias = reserva.calcularDuracaoReserva();
        double taxa = 0.00;
        try {
            taxa = calcularTaxaExtra(reserva, dias);
        } catch (ReservaNaoExisteException | DadosInsuficientesException e) {
            throw e;
        }
        return reserva + "\nTaxa extra: R$" + String.format("%.2f", taxa);
    }

    public String gerarComprovanteEstadia(Estadia estadia) {
        int duracao = estadia.calcularDuracao();
        return estadia + "Duracao da estadia: " + String.format("%d", duracao) + " dias";
    }

    public List<Reserva> listarReservasUsuario(Usuario usuario) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva r : repositorioReservas.listar()) {
            if (r.getUsuarioComum().equals(usuario)) {
                resultado.add(r);
            }
        }
        Collections.sort(resultado);
        return resultado;
    }

    public List<Estadia> listarEstadiasUsuario(Usuario usuario) {
        List<Estadia> resultado = new ArrayList<>();
        for (Estadia e : repositorioEstadia.listar()) {
            if (e.getReserva().getUsuarioComum().equals(usuario)) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    //Verifica se usuario possui alguma estadia ativa
    //Se fez check in em alguma reserva
    public Estadia getEstadiaAtiva(Usuario usuario) throws OperacaoNaoPermitidaException, DadosInsuficientesException {
        List<Estadia> estadias = null;
        Estadia estadiaAtiva = null;
        if (usuario != null) {
            estadias = listarEstadiasUsuario(usuario);
            if (estadias != null) {
                for (Estadia estadia : estadias) {
                    if (estadia.getReserva() != null && estadia.getDataFim() == null) {
                        estadiaAtiva = estadia;
                        break;
                    }
                }
            } else {
                throw new OperacaoNaoPermitidaException("O usuario nao possui estadias");
            }
        } else {
            throw new DadosInsuficientesException("O usuario nao pode ser nulo");
        }
        return estadiaAtiva;
    }

    public List<Estadia> getEstadiasDosBensDoUsuarioAdm(Usuario usuario) throws OperacaoNaoPermitidaException, DadosInsuficientesException {
        List<Estadia> resultado = new ArrayList<>();
        if (usuario != null) {
            List<Estadia> estadias = repositorioEstadia.listar();
            if (estadias != null) {
                for (Estadia estadia : estadias) {
                    if (estadia.getReserva() != null && estadia.getReserva().getBem().getCadastradoPor().equals(usuario)) {
                        resultado.add(estadia);
                    }
                }
            }
        } else {
            throw new DadosInsuficientesException("O usuario nao pode ser nulo");
        }
        return resultado;
    }


    public List<Reserva> buscarReservaPorBem(long idBem) throws OperacaoNaoPermitidaException {
        if (idBem == 0) {
            throw new OperacaoNaoPermitidaException("ID do bem invalido");
        }
        return this.repositorioReservas.buscarReservasPorBem(idBem);
    }

    public List<Estadia> buscarEstadiasAtivasNumBem(long idBem) {
        return repositorioEstadia.buscarEstadiasAtivasNumBem(idBem);
    }

    public List<Reserva> buscarReservaPorNomeBem(Usuario usuario, String nomeBem)
            throws DadosInsuficientesException, BuscaNaoPermitidaException, OperacaoNaoPermitidaException {

        List<Reserva> resultado = new ArrayList<>();
        List<Reserva> reservasUsuario = listarReservasUsuario(usuario);
        int criteriosPreenchidos = 0;
        if (usuario != null) {
            if (reservasUsuario != null) {

                if (nomeBem != null && !nomeBem.trim().isEmpty()) {
                    criteriosPreenchidos++;
                }

                if (criteriosPreenchidos == 0) {
                    throw new BuscaNaoPermitidaException("É necessário inserir o nome do bem para buscar.");
                }

                for (Reserva reserva : reservasUsuario) {
                    boolean atendeNomeBem = false;


                    // Verifica se o nome do bem está presente
                    if (nomeBem != null && !nomeBem.trim().isEmpty()) {
                        if (reserva.getBem().getNome().toLowerCase().contains(nomeBem.toLowerCase())) {
                            atendeNomeBem = true;
                        }
                    }


                    // Adiciona a reserva se atender pelo menos um dos critérios
                    if (nomeBem != null && atendeNomeBem) {
                        resultado.add(reserva);
                    }
                }
            } else {
                throw new OperacaoNaoPermitidaException("Usuário não possui reservas");
            }
        } else {
            throw new DadosInsuficientesException("Usuário não pode ser nulo.");
        }
        return resultado;
    }


//RELATORIOS

    public List<Reserva> buscarReservasPorMultiplosPeriodos(Bem bem, Usuario usuario, LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Reserva> reservasDentroDosPeriodos = new ArrayList<>();
        LocalDate dataInicial = dataInicio.toLocalDate();
        LocalDate dataFinal = dataFim.toLocalDate();

        for (Reserva reserva : repositorioReservas.listar()) {
            if (!reserva.getUsuarioComum().equals(usuario) && reserva.getBem().equals(bem)) {
                LocalDate reservaInicio = reserva.getDataInicio().toLocalDate();
                LocalDate reservaFim = reserva.getDataFim().toLocalDate();

                // Condição correta para verificar se há sobreposição de períodos
                boolean conflito = !(reservaFim.isBefore(dataInicial) || reservaInicio.isAfter(dataFinal));

                if (conflito) {
                    Estadia estadia = repositorioEstadia.buscarEstadiaPorReserva((int) reserva.getId());
                    if (estadia == null) {
                        reservasDentroDosPeriodos.add(reserva);
                    }
                }
            }
        }
        return reservasDentroDosPeriodos;
    }


    //HISTORICO DE USO DE UM BEM
    public List<Estadia> historicoDeUsoBem(int idBem) {
        List<Estadia> historico = new ArrayList<>();
        for (Estadia estadia : repositorioEstadia.buscarEstadiasPorBem(idBem)) {
            historico.add(estadia);
        }
        Collections.sort(historico);
        return historico;
    }

    public List<Map.Entry<LocalDate, Long>> periodosMaisReservados(int idBem) {
        List<Reserva> reservas = repositorioReservas.buscarReservasPorBem(idBem);
        List<Estadia> estadias = repositorioEstadia.buscarEstadiasPorBem(idBem);
        List<Map.Entry<LocalDate, Long>> resultado = calcularContagemPorData(reservas, estadias);
        // Ordenar em ordem decrescente pelo número de reservas
        resultado.sort(Comparator.comparing(Map.Entry<LocalDate, Long>::getValue).reversed());
        return resultado;
    }

    public Map<YearMonth, Long> reservasPorMes(int idBem) {
        List<Reserva> reservas = repositorioReservas.buscarReservasPorBem(idBem);
        List<Estadia> estadias = repositorioEstadia.buscarEstadiasPorBem(idBem);

        List<Map.Entry<LocalDate, Long>> contagemPorData = calcularContagemPorData(reservas, estadias);

        // Mapa para armazenar a soma das reservas agrupadas por mês
        Map<YearMonth, Long> reservasPorMes = new HashMap<>();

        for (Map.Entry<LocalDate, Long> entry : contagemPorData) {
            YearMonth mes = YearMonth.from(entry.getKey());
            reservasPorMes.merge(mes, entry.getValue(), Long::sum);
        }

        return reservasPorMes;
    }

    public Map<YearMonth, Long> reservasPorMesTodosOsBens() {
        List<Reserva> todasReservas = repositorioReservas.listar();
        List<Estadia> todasEstadias = repositorioEstadia.listar();

        List<Map.Entry<LocalDate, Long>> contagemPorData = calcularContagemPorData(todasReservas, todasEstadias);

        // Mapa para armazenar a soma das reservas agrupadas por mês
        Map<YearMonth, Long> reservasPorMes = new HashMap<>();

        for (Map.Entry<LocalDate, Long> entry : contagemPorData) {
            YearMonth mes = YearMonth.from(entry.getKey());
            reservasPorMes.merge(mes, entry.getValue(), Long::sum);
        }

        return reservasPorMes;
    }


}