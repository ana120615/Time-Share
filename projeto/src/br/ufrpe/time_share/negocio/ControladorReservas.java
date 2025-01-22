package br.ufrpe.time_share.negocio;


import java.time.LocalDateTime;
import java.util.ArrayList;

import br.ufrpe.time_share.dados.IRepositorioReservas;
import br.ufrpe.time_share.excecoes.BemNaoExisteException;
import br.ufrpe.time_share.excecoes.CotaJaReservadaException;
import br.ufrpe.time_share.excecoes.PeriodoJaReservadoException;
import br.ufrpe.time_share.excecoes.ReservaJaCanceladaException;
import br.ufrpe.time_share.excecoes.ReservaJaExisteException;
import br.ufrpe.time_share.excecoes.ReservaNaoExisteException;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Promocao;
import br.ufrpe.time_share.negocio.beans.Reserva;

public class ControladorReservas {
    private IRepositorioReservas repositorio;

    public ControladorReservas(IRepositorioReservas instanciaInterface) {
        this.repositorio = instanciaInterface;
    }

//checkin e checkout deve ser aqui? se for:
//so posso fazer alterar se nao tiver usado a reserva
//ao fazer checkout deve exluir reserva

//deve ter ilegal argument?
//metodo para criar reserva/ reservar
    public Reserva criarReserva(Reserva reserva)throws ReservaJaExisteException, PeriodoJaReservadoException{
        Reserva novaReserva;
        if(reserva==null){
            throw new NullPointerException();
        }
        else{
         novaReserva = repositorio.buscarReservasPorId(reserva.getId());
        if(novaReserva!=null){
            throw new ReservaJaExisteException("Reserva ja existe");
        }
        //poderia colocar no repositorio buscar reserva por periodo
         for(Reserva buscar: repositorio.listarReservas()){
        if(!(reserva.getDataFim().isBefore(buscar.getDataInicio()))||!(reserva.getDataInicio().isAfter(reserva.getDataFim()))&&buscar.getStatus()){
            throw new PeriodoJaReservadoException("Esse periodo ja esta reservado");
        }
        else{
            repositorio.cadastrarReserva(novaReserva);
        }
    }
}
    return novaReserva;
    }

//metodo para cancelar reserva    
    public Reserva cancelarReserva(int idReserva)throws ReservaNaoExisteException, ReservaJaCanceladaException{
        Reserva reservaCancelada;
        reservaCancelada = repositorio.buscarReservasPorId(idReserva);
        if(reservaCancelada==null){
            throw new ReservaNaoExisteException("Reserva inexistente");
        }
        else{
            if(!(reservaCancelada.getStatus())){
            throw new ReservaJaCanceladaException("Reserva ja cancelada");
            }
            else{
              reservaCancelada.cancelarReserva();  
            } 
        }
    return reservaCancelada;
    }

    //metodo para reembolso apos cancelamento
    //talvez colocar mensagem no nullpointer
    public double reembolsar(Reserva reserva){
        double reembolso=0.00;
    if(!(reserva.getStatus())){
    try {
        if(calcularTaxaExtra(reserva)!=0){
        reembolso+=(calcularTaxaExtra(reserva))*0.30;
        }
    } 
    catch(NullPointerException e){
        e.getMessage();
    }
    catch (ReservaNaoExisteException e) {
        e.getMessage();
    }
    catch(CotaJaReservadaException e){
        e.getMessage();
    }
    }
    return reembolso;
    }
    
    //metodo para alterar periodo da reserva
    public Reserva alterarPeriodoReserva(int idReserva,LocalDateTime novaDataInicio,LocalDateTime novaDataFim) throws ReservaNaoExisteException, ReservaJaCanceladaException{
    Reserva reserva = repositorio.buscarReservasPorId(idReserva);
   if(reserva==null){
    throw new ReservaNaoExisteException("Reserva inexistente");
   }
   else{
    if(!(reserva.getStatus())){
        throw new ReservaJaCanceladaException("Reserva ja cancelada");
    }
    else{
        reserva.setDataInicio(novaDataInicio);
        reserva.setDataFim(novaDataFim);
        repositorio.atualizarReserva(reserva);
    }
   }
    return reserva;
    }



    public ArrayList<LocalDateTime[]> consultarDisponibilidade(int bemId, LocalDateTime inicioPeriodo, LocalDateTime fimPeriodo)throws BemNaoExisteException {
ArrayList<LocalDateTime[]> periodosDisponiveis = new ArrayList<>();
       // Buscar todas as reservas para o bem
       ArrayList<Reserva> reservas = repositorio.buscarReservasPorBem(bemId); 

        if (reservas==null) {
            throw new BemNaoExisteException("Bem não existe");
        }
        else{
        LocalDateTime inicioAtual = inicioPeriodo;
        LocalDateTime fimAtual;
        while (!inicioAtual.isAfter(fimPeriodo)) {
             fimAtual = inicioAtual.plusDays(6).withHour(23).withMinute(59).withSecond(59);//1 semana
            if (fimAtual.isAfter(fimPeriodo)) {
                fimAtual = fimPeriodo; 
            }

            LocalDateTime[] periodo = {inicioAtual, fimAtual};

            boolean disponivel = true;

            //verificando reservas
            for (Reserva reservaBusca : reservas) {
                if ((inicioAtual.isBefore(reservaBusca.getDataFim()) || inicioAtual.equals(reservaBusca.getDataFim())) &&
                    (fimAtual.isAfter(reservaBusca.getDataInicio()) || fimAtual.equals(reservaBusca.getDataInicio())) || !(reservaBusca.getStatus())) {
                    disponivel = false;
                    break;
                }

            //verificando cotas
                for (Cota cota : reservaBusca.getBem().getCotas()) { 
                    if (!(cota.isStatusDeDisponibilidadeParaReserva())&&(inicioAtual.isBefore(cota.getDataFim()) || inicioAtual.equals(cota.getDataFim())) &&
                        (fimAtual.isAfter(cota.getDataInicio()) || fimAtual.equals(cota.getDataInicio()))) {
                        disponivel = false;
                        break;
                    }
                }
            }

            if (disponivel) {
                periodosDisponiveis.add(periodo);
            }

            inicioAtual = fimAtual.plusSeconds(1); 
        }
        }
        return periodosDisponiveis;
    }
    

    //metodo para calcular taxa extra
    public double calcularTaxaExtra(Reserva reserva) throws ReservaNaoExisteException, CotaJaReservadaException{
            double taxa = 0.00;
            boolean reservaTaxada = true;
            Promocao promocao=new Promocao();
        if(reserva==null){
            throw new NullPointerException();
        }
        else{
            if(repositorio.buscarReserva(reserva)==null){
                throw new ReservaNaoExisteException("Reserva inexistente");
            }
            else{
                
                    for(Cota cota: reserva.getBem().getCotas()){
                if(cota.getDataInicio().equals(reserva.getDataInicio())&&cota.getDataFim().equals(reserva.getDataFim())){
                    if(!(cota.isStatusDeDisponibilidadeParaReserva())){
                        throw new CotaJaReservadaException("A cota ja foi utilizada em uma reserva");
                    }
                    else{
                        cota.setStatusDeDisponibilidadeParaReserva(false);
                        reservaTaxada = false;
                        break;
                    }
            }
                    }
                    if(reservaTaxada){
                    taxa = 150;
                    }
                }
            }
        return taxa-=promocao.calcularTaxaPromocao(reserva.getDataInicio(), reserva.getUsuarioComum());   
    }
}
