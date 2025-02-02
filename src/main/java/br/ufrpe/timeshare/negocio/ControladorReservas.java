package br.ufrpe.timeshare.negocio;

import br.ufrpe.timeshare.dados.IRepositorioEstadia;
import br.ufrpe.timeshare.dados.IRepositorioReservas;
import br.ufrpe.timeshare.excecoes.*;
import br.ufrpe.timeshare.negocio.beans.*;

import java.time.LocalDate;
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
    public String checkin(int idReserva, LocalDateTime dataInicio) throws ReservaNaoExisteException, ReservaJaCanceladaException, ForaPeriodoException {
        Reserva reservaRelacionada = repositorioReservas.buscar(idReserva);
        Estadia estadia = null;
        int duracao = 0;
        if (reservaRelacionada == null) {
            throw new ReservaNaoExisteException("Reserva inexistente");
        } else {
            if (reservaRelacionada.isCancelada()) {
                throw new ReservaJaCanceladaException("Reserva ja cancelada");
            } else {
                int idAleatorio = 1001 + ThreadLocalRandom.current().nextInt(10000);
                estadia = new Estadia(idAleatorio, reservaRelacionada);
                //verifica se data de check in esta no periodo reservado
                if (dataInicio.isBefore(reservaRelacionada.getDataInicio()) || dataInicio.isAfter(reservaRelacionada.getDataFim())) {
                    throw new ForaPeriodoException("Data de inicio fora do periodo da reserva");
                } else {
                    estadia.setDataInicio(dataInicio);
                    estadia.setDataFim(reservaRelacionada.getDataFim());
                    duracao = estadia.calcularDuracao();
                    repositorioEstadia.cadastrar(estadia); //cadastra estadia no repositorio
                }
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


    public String checkout(Estadia estadia, LocalDateTime agora) throws ReservaNaoExisteException, ReservaJaCanceladaException {
        Reserva reserva = null;
        if (estadia.getDataFim().equals(agora) || estadia.getDataFim().isBefore(agora)) {
            reserva = repositorioReservas.buscarReserva(estadia.getReserva());
            if (reserva == null) {
                throw new ReservaNaoExisteException("Reserva inexistente");
            } else {
                if (reserva.isCancelada()) {
                    throw new ReservaJaCanceladaException("Reserva ja cancelada");
                } else {
                    repositorioReservas.remover(reserva); //remove reserva utilizada do repositorio
                }
            }
        }
        return gerarComprovanteEstadia(estadia, estadia.calcularDuracao());
    }

    //metodo para criar reserva/ reservar
    public String criarReserva(LocalDateTime dataInicio, LocalDateTime dataFim, Usuario usuarioComum, Bem bem)
            throws DadosInsuficientesException, ReservaJaExisteException, ForaPeriodoException, PeriodoJaReservadoException, PeriodoNaoDisponivelParaReservaException, ReservaNaoExisteException, CotaJaReservadaException {
        double taxa = 0.00;
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
    public String reservaPeriodoCota(Cota cota) throws CotaNaoExisteException, ProprietarioNaoIdentificadoException, DadosInsuficientesException, ReservaJaExisteException, ForaPeriodoException, PeriodoJaReservadoException, PeriodoNaoDisponivelParaReservaException, ReservaNaoExisteException, CotaJaReservadaException {
        String reservaFeita = null;
        if (cota == null) {
            throw new CotaNaoExisteException("Cota inexistente");
        }

        if (cota.getProprietario() == null) {
            throw new ProprietarioNaoIdentificadoException("Cota sem proprietário");
        }
        reservaFeita = criarReserva(cota.getDataInicio(), cota.getDataFim(), cota.getProprietario(), cota.getBemAssociado());
        return reservaFeita;

    }


    //metodo para cancelar reserva
    //e verificar reembolso
    //liberar periodo da cota para ser reservada
    //caso alguma tenha sido usada na reserva
    public String cancelarReserva(int idReserva, Usuario usuario) throws ReservaNaoExisteException, ReservaJaCanceladaException, ReservaNaoReembolsavelException, CotaJaReservadaException, UsuarioNaoPermitidoException {
        Reserva reservaCancelada = repositorioReservas.buscar(idReserva);
        
        if (reservaCancelada == null) {
            throw new ReservaNaoExisteException("Reserva inexistente");
        }

        ArrayList<Cota> cotasBemAssociadoReserva = reservaCancelada.getBem().getCotas();
        if (reservaCancelada.isCancelada()) {
            throw new ReservaJaCanceladaException("Reserva ja cancelada");
        }

        if (!reservaCancelada.getUsuarioComum().equals(usuario)) {
            throw new UsuarioNaoPermitidoException("Reserva nao vinculada a este usuario.");
        }

        reservaCancelada.cancelarReserva();
        String comprovanteCancelamento = "FLEX SHARE\n"+"____________________________\n"+"COMPROVANTE DE CANCELAMENTO DE RESERVA: \n"+"__________________________________________________\n"+"RESERVA: \n"+ reservaCancelada.getId+"\n Periodo: "+reservaCancelada.getDataInicio+"-"+reservaCancelada.getDataFim+"\n REEMBOLSO: "+reembolsar(reservaCancelada);
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
        if (reserva.isCancelada()) {
            if (reserva.getDataFim().toLocalDate().isBefore(LocalDate.now())) {
                throw new ReservaNaoReembolsavelException("Periodo da reserva expirado. Nao reembolsavel");
            }
            if (calcularTaxaExtra(reserva, dias) != 0) {
                reembolso += (calcularTaxaExtra(reserva, dias)) * 0.30;
            }

        }
        return reembolso;
    }

    //verifica se periodo da reserva e valido
    private void verificarPeriodo(Bem bem, Usuario usuario, LocalDateTime dataInicio, LocalDateTime dataFim) throws PeriodoJaReservadoException, ForaPeriodoException, PeriodoNaoDisponivelParaReservaException {

        //verifica se datas sao validas
        if (dataInicio.isAfter(dataFim)) {
            throw new ForaPeriodoException("A data inicial nao pode ser depois da data final");
        }

        //busca de reserva existente para o bem no periodo requisitado
        //verifica se reserva pertence ao mesmo bem e se esta ativa
        for (Reserva buscar : repositorioReservas.listar()) {
            if (!buscar.isCancelada() && buscar.getBem().equals(bem) &&
                    !(dataFim.isBefore(buscar.getDataInicio()) || dataInicio.isAfter(buscar.getDataFim()))) {
                throw new PeriodoJaReservadoException("Periodo ja reservado.");
            }
        }

        //Verifica se periodo requisitado pertence a cota de outro usuario
        ArrayList<Cota> cotasBemAssociadoReserva = bem.getCotas();
        for (Cota cota : cotasBemAssociadoReserva) {
            if (!cota.getStatusDeDisponibilidadeParaCompra() && !usuario.equals(cota.getProprietario())) {
                if ((dataInicio.isBefore(cota.getDataFim()) || dataInicio.isEqual(cota.getDataFim())) &&
                        (dataFim.isAfter(cota.getDataInicio()) || dataFim.isEqual(cota.getDataInicio()))) {
                    throw new PeriodoNaoDisponivelParaReservaException("Esse período está incluso numa cota vendida.");
                }
            }

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

        if (reserva.isCancelada()) {
            throw new ReservaJaCanceladaException("Reserva ja cancelada");
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

    //consulta de periodos disponiveis para reserva
//considerando o usuario que deseja reservar
    public List<String> consultarDisponibilidadeParaReserva(Bem bem, LocalDateTime inicioPeriodo, LocalDateTime fimPeriodo, Usuario usuario) throws BemNaoExisteException {
        List<String> periodosDisponiveis = new ArrayList<>();
        LocalDateTime inicioAtual = inicioPeriodo;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        boolean existeReservaAtiva = false;
        boolean existeCotaOcupada = false;

        // Buscar todas as reservas para o bem
        List<Reserva> reservas = repositorioReservas.buscarReservasPorBem(bem.getId());
        //Buscar as cotas do bem
        ArrayList<Cota> cotasBemAssociado = bem.getCotas();

        //verifica se datas sao validas
        if (fimPeriodo.isBefore(inicioPeriodo)) {
            throw new IllegalArgumentException("A data de inicio deve ser antes da data final");
        }


        while (!inicioAtual.isAfter(fimPeriodo)) {
            periodosDisponiveis.add(inicioAtual.format(formatter));

            //verifica periodos em que ha cotas que ja foram vendidas
            //e nao podem ser usadas para reservas de todos
            for (Cota cota : cotasBemAssociado) {
                if (!cota.getProprietario().equals(usuario) || !cota.getStatusDeDisponibilidadeParaCompra() || (cota.getProprietario().equals(usuario) && !cota.isStatusDeDisponibilidadeParaReserva())) {
                    if ((inicioAtual.isBefore(cota.getDataFim()) || inicioAtual.isEqual(cota.getDataFim())) &&
                            (fimPeriodo.isAfter(cota.getDataInicio()) || fimPeriodo.isEqual(cota.getDataInicio()))) {
                        existeCotaOcupada = true;
                    }
                }
            }

            //verifica periodos em que ha reservas ativas
            for (Reserva buscar : reservas) {
                if (!buscar.isCancelada() &&
                        !(inicioAtual.isBefore(buscar.getDataInicio()) || inicioAtual.isAfter(buscar.getDataFim()))) {
                    existeReservaAtiva = true;
                }
            }

            if (existeCotaOcupada || existeReservaAtiva) {
                periodosDisponiveis.remove(inicioAtual.toString());
            }
            inicioAtual = inicioAtual.plusDays(1);
        }

        return periodosDisponiveis;
    }


    //TODO verificar se vai ser necessario esse metodo de consultar disponibilidade do bem
    //relatorio
//consulta de disponibilidade futura do bem
    public List<String> consultarDisponibilidadeDoBem(Bem bem, LocalDateTime inicioPeriodo, LocalDateTime fimPeriodo) throws BemNaoExisteException {
        List<String> periodosDisponiveis = new ArrayList<>();
        LocalDateTime inicioAtual = inicioPeriodo;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        boolean existeReservaAtiva = false;
        boolean existeCotaOcupada = false;

        // Buscar todas as reservas para o bem
        List<Reserva> reservas = repositorioReservas.buscarReservasPorBem(bem.getId());
        //Buscar as cotas do bem
        ArrayList<Cota> cotasBemAssociado = bem.getCotas();

        //verifica se datas sao validas
        if (fimPeriodo.isBefore(inicioPeriodo) || inicioPeriodo.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("\nInvalido");
        }


        while (!inicioAtual.isAfter(fimPeriodo)) {
            periodosDisponiveis.add(inicioAtual.format(formatter));

            //verifica periodos em que ha cotas que ja foram vendidas
            //e nao podem ser usadas para reservas de todos
            for (Cota cota : cotasBemAssociado) {
                if (!cota.getStatusDeDisponibilidadeParaCompra()) {
                    if ((inicioAtual.isBefore(cota.getDataFim()) || inicioAtual.isEqual(cota.getDataFim())) &&
                            (fimPeriodo.isAfter(cota.getDataInicio()) || fimPeriodo.isEqual(cota.getDataInicio()))) {
                        existeCotaOcupada = true;
                    }
                }
            }

            //verifica periodos em que ha reservas ativas
            for (Reserva buscar : reservas) {
                if (!buscar.isCancelada() &&
                        !(inicioAtual.isBefore(buscar.getDataInicio()) || inicioAtual.isAfter(buscar.getDataFim()))) {
                    existeReservaAtiva = true;
                }
            }

            if (existeCotaOcupada || existeReservaAtiva) {
                periodosDisponiveis.remove(inicioAtual.toString());
            }
            inicioAtual = inicioAtual.plusDays(1);
        }

        return periodosDisponiveis;
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
        if (cotas == null) {
            reservaTaxada = true;
        }

        // Verifica se a cota cobre a reserva
        else {
            for (Cota cota : cotas) {
                if (cota.getProprietario().equals(reserva.getUsuarioComum())) {
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
                            throw new CotaJaReservadaException("A cota já foi utilizada em uma reserva");

                        } else {
                            cota.setStatusDeDisponibilidadeParaReserva(false);
                            reservaTaxada = false;
                            break;
                        }
                    }
                }
            }
        }

        //calcula a taxa baseada no preco de 1 cota e na quantidade de dias da reserva
        //5% ao dia
        if (reservaTaxada) {
            taxa = cotas.get(0).getPreco() * 0.05 * quantidadeDias;
            double taxaPromocional = promocao.calcularTaxaPromocao(reserva.getDataInicio(), reserva.getUsuarioComum());
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
            LocalDate dataInicio = reserva.getDataInicio().toLocalDate();
            LocalDate dataFim = reserva.getDataFim().toLocalDate();
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

                        String periodo = periodoInicio.toLocalDate() + " - " + periodoFim.toLocalDate();
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
