package br.ufrpe.time_share.negocio;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import br.ufrpe.time_share.dados.IRepositorioCotas;
import br.ufrpe.time_share.dados.IRepositorioReservas;
import br.ufrpe.time_share.excecoes.*;
import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Estadia;
import br.ufrpe.time_share.negocio.beans.Promocao;
import br.ufrpe.time_share.negocio.beans.Reserva;
import br.ufrpe.time_share.negocio.beans.Usuario;

public class ControladorReservas {
    private IRepositorioReservas repositorioReservas;


    public ControladorReservas(IRepositorioReservas instanciaInterface) {
        this.repositorioReservas = instanciaInterface;
    }

    public Estadia checkin(int idReserva, LocalDateTime dataInicio) throws ReservaNaoExisteException, ReservaJaCanceladaException, ForaPeriodoException {
        Reserva reservaRelacionada = repositorioReservas.buscar(idReserva);
        Estadia estadia = null;
        if (reservaRelacionada == null) {
            throw new ReservaNaoExisteException("Reserva inexistente");
        } else {
            if (reservaRelacionada.isCancelada()) {
                throw new ReservaJaCanceladaException("Reserva ja cancelada");
            } else {
                int idAleatorio = 1001 + ThreadLocalRandom.current().nextInt(10000);
                estadia = new Estadia(idAleatorio, reservaRelacionada);
                if (dataInicio.isBefore(reservaRelacionada.getDataInicio()) || dataInicio.isAfter(reservaRelacionada.getDataFim())) {
                    throw new ForaPeriodoException("Data de inicio fora do periodo da reserva");
                } else {
                    estadia.setDataInicio(dataInicio);
                    estadia.setDataFim(reservaRelacionada.getDataFim());
                }
            }
        }
        return estadia;
    }

    //ao passar o tempo reservado, tenho a opcao de prolongar
    //a estadia ou fazer check out
    public Estadia prolongarEstadia(Estadia estadia) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime novaDataFim = estadia.getDataFim().plusDays(6).withHour(23).withMinute(59).withSecond(59);

        if (estadia.getDataFim().equals(agora) || estadia.getDataFim().isBefore(agora)) {
            try {
                alterarPeriodoReserva(estadia.getReserva().getId(), agora, novaDataFim);
                estadia.setDataFim(novaDataFim);
            } catch (ReservaNaoExisteException e) {
                System.out.println(e.getMessage());

            } catch (ReservaJaCanceladaException e) {
                System.out.println(e.getMessage());
            } 
        }
        return estadia;
    }

    public int checkout(Estadia estadia) throws ReservaNaoExisteException, ReservaJaCanceladaException {
        Reserva reserva = null;
        LocalDateTime agora = LocalDateTime.now();
        if (estadia.getDataFim().equals(agora) || estadia.getDataFim().isBefore(agora)) {
            reserva = repositorioReservas.buscarReserva(estadia.getReserva());
            if (reserva == null) {
                throw new ReservaNaoExisteException("Reserva inexistente");
            } else {
                if (reserva.isCancelada() ) {
                    throw new ReservaJaCanceladaException("Reserva ja cancelada");
                } else {
                    estadia.getReserva().setStatus(false);
                    
                }
            }
        }
        return estadia.calcularDuracao();
    }

    //metodo para criar reserva/ reservar
    public Reserva criarReserva(LocalDateTime dataInicio, LocalDateTime dataFim, Usuario usuarioComum, Bem bem)
            throws ReservaJaExisteException, PeriodoJaReservadoException, DadosInsuficientesException, ForaPeriodoException, PeriodoNaoDisponivelParaReservaException {

        if (bem == null || dataInicio == null || dataFim == null || usuarioComum == null) {
            throw new DadosInsuficientesException("Bem, data de inicio, data final ou usuario nao podem ser nulos.");
        }

        Random random = new Random();
        int idNumeroReserva = 0;

        // Garante ID diferente
        for (int i = 0; i < 1; ) {
            idNumeroReserva = 1001 + random.nextInt(99999);
            if (repositorioReservas.buscar(idNumeroReserva) == null) {
                i++;
            }
        }

        //cria reserva
        Reserva reserva = new Reserva(idNumeroReserva, dataInicio, dataFim, usuarioComum, bem);

        //verifica se reserva ja existe
        Reserva reservaExistente = repositorioReservas.buscarReserva(reserva);
        if (reservaExistente != null) {
            throw new ReservaJaExisteException("Reserva ja existe.");
        }

        //verifica se periodo e valido
        if (dataInicio.isAfter(dataFim)||dataInicio.isBefore(LocalDateTime.now())) {
            throw new ForaPeriodoException("A data inicial nao pode ser depois da data final ou antes da data atual");
        }

        //busca de reserva existente para o bem no periodo requisitado
        //verifica se reserva pertence ao mesmo bem e se esta ativa
        for (Reserva buscar : repositorioReservas.listar()) {
            if (!buscar.isCancelada() && buscar.getBem().equals(reserva.getBem()) &&
                    !(reserva.getDataFim().isBefore(buscar.getDataInicio()) || reserva.getDataInicio().isAfter(buscar.getDataFim()))) {
                throw new PeriodoJaReservadoException("Periodo ja reservado.");
            }
        }

        //Verifica se periodo requisitado pertence a cota de outro usuario
        ArrayList<Cota> cotasBemAssociadoReserva = bem.getCotas();
        for (Cota cota : cotasBemAssociadoReserva) {
            if (!cota.getStatusDeDisponibilidadeParaCompra() && !usuarioComum.equals(cota.getProprietario())) {
                if ( (reserva.getDataInicio().isBefore(cota.getDataFim()) || reserva.getDataInicio().isEqual(cota.getDataFim())) &&
                (reserva.getDataFim().isAfter(cota.getDataInicio()) || reserva.getDataFim().isEqual(cota.getDataInicio()))) {
                    throw new PeriodoNaoDisponivelParaReservaException("Esse período está incluso numa cota vendida.");
                }
            }

        }

        //cadastra reserva no repositorio e calcula taxa
        int dias = reserva.calcularDuracaoReserva();
        repositorioReservas.cadastrar(reserva);
        try {
            calcularTaxaExtra(reserva, dias);
        } catch (ReservaNaoExisteException | CotaJaReservadaException e) {
            e.printStackTrace();
        }
        return reserva;
    }


