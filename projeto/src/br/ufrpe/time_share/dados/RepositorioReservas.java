package br.ufrpe.time_share.dados;
import java.time.LocalDate;

import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Reserva;

public class RepositorioReservas extends Repositorio<Reserva> {
    private RepositorioCotas repositorioCotas;
    //Construtor
    public RepositorioReservas(){
        super();
    }
    public Reserva buscarPorId(int id){
    Reserva resultado=null;
    for (Reserva reserva : getEntidades()) {
            if (reserva.getId()==id) {
                resultado= reserva;
            }
        }
    return resultado;
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
