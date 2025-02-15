package br.ufrpe.timeshare.negocio;

import br.ufrpe.timeshare.dados.IRepositorioEstadia;
import br.ufrpe.timeshare.dados.IRepositorioReservas;
import br.ufrpe.timeshare.dados.RepositorioEstadia;
import br.ufrpe.timeshare.dados.RepositorioReservas;
import br.ufrpe.timeshare.excecoes.*;
import br.ufrpe.timeshare.negocio.beans.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ControladorReservas {
    private IRepositorioReservas repositorioReservas;
    private IRepositorioEstadia repositorioEstadia;

    public ControladorReservas() {
        this.repositorioReservas = RepositorioReservas.getInstancia();
        this.repositorioEstadia = RepositorioEstadia.getInstancia();
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


    public String prolongarEstadia(int idEstadia, int quantidaDias) throws ReservaNaoExisteException, ReservaJaCanceladaException, ForaPeriodoException, PeriodoJaReservadoException, PeriodoNaoDisponivelParaReservaException, ReservaNaoExisteException, CotaJaReservadaException, ReservaJaExisteException, DadosInsuficientesException, EstadiaNaoExisteException, UsuarioNaoPermitidoException, OperacaoNaoPermitidaException {
        Estadia estadia = repositorioEstadia.buscar(idEstadia);
        if (estadia == null) {
            throw new EstadiaNaoExisteException("Estadia inexistente.");
        }
        Reserva reserva = estadia.getReserva();
        LocalDateTime novaDataFim = reserva.getDataFim().plusDays(quantidaDias);
        if (LocalDateTime.now().isBefore(reserva.getDataFim())) {
            alterarPeriodoReserva(reserva.getId(), reserva.getDataInicio(), novaDataFim, reserva.getUsuarioComum());
        }
        return gerarComprovanteReserva(reserva);
    }


    public String checkout(int idEstadia) throws ReservaNaoExisteException, ReservaJaCanceladaException, EstadiaNaoExisteException {
        LocalDateTime agora = LocalDateTime.now();
        Estadia estadia = repositorioEstadia.buscar(idEstadia);
        if (estadia == null) {
            throw new EstadiaNaoExisteException("Estadia inexistente.");
        }
        Reserva reserva = repositorioReservas.buscarReserva(estadia.getReserva());
        if (reserva == null) {
            throw new ReservaNaoExisteException("Reserva inexistente.");
        } else {
            estadia.setDataFim(agora);
            repositorioReservas.remover(reserva); //remove reserva do repositorio
        }
        return gerarComprovanteEstadia(estadia);
    }


    //metodo para criar reserva/ reservar
    public String criarReserva(LocalDateTime dataInicio, LocalDateTime dataFim, Usuario usuarioComum, Bem bem)
            throws DadosInsuficientesException, ReservaJaExisteException, ForaPeriodoException, PeriodoJaReservadoException, PeriodoNaoDisponivelParaReservaException, ReservaNaoExisteException, CotaJaReservadaException {
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
        if (repositorioReservas.verificarConflitoNaReserva(bem.getId(), dataInicio, dataFim)) {
            throw new ReservaJaExisteException("Conflito de Periodo. Reserva não realizada.");
        }
        //verifica se reserva esta dentro de uma cota que ja foi comprada e nao pertence ao usuario
        if (verificarConflitoDeDatasCota(bem, dataInicio, dataFim, usuario)) {
            throw new PeriodoNaoDisponivelParaReservaException("O periodo esta dentro de uma cota comprada por outro usuario.");
        }
    }


    public void liberarPeriodoCota(Cota cota, int idReserva, Usuario usuario) throws DadosInsuficientesException, ReservaNaoExisteException, OperacaoNaoPermitidaException, UsuarioNaoPermitidoException {
        if (cota == null || idReserva == 0) {
            throw new DadosInsuficientesException("Dados insuficientes.");
        }
        Reserva reservaCancelada = repositorioReservas.buscar(idReserva);

        if (reservaCancelada == null) {
            throw new ReservaNaoExisteException("Reserva com este ID nao existe.");
        }
        if (!reservaCancelada.getUsuarioComum().equals(usuario)) {
            throw new UsuarioNaoPermitidoException("Reserva nao vinculada a este usuario.");
        }
        if (!reservaCancelada.getDataInicio().equals(cota.getDataInicio()) || !reservaCancelada.getDataFim().equals(cota.getDataFim())) {
            throw new OperacaoNaoPermitidaException("A reserva nao foi realizada dentro da cota, verifique o periodo completo da reserva para cancela-la.");
        }
        cota.setStatusDeDisponibilidadeParaReserva(true);
        repositorioReservas.remover(reservaCancelada);
    }


    public String cancelarReserva(int idReserva, Usuario usuario) throws ReservaNaoExisteException, ReservaJaCanceladaException, CotaJaReservadaException, UsuarioNaoPermitidoException, ReservaNaoReembolsavelException, DadosInsuficientesException {
        Reserva reservaCancelada = repositorioReservas.buscar(idReserva);
        double reembolso;
        if (reservaCancelada == null) {
            throw new ReservaNaoExisteException("Reserva inexistente");
        }
        Estadia estadia = repositorioEstadia.buscarEstadiaPorReserva((int) reservaCancelada.getId());
        if (estadia != null && estadia.getDataInicio() != null && estadia.getDataFim() == null) {
            throw new ReservaJaCanceladaException("Estadia ja iniciada. Nao pode ser cancelada.");
        }
        ArrayList<Cota> cotasBemAssociadoReserva = reservaCancelada.getBem().getCotas();
        if (!reservaCancelada.getBem().getCadastradoPor().equals(usuario) || !reservaCancelada.getUsuarioComum().equals(usuario)) {
            throw new UsuarioNaoPermitidoException("Reserva nao vinculada a este usuario.");
        }
        reembolso = reembolsar(reservaCancelada);
        String comprovanteCancelamento = "FLEX SHARE\n" + "____________________________\n" + "COMPROVANTE DE CANCELAMENTO DE RESERVA: \n" + "__________________________________________________\n" + "RESERVA: \n" + reservaCancelada.getId() + "\n Periodo: " + reservaCancelada.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "-" + reservaCancelada.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n REEMBOLSO: R$" + reembolso;
        repositorioReservas.remover(reservaCancelada);
        return comprovanteCancelamento;
    }

    public void cancelarListaReservas (List<Reserva> aCancelar) {
        if(aCancelar == null) {
            for(Reserva reserva : aCancelar) {
                repositorioReservas.remover(reserva);
            }
        }
    }

    //metodo para reembolso apos cancelamento
    public double reembolsar(Reserva reserva) throws ReservaNaoReembolsavelException, NullPointerException, ReservaNaoExisteException, CotaJaReservadaException, DadosInsuficientesException {
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


    public String alterarPeriodoReserva(long idReserva, LocalDateTime novaDataInicio, LocalDateTime novaDataFim, Usuario usuario)
            throws ReservaNaoExisteException, ReservaJaCanceladaException, ForaPeriodoException, PeriodoJaReservadoException, PeriodoNaoDisponivelParaReservaException, ReservaNaoExisteException, CotaJaReservadaException, ReservaJaExisteException, DadosInsuficientesException, UsuarioNaoPermitidoException {
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
            if (repositorioReservas.verificarConflitoNaReserva(bem.getId(), novaDataInicio, reserva.getDataInicio())) {
                throw new ReservaJaExisteException("Conflito de Periodo.");
            }
        }
        //verificar a parte final nova
        if (novaDataFim.isAfter(reserva.getDataFim())) {
            if (repositorioReservas.verificarConflitoNaReserva(bem.getId(), reserva.getDataFim(), novaDataFim)) {
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
                        !cotasBemAssociado.get(i).getStatusDeDisponibilidadeParaCompra()) || cotasBemAssociado.get(i).getProprietario() != null &&
                        (cotasBemAssociado.get(i).getProprietario().equals(usuario) && !cotasBemAssociado.get(i).getStatusDeDisponibilidadeParaReserva())) {

                    existeCotaOcupada = verificarConflitoDeDatasCota(cotasBemAssociado.get(i), inicioAtual, dataFim);
                }
            }
            // Verifica periodos em que ha reservas ativas
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
        if (cotaAtual.getDataInicio().isBefore(dataInicio) && cotaAtual.getDataFim().isAfter(dataInicio)) {
            conflito = true;
        } else if (cotaAtual.getDataInicio().isBefore(dataFim) && cotaAtual.getDataFim().isAfter(dataFim)) {
            conflito = true;
        } else if (cotaAtual.getDataInicio().isAfter(dataInicio) && cotaAtual.getDataFim().isBefore(dataFim)) {
            conflito = true;
        } else if (cotaAtual.getDataInicio().isEqual(dataInicio) || cotaAtual.getDataFim().isEqual(dataFim)) {
            conflito = true;
        }
        return conflito;
    }

    private boolean verificarConflitoDeDatasCota(Bem bem, LocalDateTime dataInicio, LocalDateTime dataFim, Usuario usuario) {
        boolean conflito = false;
        List<Cota> cotasBemAssociado = bem.getCotas();
        for (Cota cotaAtual : cotasBemAssociado) {
            if ((cotaAtual.getProprietario() != null && !cotaAtual.getProprietario().equals(usuario)) &&
                    !cotaAtual.getStatusDeDisponibilidadeParaCompra()) {
                if (cotaAtual.getDataInicio().isBefore(dataInicio) && cotaAtual.getDataFim().isAfter(dataInicio)) {
                    conflito = true;
                } else if (cotaAtual.getDataInicio().isBefore(dataFim) && cotaAtual.getDataFim().isAfter(dataFim)) {
                    conflito = true;
                } else if (cotaAtual.getDataInicio().isAfter(dataInicio) && cotaAtual.getDataFim().isBefore(dataFim)) {
                    conflito = true;
                } else if (cotaAtual.getDataInicio().isEqual(dataInicio) || cotaAtual.getDataFim().isEqual(dataFim)) {
                    conflito = true;
                }
            }
        }
        return conflito;
    }

    public List<Reserva> getReservas() {
        return this.repositorioReservas.listar();
    }

    public List<Estadia> getEstadias() {
        return this.repositorioEstadia.listar();
    }

    //Verifica se o periodo esta dentro da cota atual
    private boolean verificarConflitoDeDatasReserva(Reserva reservaAtual, LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (reservaAtual.getDataInicio().isBefore(dataInicio) && reservaAtual.getDataFim().isAfter(dataInicio)) {
            return true;
        } else if (reservaAtual.getDataInicio().isBefore(dataFim) && reservaAtual.getDataFim().isAfter(dataFim)) {
            return true;
        } else if (reservaAtual.getDataInicio().isAfter(dataInicio) && reservaAtual.getDataFim().isBefore(dataFim)) {
            return true;
        } else if (reservaAtual.getDataInicio().isEqual(dataInicio) || reservaAtual.getDataFim().isEqual(dataFim)) {
            return true;
        }
        return false;
    }


    //metodo para calcular taxa extra
    //tambem e usado quando prolongo uma estadia
    //verifica se cota pertence ao usuario e corresponde ao periodo que deseja reservar
    public double calcularTaxaExtra(Reserva reserva, int quantidadeDias) throws ReservaNaoExisteException, CotaJaReservadaException, DadosInsuficientesException {
        //taxa e uma porcentagem do preco de uma cota do bem
        double taxa = 0.00;
        boolean reservaTaxada = true;
        Promocao promocao = new Promocao();

        if (reserva == null) {
            throw new ReservaNaoExisteException("Reserva nao pode ser nula.");
        }

        if (quantidadeDias < 1) {
            throw new DadosInsuficientesException("Quantidade de dias invalida.");
        }

        //deve verificar se ha alguma cota no periodo
        //se nao houver, a taxa sera automaticamente aplicada
        List<Cota> cotas = reserva.getBem().getCotas();

        for (Cota cota : cotas) {
            if (cota.getProprietario() != null && cota.getProprietario().equals(reserva.getUsuarioComum())) {
                boolean datasIguais = cota.getDataInicio().isEqual(reserva.getDataInicio()) &&
                        cota.getDataFim().isEqual(reserva.getDataFim());

                boolean cotaCobreReserva = (!cota.getDataInicio().isAfter(reserva.getDataInicio()) && !cota.getDataFim().isBefore(reserva.getDataFim())) ||
                        (!cota.getDataInicio().isBefore(reserva.getDataInicio()) && cota.getDataFim().isAfter(reserva.getDataFim())) ||
                        (cota.getDataInicio().isBefore(reserva.getDataInicio()) && !cota.getDataFim().isAfter(reserva.getDataFim())) ||
                        (cota.getDataInicio().isEqual(reserva.getDataInicio()) && !cota.getDataFim().isAfter(reserva.getDataFim())) ||
                        (!cota.getDataInicio().isBefore(reserva.getDataInicio()) && cota.getDataFim().isEqual(reserva.getDataFim())) ||
                        (cota.getDataInicio().isEqual(reserva.getDataInicio()) && cota.getDataFim().isEqual(reserva.getDataFim()));
                if (datasIguais || cotaCobreReserva) {
                    reservaTaxada = false;
                    break;
                }
            }
        }


        //calcula a taxa baseada no preco de 1 cota e na quantidade de dias da reserva
        //5% ao dia
        if (reservaTaxada) {
            if (!cotas.isEmpty()) {
                taxa = cotas.getFirst().getPreco() * 0.05 * quantidadeDias;
            }
            double taxaPromocional = promocao.calcularTaxaPromocao(reserva.getDataInicio().withHour(0).withMinute(0), reserva.getUsuarioComum());
            double desconto = taxa * taxaPromocional;
            taxa -= desconto;
        }
        return Math.max(taxa, 0.00);
    }


    public String gerarComprovanteReserva(Reserva reserva) throws CotaJaReservadaException, DadosInsuficientesException, ReservaNaoExisteException {
        int dias = reserva.calcularDuracaoReserva();
        double taxa = calcularTaxaExtra(reserva, dias);
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

    public List<Reserva> buscarReservasPorMultiplosPeriodos(Bem bem, LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Reserva> reservasDentroDosPeriodos = new ArrayList<>();

        for (Reserva reserva : repositorioReservas.listar()) {
            if (reserva.getBem().equals(bem)) {
                boolean conflito = false;
                LocalDateTime reservaInicio = reserva.getDataInicio();
                LocalDateTime reservaFim = reserva.getDataFim();

                if (reserva.getDataInicio().isBefore(dataInicio) && reserva.getDataFim().isAfter(dataInicio)) {
                    conflito = true;
                } else if (reserva.getDataInicio().isBefore(dataFim) && reserva.getDataFim().isAfter(dataFim)) {
                    conflito = true;
                } else if (reserva.getDataInicio().isAfter(dataInicio) && reserva.getDataFim().isBefore(dataFim)) {
                    conflito = true;
                } else if (reserva.getDataInicio().isEqual(dataInicio) || reserva.getDataFim().isEqual(dataFim)) {
                    conflito = true;
                }
                // Verifica se há sobreposição da reserva com qualquer período e se ha estadia
                if (conflito) {
                    Estadia estadia = repositorioEstadia.buscarEstadiaPorReserva((int) reserva.getId());
                    if(estadia != null) {
                        reservasDentroDosPeriodos.add(reserva);
                    }
                }
            }
        }
        return reservasDentroDosPeriodos;
    }


//RELATORIOS

    //HISTORICO DE USO DE UM BEM
    public List<Estadia> historicoDeUsoBem(int idBem) {
        List<Estadia> historico = new ArrayList<>();
        for (Estadia estadia : repositorioEstadia.buscarEstadiasPorBem(idBem)) {
            historico.add(estadia);
        }
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

}
