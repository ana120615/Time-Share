package br.ufrpe.timeshare.negocio;

import br.ufrpe.timeshare.dados.IRepositorioEstadia;
import br.ufrpe.timeshare.dados.IRepositorioReservas;
import br.ufrpe.timeshare.excecoes.*;
import br.ufrpe.timeshare.negocio.beans.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ControladorReservas {
    private IRepositorioReservas repositorioReservas;
    private IRepositorioEstadia repositorioEstadia;

    public ControladorReservas(IRepositorioReservas instanciaInterfaceR, IRepositorioEstadia instanciaInterfaceE) {
        this.repositorioReservas = instanciaInterfaceR;
        this.repositorioEstadia = instanciaInterfaceE;
    }


    //check in
    public String checkin(int idReserva) throws EstadiaJaInicializadaException, ReservaNaoExisteException, ReservaJaCanceladaException, ForaPeriodoException {
        LocalDateTime dataInicio = LocalDateTime.now();
        Reserva reservaRelacionada = repositorioReservas.buscar(idReserva);
        Estadia verificadorEstadia = repositorioEstadia.buscarEstadiaPorReserva(idReserva);
        Estadia estadia;
        int duracao;

        if (verificadorEstadia != null) {
            throw new EstadiaJaInicializadaException("Check-in ja realizado nesta reserva.");
        }

        if (reservaRelacionada == null) {
            throw new ReservaNaoExisteException("Reserva inexistente.");
        } else {
            //criacao de id aleatorio
            int idAleatorio = 0;
            for (int i = 0; i < 1; ) {
                idAleatorio = ThreadLocalRandom.current().nextInt(1001, 99999 + 1);
                if (repositorioEstadia.buscar(idAleatorio) == null) {
                    i++; // Sai do loop se o ID for unico
                }
            }
            estadia = new Estadia(idAleatorio, reservaRelacionada);
            //verifica se data de check in esta no periodo reservado
            if (dataInicio.isBefore(reservaRelacionada.getDataInicio()) || dataInicio.isAfter(reservaRelacionada.getDataFim())) {
                throw new ForaPeriodoException("Data de inicio fora do periodo da reserva.");
            } else {
                estadia.setDataInicio(dataInicio);
                estadia.setDataFim(null); //a data final da estadia so vai ser alterada no check-out
                duracao = estadia.calcularDuracao();
                repositorioEstadia.cadastrar(estadia); //cadastra estadia no repositorio
            }

        }
        return gerarComprovanteEstadia(estadia, duracao);
    }

    //ao passar o tempo reservado, tenho a opcao de prolongar
    //a estadia ou fazer check out
    //pode ser cobrada uma taxa extra
    public String prolongarEstadia(Estadia estadia, int quantidadeDias, LocalDateTime agora) throws ReservaNaoExisteException, ReservaJaCanceladaException, ForaPeriodoException, PeriodoJaReservadoException, PeriodoNaoDisponivelParaReservaException, ReservaNaoExisteException, CotaJaReservadaException {
        LocalDateTime novaDataFim = estadia.getDataFim().plusDays(quantidadeDias);
        if (estadia.getDataFim().equals(agora) || estadia.getDataFim().isBefore(agora)) {
            alterarPeriodoReserva(estadia.getReserva().getId(), agora, novaDataFim);
            estadia.setDataFim(novaDataFim);
        }
        //retorna comprovante de novo periodo reservado 
        //que sera utilizado na estadia
        return gerarComprovanteReserva(estadia.getReserva(), estadia.getReserva().calcularDuracaoReserva());
    }


    public String checkout(int idestadia) throws ReservaNaoExisteException, ReservaJaCanceladaException, EstadiaNaoExisteException {
        LocalDateTime agora = LocalDateTime.now();
        Estadia estadia = repositorioEstadia.buscar(idestadia);

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

        return gerarComprovanteEstadia(estadia, estadia.calcularDuracao());
    }

    //metodo para criar reserva/ reservar
    public String criarReserva(LocalDateTime dataInicio, LocalDateTime dataFim, Usuario usuarioComum, Bem bem)
            throws DadosInsuficientesException, ReservaJaExisteException, ForaPeriodoException, PeriodoJaReservadoException, PeriodoNaoDisponivelParaReservaException, ReservaNaoExisteException, CotaJaReservadaException {
        double taxa;
        if (bem == null || dataInicio == null || dataFim == null || usuarioComum == null) {
            throw new DadosInsuficientesException("Bem, data de inicio, data final ou usuario nao podem ser nulos.");
        }

        //criacao de id aleatorio
        int idNumeroReserva = 0;
        for (int i = 0; i < 1; ) {
            idNumeroReserva = ThreadLocalRandom.current().nextInt(1001, 99999 + 1);
            if (repositorioReservas.buscar(idNumeroReserva) == null) {
                i++; // Sai do loop se o ID for unico
            }
        }
        //cria reserva
        Reserva reserva = new Reserva(idNumeroReserva, dataInicio, dataFim, usuarioComum, bem);

        //verifica se reserva ja existe
        Reserva reservaExistente = repositorioReservas.buscarReserva(reserva);
        if (reservaExistente != null) {
            throw new ReservaJaExisteException("Reserva ja existe.");
        }

        verificarPeriodo(bem, usuarioComum, dataInicio, dataFim);


        //cadastra reserva no repositorio e calcula taxa
        int dias = reserva.calcularDuracaoReserva();
        repositorioReservas.cadastrar(reserva);
        taxa = calcularTaxaExtra(reserva, dias);

        return gerarComprovanteReserva(reserva, taxa);
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

        if (!cota.isStatusDeDisponibilidadeParaReserva()) {
            throw new OperacaoNaoPermitidaException("A cota nao foi reservada anteriormente.");
        }

        cota.setStatusDeDisponibilidadeParaReserva(true);
        repositorioReservas.remover(reservaCancelada);
    }


    //metodo para cancelar reserva
    //e verificar reembolso
    //liberar periodo da cota para ser reservada
    //caso alguma tenha sido usada na reserva
    //a reserva e removida mesmo que nao haja reembolso
    public String cancelarReserva(int idReserva, Usuario usuario) throws ReservaNaoExisteException, ReservaJaCanceladaException, CotaJaReservadaException, UsuarioNaoPermitidoException, ReservaNaoReembolsavelException {
        Reserva reservaCancelada = repositorioReservas.buscar(idReserva);
        double reembolso;

        if (reservaCancelada == null) {
            throw new ReservaNaoExisteException("Reserva inexistente");
        }

        ArrayList<Cota> cotasBemAssociadoReserva = reservaCancelada.getBem().getCotas();


        if (!reservaCancelada.getUsuarioComum().equals(usuario)) {
            throw new UsuarioNaoPermitidoException("Reserva nao vinculada a este usuario.");
        }

        reembolso = reembolsar(reservaCancelada);

        String comprovanteCancelamento = "FLEX SHARE\n" + "____________________________\n" + "COMPROVANTE DE CANCELAMENTO DE RESERVA: \n" + "__________________________________________________\n" + "RESERVA: \n" + reservaCancelada.getId() + "\n Periodo: " + reservaCancelada.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "-" + reservaCancelada.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n REEMBOLSO: R$" + reembolso;
        //liberando cota
        for (Cota cota : cotasBemAssociadoReserva) {
            if (!cota.isStatusDeDisponibilidadeParaReserva() && reservaCancelada.getUsuarioComum().equals(cota.getProprietario())) {
                if ((reservaCancelada.getDataInicio().isBefore(cota.getDataFim()) || reservaCancelada.getDataInicio().isEqual(cota.getDataFim())) &&
                        (reservaCancelada.getDataFim().isAfter(cota.getDataInicio()) || reservaCancelada.getDataFim().isEqual(cota.getDataInicio()))) {
                    cota.setStatusDeDisponibilidadeParaReserva(true);
                }
            }
        }
        repositorioReservas.remover(reservaCancelada);
        return comprovanteCancelamento;
    }


    //metodo para reembolso apos cancelamento
    public double reembolsar(Reserva reserva) throws ReservaNaoReembolsavelException, NullPointerException, ReservaNaoExisteException, CotaJaReservadaException {
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


    //verifica se periodo da reserva e valido
    private void verificarPeriodo(Bem bem, Usuario usuario, LocalDateTime dataInicio, LocalDateTime dataFim) throws PeriodoJaReservadoException, ForaPeriodoException, PeriodoNaoDisponivelParaReservaException {
        LocalDateTime inicioAtual = dataInicio;
        ArrayList<Cota> cotasBemAssociado = bem.getCotas();

        //verifica se datas sao validas
        if (dataInicio.isAfter(dataFim)) {
            throw new ForaPeriodoException("A data inicial nao pode ser depois da data final");
        }


        while (!inicioAtual.isAfter(dataFim)) {

            //busca de reserva existente para o bem no periodo requisitado
            //verifica se reserva pertence ao mesmo bem e se esta ativa
            for (Reserva buscar : repositorioReservas.listar()) {
                if (buscar.getBem().equals(bem)) {
                    if (verificarConflitoDeDatasReserva(buscar, inicioAtual)) {
                        throw new PeriodoJaReservadoException("Periodo ja reservado.");
                    }
                }
            }

            //Verifica periodos em que ha cotas que ja foram vendidas e nao podem ser usadas para reservas de todos
            for (Cota cota : cotasBemAssociado) {
                if (cota.getProprietario() != null && !cota.getProprietario().equals(usuario) ||
                        !cota.getStatusDeDisponibilidadeParaCompra() || cota.getProprietario() != null &&
                        (cota.getProprietario().equals(usuario) && !cota.isStatusDeDisponibilidadeParaReserva())) {

                    if (verificarConflitoDeDatasCota(cota, inicioAtual)) {
                        throw new PeriodoNaoDisponivelParaReservaException("O periodo esta dentro de uma cota comprada por outro usuario.");
                    }

                }
            }


            inicioAtual = inicioAtual.plusDays(1);

        }
    }


    //metodo para alterar periodo da reserva
    public String alterarPeriodoReserva(long idReserva, LocalDateTime novaDataInicio, LocalDateTime novaDataFim)
            throws ReservaNaoExisteException, ReservaJaCanceladaException, ForaPeriodoException, PeriodoJaReservadoException, PeriodoNaoDisponivelParaReservaException, ReservaNaoExisteException, CotaJaReservadaException {

        //busca reserva pelo id
        Reserva reserva = repositorioReservas.buscar(idReserva);

        if (reserva == null) {
            throw new ReservaNaoExisteException("Reserva inexistente");
        }

        //verifica validade do periodo
        verificarPeriodo(reserva.getBem(), reserva.getUsuarioComum(), novaDataInicio, novaDataFim);


        // Atualizar dados da reserva
        reserva.setDataInicio(novaDataInicio);
        reserva.setDataFim(novaDataFim);
        int duracao = reserva.calcularDuracaoReserva();
        double taxaExtra;
        //calcula possivel taxa extra para alterar o periodo
        taxaExtra = calcularTaxaExtra(reserva, duracao);

        return gerarComprovanteReserva(reserva, taxaExtra);
    }

    // Consulta de periodos disponiveis para reserva
    // Considerando o usuario que deseja reservar
    public List<String> consultarDisponibilidadeParaReserva(Bem bem, LocalDateTime inicioPeriodo, LocalDateTime dataFim, Usuario usuario) throws BemNaoExisteException {
        List<String> periodosDisponiveis = new ArrayList<>();
        LocalDateTime inicioAtual = inicioPeriodo;
        boolean existeReservaAtiva;
        boolean existeCotaOcupada;

        // Buscar todas as reservas para o bem
        List<Reserva> reservas = repositorioReservas.buscarReservasPorBem(bem.getId());
        // Buscar as cotas do bem
        ArrayList<Cota> cotasBemAssociado = bem.getCotas();

        // Verifica se datas sao validas
        if (dataFim.isBefore(inicioPeriodo)) {
            throw new IllegalArgumentException("A data de inicio deve ser antes da data final.");
        }


        while (!inicioAtual.isAfter(dataFim)) {
            existeCotaOcupada = false;
            existeReservaAtiva = false;
            periodosDisponiveis.add(inicioAtual.toString());

            //Verifica periodos em que ha cotas que ja foram vendidas e nao podem ser usadas para reservas de todos
            for (int i = 0; i < cotasBemAssociado.size() && !existeCotaOcupada; i++) {
                if (cotasBemAssociado.get(i).getProprietario() != null && !cotasBemAssociado.get(i).getProprietario().equals(usuario) ||
                        !cotasBemAssociado.get(i).getStatusDeDisponibilidadeParaCompra() || cotasBemAssociado.get(i).getProprietario() != null &&
                        (cotasBemAssociado.get(i).getProprietario().equals(usuario) && !cotasBemAssociado.get(i).isStatusDeDisponibilidadeParaReserva())) {

                    existeCotaOcupada = verificarConflitoDeDatasCota(cotasBemAssociado.get(i), inicioAtual);

                }
            }

            // Verifica periodos em que ha reservas ativas
            for (int i = 0; i < reservas.size() && !existeReservaAtiva; i++) {
                existeReservaAtiva = verificarConflitoDeDatasReserva(reservas.get(i), inicioAtual);
            }

            if (existeCotaOcupada || existeReservaAtiva) {
                periodosDisponiveis.remove(inicioAtual.toString());
            }
            inicioAtual = inicioAtual.plusDays(1);
        }

        return periodosDisponiveis;
    }


    public List<String> consultarDisponibilidadeReservasGeral(Bem bem, LocalDateTime inicioPeriodo, LocalDateTime dataFim, Usuario usuario) throws BemNaoExisteException {
        List<String> periodosDisponiveis = new ArrayList<>();
        boolean existeReservaAtiva;
        boolean existeCotaOcupada;

        // Buscar todas as reservas para o bem
        List<Reserva> reservas = repositorioReservas.buscarReservasPorBem(bem.getId());
        // Buscar as cotas do bem
        ArrayList<Cota> cotasBemAssociado = bem.getCotas();
        // Garante que o usuário não posso colocar o final do periodo antes do inicio do periodo
        if (dataFim.isBefore(inicioPeriodo) || inicioPeriodo.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("\nInválido");
        }

        LocalDateTime inicioAtual = inicioPeriodo;

        while (!inicioAtual.isAfter(dataFim)) {
            existeReservaAtiva = false;
            existeCotaOcupada = false;
            periodosDisponiveis.add(inicioAtual.toString());

            //Verifica periodos em que ha cotas que ja foram vendidas e nao podem ser usadas para reservas de todos
            for (int i = 0; i < cotasBemAssociado.size() && !existeCotaOcupada; i++) {
                if (cotasBemAssociado.get(i).getProprietario() != null && !cotasBemAssociado.get(i).getProprietario().equals(usuario) ||
                        !cotasBemAssociado.get(i).getStatusDeDisponibilidadeParaCompra() || cotasBemAssociado.get(i).getProprietario() != null &&
                        (cotasBemAssociado.get(i).getProprietario().equals(usuario) && !cotasBemAssociado.get(i).isStatusDeDisponibilidadeParaReserva())) {

                    existeCotaOcupada = verificarConflitoDeDatasCota(cotasBemAssociado.get(i), inicioAtual);

                }
            }

            // Verifica periodos em que ha reservas ativas
            for (int i = 0; i < reservas.size() && !existeReservaAtiva; i++) {
                existeReservaAtiva = verificarConflitoDeDatasReserva(reservas.get(i), inicioAtual);
            }

            if (existeCotaOcupada || existeReservaAtiva) {
                periodosDisponiveis.remove(inicioAtual.toString());
            }

            inicioAtual = inicioAtual.plusDays(1);
        }

        return periodosDisponiveis;
    }

    private boolean verificarConflitoDeDatasCota(Cota cota, LocalDateTime inicioAtual) {
        boolean existeCotaOcupada = false;

        if (((inicioAtual.getYear() == cota.getDataInicio().getYear() &&
                (inicioAtual.getMonth() == cota.getDataInicio().getMonth()) &&
                (inicioAtual.getDayOfYear() >= cota.getDataInicio().getDayOfYear() && inicioAtual.getDayOfYear() <= cota.getDataFim().getDayOfYear())) ||
                ((inicioAtual.getYear() == cota.getDataFim().getYear()) &&
                        inicioAtual.getMonth() == cota.getDataFim().getMonth() &&
                        (inicioAtual.getDayOfYear() <= cota.getDataFim().getDayOfYear() && inicioAtual.getDayOfYear() >= cota.getDataInicio().getDayOfYear())))) {

            existeCotaOcupada = true;

        }


        return existeCotaOcupada;
    }


    private boolean verificarConflitoDeDatasReserva(Reserva reserva, LocalDateTime inicioAtual) {
        boolean existeReservaOcupada = false;

        if (((inicioAtual.getYear() == reserva.getDataInicio().getYear() &&
                (inicioAtual.getMonth() == reserva.getDataInicio().getMonth()) &&
                (inicioAtual.getDayOfYear() >= reserva.getDataInicio().getDayOfYear() && inicioAtual.getDayOfYear() <= reserva.getDataFim().getDayOfYear())) ||
                ((inicioAtual.getYear() == reserva.getDataFim().getYear()) &&
                        inicioAtual.getMonth() == reserva.getDataFim().getMonth() &&
                        (inicioAtual.getDayOfYear() <= reserva.getDataFim().getDayOfYear() && inicioAtual.getDayOfYear() >= reserva.getDataInicio().getDayOfYear())))) {

            existeReservaOcupada = true;

        }


        return existeReservaOcupada;
    }


    //metodo para calcular taxa extra
    //tambem e usado quando prolongo uma estadia
    //verifica se cota pertence ao usuario e corresponde ao periodo que deseja reservar
    public double calcularTaxaExtra(Reserva reserva, int quantidadeDias) throws ReservaNaoExisteException, CotaJaReservadaException {
        //taxa e uma porcentagem do preco de uma cota do bem
        double taxa = 0.00;
        boolean reservaTaxada = true;
        Promocao promocao = new Promocao();

        if (reserva == null) {
            throw new NullPointerException("Reserva nao pode ser nula");
        }

        if (repositorioReservas.buscarReserva(reserva) == null) {
            throw new ReservaNaoExisteException("Reserva inexistente");
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
                    if (!cota.isStatusDeDisponibilidadeParaReserva()) {
                        throw new CotaJaReservadaException("A cota ja foi utilizada em uma reserva");

                    } else {
                        cota.setStatusDeDisponibilidadeParaReserva(false);
                        reservaTaxada = false;
                        break;
                    }
                }
            }
        }


        //calcula a taxa baseada no preco de 1 cota e na quantidade de dias da reserva
        //5% ao dia
        if (reservaTaxada) {
            assert cotas != null;
            taxa = cotas.getFirst().getPreco() * 0.05 * quantidadeDias;
            double taxaPromocional = promocao.calcularTaxaPromocao(reserva.getDataInicio().withHour(0).withMinute(0), reserva.getUsuarioComum());
            double desconto = taxa * taxaPromocional;
            taxa -= desconto;
        }


        return Math.max(taxa, 0.00);
    }


    //metodo para gerar comprovante da reserva
    public String gerarComprovanteReserva(Reserva reserva, double taxa) {
        String reservaToString = reserva.toString();
        return reservaToString + "Taxa extra: R$" + String.format("%.2f", taxa);
    }

    //metodo para gerar comprovante da estadia
    public String gerarComprovanteEstadia(Estadia estadia, int duracao) {
        String estadiaToString = estadia.toString();
        return estadiaToString + "Duracao da estadia: " + String.format("%d", duracao) + " dias";
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


//RELATORIOS

    //HISTORICO DE USO DE UM BEM
    public List<Estadia> historicoDeUsoBem(int idBem) {
        List<Estadia> historico = new ArrayList<>();
        for (Estadia estadia : repositorioEstadia.buscarEstadiasPorBem(idBem)) {
            historico.add(estadia);
        }
        return historico;
    }


    //VER PERIODOS MAIS RESERVADOS
    public List<String> PeriodosMaisReservados(int idBem) {
        List<Reserva> reservas = repositorioReservas.buscarReservasPorBem(idBem);
        List<Estadia> estadias = repositorioEstadia.buscarEstadiasPorBem(idBem);
        List<String> periodos = new ArrayList<>();
        List<Integer> contagens = new ArrayList<>();


        for (Reserva reserva : reservas) {
            LocalDateTime dataInicio = reserva.getDataInicio();
            LocalDateTime dataFim = reserva.getDataFim();
            String periodo = dataInicio + " - " + dataFim;

            adicionarNosMaisReservados(periodo, periodos, contagens);
        }

        //so considerar periodos de estadias com reserva null
        //pois comprova o check out e nao havera repeticao
        for (Estadia estadia : estadias) {
            if (estadia.getReserva() == null) {
                LocalDateTime dataInicioEstadia = estadia.getDataInicio();
                LocalDateTime dataFimEstadia = estadia.getDataFim();

                // Verificar se a estadia cobre ou se sobrepoe com a reserva
                for (Reserva reserva : reservas) {
                    LocalDateTime dataInicioReserva = reserva.getDataInicio();
                    LocalDateTime dataFimReserva = reserva.getDataFim();

                    if (dataInicioEstadia.isBefore(dataFimReserva) && dataFimEstadia.isAfter(dataInicioReserva)) {
                        // Ajustar as datas para contabilizar todos os dias dentro da estadia
                        LocalDateTime periodoInicio = dataInicioEstadia.isBefore(dataInicioReserva) ? dataInicioEstadia : dataInicioReserva;
                        LocalDateTime periodoFim = dataFimEstadia.isAfter(dataFimReserva) ? dataFimEstadia : dataFimReserva;

                        String periodo = periodoInicio + " - " + periodoFim;
                        adicionarNosMaisReservados(periodo, periodos, contagens);
                    }
                }
            }
        }

        // Ordenando com bubble sort
        List<String> resultado = new ArrayList<>();
        for (int i = 0; i < contagens.size(); i++) {
            for (int j = i + 1; j < contagens.size(); j++) {
                if (contagens.get(i) < contagens.get(j)) {
                    int tempContagem = contagens.get(i);
                    contagens.set(i, contagens.get(j));
                    contagens.set(j, tempContagem);
                    String tempPeriodo = periodos.get(i);
                    periodos.set(i, periodos.get(j));
                    periodos.set(j, tempPeriodo);
                }
            }
        }


        for (int i = 0; i < periodos.size(); i++) {
            resultado.add(" - Quantidade: " + contagens.get(i) + ": " + periodos.get(i));
        }

        return resultado;
    }

    // metodo auxiliar para adicionar um periodo completo 
    private void adicionarNosMaisReservados(String periodo, List<String> periodos, List<Integer> contagens) {
        int index = periodos.indexOf(periodo);

        // Se o periodo ja existe, incrementa a contagem
        if (index != -1) {
            contagens.set(index, contagens.get(index) + 1);
        } else {
            // Se nao existe, adiciona um novo periodo com contagem 1
            periodos.add(periodo);
            contagens.add(1);
        }
    }
}