//metodo para reservar o periodo completo de 1 cota
public void reservaPeriodoCota (Cota cota) throws CotaNaoExisteException, ProprietarioNaoIdentificadoException{
    if (cota == null) {
        throw new CotaNaoExisteException("Cota inexistente");
    }

    if (cota.getProprietario() == null) {
        throw new ProprietarioNaoIdentificadoException("Cota sem proprietário");
    }

    try {
        criarReserva(cota.getDataInicio(), cota.getDataFim(), cota.getProprietario(), cota.getBemAssociado());
    } catch (ReservaJaExisteException | PeriodoJaReservadoException | DadosInsuficientesException | ForaPeriodoException | PeriodoNaoDisponivelParaReservaException e) {
        System.out.println(e.getMessage());

    }
}


    //metodo para cancelar reserva
    public void cancelarReserva(int idReserva) throws ReservaNaoExisteException, ReservaJaCanceladaException {
        Reserva reservaCancelada = repositorioReservas.buscar(idReserva);
        if (reservaCancelada == null) {
            throw new ReservaNaoExisteException("Reserva inexistente");
        } else {
            if (reservaCancelada.isCancelada()) {
                throw new ReservaJaCanceladaException("Reserva ja cancelada");
            } else {
                reservaCancelada.cancelarReserva();
                try {
                    reembolsar(reservaCancelada);
                } catch (ReservaNaoReembolsavelException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //metodo para reembolso apos cancelamento
    public double reembolsar(Reserva reserva) throws ReservaNaoReembolsavelException {
        double reembolso = 0.00;
        int dias = reserva.calcularDuracaoReserva();
        if (reserva.isCancelada()) {
            if(reserva.getDataFim().isBefore(LocalDateTime.now())){
                throw new ReservaNaoReembolsavelException("Periodo da reserva expirado. Nao reembolsavel");
            }
            try {
                if (calcularTaxaExtra(reserva, dias) != 0) {
                    reembolso += (calcularTaxaExtra(reserva, dias)) * 0.30;
                } 
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            } catch (ReservaNaoExisteException e) {
                System.out.println(e.getMessage());
            } catch (CotaJaReservadaException e) {
                System.out.println(e.getMessage());
            }
        }
        return reembolso;
    }

    //verifica se periodo da reserva e valido
    public void verificarPeriodo(Reserva reserva, LocalDateTime dataInicial, LocalDateTime dataFinal) throws PeriodoJaReservadoException, ForaPeriodoException, PeriodoNaoDisponivelParaReservaException{
        
        //verifica se datas sao validas
        if (dataInicial.isAfter(dataFinal)||dataInicial.isBefore(LocalDateTime.now())) {
            throw new ForaPeriodoException("A data inicial nao pode ser depois da data final ou antes da data atual");
        }

       //busca de reserva existente para o bem no periodo requisitado
       //verifica se reserva pertence ao mesmo bem e se esta ativa
       for (Reserva buscar : repositorioReservas.listar()) {
           if (!buscar.isCancelada() && buscar.getBem().equals(reserva.getBem()) &&
                   !(reserva.getDataFim().isBefore(buscar.getDataInicio()) || reserva.getDataInicio().isAfter(buscar.getDataFim()))) {
               throw new PeriodoJaReservadoException("Periodo ja reservado.");
           }
       }

       //Verifica se periodo requisitado pertence a cota de outro usuario
       ArrayList<Cota> cotasBemAssociadoReserva = reserva.getBem().getCotas();
       for (Cota cota : cotasBemAssociadoReserva) {
           if (!cota.getStatusDeDisponibilidadeParaCompra() && !reserva.getUsuarioComum().equals(cota.getProprietario())) {
               if ( (reserva.getDataInicio().isBefore(cota.getDataFim()) || reserva.getDataInicio().isEqual(cota.getDataFim())) &&
               (reserva.getDataFim().isAfter(cota.getDataInicio()) || reserva.getDataFim().isEqual(cota.getDataInicio()))) {
                   throw new PeriodoNaoDisponivelParaReservaException("Esse período está incluso numa cota vendida.");
               }
           }

       }
    }



    //metodo para alterar periodo da reserva
    public Reserva alterarPeriodoReserva(long idReserva, LocalDateTime novaDataInicio, LocalDateTime novaDataFim)
            throws ReservaNaoExisteException, ReservaJaCanceladaException {
        
        //busca reserva pelo id
        Reserva reserva = repositorioReservas.buscar(idReserva);

        if (reserva == null) {
            throw new ReservaNaoExisteException("Reserva inexistente");
        }

        if (reserva.isCancelada()) {
            throw new ReservaJaCanceladaException("Reserva ja cancelada");
        }
       
        //verifica validade do periodo
        try {
            verificarPeriodo(reserva, novaDataInicio, novaDataFim);
        } catch (ForaPeriodoException e) {
            e.getMessage();
        }
        catch(PeriodoJaReservadoException e){
        e.getMessage();
        }
        catch(PeriodoNaoDisponivelParaReservaException e){
       e.getMessage();
        }

       // Atualizar dados da reserva    
        reserva.setDataInicio(novaDataInicio);
        reserva.setDataFim(novaDataFim);
        return reserva;
    }

//talvez metodo para verificar disponibilidade para usuario especifico


//consulta de periodos disponiveis para reserva
    public List<String> consultarDisponibilidade(Bem bem, LocalDateTime inicioPeriodo, LocalDateTime fimPeriodo) throws BemNaoExisteException {
        List<String> periodosDisponiveis = new ArrayList<>();
        LocalDateTime inicioAtual = inicioPeriodo;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        boolean existeReservaAtiva=false;
        boolean existeCotaOcupada=false;

       // Buscar todas as reservas para o bem
       List<Reserva> reservas = repositorioReservas.buscarReservasPorBem(bem.getId());
       //Buscar as cotas do bem
       ArrayList<Cota> cotasBemAssociado = bem.getCotas();

        //verifica se datas sao validas
        if (fimPeriodo.isBefore(inicioPeriodo) || inicioPeriodo.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("\nInválido");
        }

  
        while (!inicioAtual.isAfter(fimPeriodo)) {
            periodosDisponiveis.add(inicioAtual.format(formatter));
            
              //verifica periodos em que ha cotas que ja foram vendidas
            //e nao podem ser usadas para reservas de todos
            for (Cota cota : cotasBemAssociado) {
                if (!cota.getStatusDeDisponibilidadeParaCompra()) {
                    if ( (inicioAtual.isBefore(cota.getDataFim()) || inicioAtual.isEqual(cota.getDataFim())) &&
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

            if(existeCotaOcupada||existeReservaAtiva){
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
    if(cotas==null){
        reservaTaxada = true;
    }

        // Verifica se a cota cobre a reserva
        else{
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
    //0.15% ao dia
        if (reservaTaxada) {
            taxa = cotas.get(0).getPreco()*0.0015*quantidadeDias;
            double taxaPromocional = promocao.calcularTaxaPromocao(reserva.getDataInicio(), reserva.getUsuarioComum());
            double desconto = taxa * taxaPromocional;
            taxa -= desconto;
        }


        return Math.max(taxa, 0.00);
    }



    //metodo para gerar comprovante da reserva
    public String gerarComprovanteReserva(Reserva reserva, double taxa) {
        String reservaToString = reserva.toString();
        return reservaToString + ", taxa extra: R$" + String.format("%.2f", taxa);
    }

    //metodo para gerar comprovante da estadia
    public String gerarComprovanteEstadia(Estadia estadia, int duracao) {
        String estadiaToString = estadia.toString();
        return estadiaToString + ", duracao da estadia: " + String.format("%d", duracao) + " dias";
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


}
