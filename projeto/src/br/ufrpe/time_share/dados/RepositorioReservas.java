package br.ufrpe.time_share.dados;
import br.ufrpe.time_share.negocio.beans.Reserva;

public class RepositorioReservas extends Repositorio<Reserva> {
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
}
