package br.ufrpe.time_share.dados;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Reserva;
// pendencias : listarReservas, salvar Reservas, ver a lógica para quem não tem cotas. 
public class RepositorioReservas extends Repositorio<Reserva> {
    private RepositorioCotas repositorioCotas;
    //Construtor
    public RepositorioReservas(){
        super();
        this.reservas = new ArrayList<>();
    }
    private List<Reserva> reservas;
    public Reserva buscarPorId(int id){
    Reserva resultado=null;
    for (Reserva reserva : getEntidades()) {
            if (reserva.getId()==id) {
                resultado= reserva;
            }
        }
    return resultado;
    }
    public List<Reserva> buscarReservaPorBem(int bemId){
       List<Reserva> reservasPorBem = new ArrayList<>();

       for ( Reserva reserva : reservas){

        if(reserva.getBem().getId() == bemId){
            reservasPorBem.add(reserva);
        }
        
       }
       return reservasPorBem;
        
    }
    public List<Reserva> buscarReservaPorUsuarioAdm(int usuarioCPF){
        List<Reserva> reservasPorUsuario = new ArrayList<>();
        for (Reserva reserva : reservas ){
            if (reserva.getUsuarioAdm().getCpf() == usuarioCPF){
                reservasPorUsuario.add(reserva);
            }
        }
        return reservasPorUsuario;
    }
    public List<Reserva> buscarreservaPorUsuarioComum(int usuarioCPF){
        List<Reserva> reservasPorUsuario = new ArrayList<>();
        for (Reserva reserva : reservas ){
            if (reserva.getUsuarioComum().getCpf() == usuarioCPF){
                reservasPorUsuario.add(reserva);
            }
        }
        return reservasPorUsuario;
    }
    public boolean cancelarReserva (int idreserva){
        Reserva reserva = buscarPorId(idreserva);
        if ( reserva != null && reserva.getStatus()){
        reserva.cancelarReserva();
        disponibilizarCotas(reserva);
        return true;
        }
        return false;
    }
   public void disponibilizarCotas (Reserva reserva){
    for (Cota cota : reserva.getCotas()){
        //*repositorioCotas.atualizarStarusCota(cota.getId(), true);  como comentário porque o repositorio de reservas está todo como comentário*//
    }
   }


}
