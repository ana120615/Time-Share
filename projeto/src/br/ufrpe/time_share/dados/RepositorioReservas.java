package br.ufrpe.time_share.dados;

import java.util.ArrayList;
import br.ufrpe.time_share.negocio.beans.Reserva;
import br.ufrpe.time_share.negocio.beans.Usuario;

public class RepositorioReservas implements IRepositorioReservas {

    // INSTANCIA UNICA DO REPOSITORIO
    public static final RepositorioReservas INSTANCE = new RepositorioReservas();

    private ArrayList<Reserva> reservas;

    {
        this.reservas = new ArrayList<>();
    }

    // METODO PARA OBTER A INSTANCIA DO REPOSITORIO
    public static RepositorioReservas getInstance() {
        return INSTANCE;
    }

    @Override
    public void cadastrarReserva(Reserva reserva) {
        this.reservas.add(reserva);
    }

    @Override
    public void removerReserva(Reserva reserva) {
    this.reservas.remove(reserva);
    }

    @Override
    public Reserva buscarReserva(Reserva reserva) {
        Reserva resultado = null;
        boolean reservaExiste = false;
        int i;
        for (i = 0; i < this.reservas.size() && !reservaExiste; i++) {
            if (this.reservas.get(i).equals(reserva)) {
                reservaExiste = true;
            }
        }
        if (reservaExiste) {
            resultado = this.reservas.get(i - 1);
        }
        return resultado;
    }

    @Override
    public Reserva buscarReservasPorId(int idReserva) {
         Reserva retorno = null;
    for(Reserva reserva: reservas){
        if(reserva.getId()==idReserva){
        retorno = reserva;
        }
    }
        return retorno;
    }

    @Override
    public ArrayList<Reserva> buscarReservasPorUsuario(Usuario usuario) {
        ArrayList<Reserva> resultado = new ArrayList<>();
        for (Reserva reservaUser : this.reservas) {
            if (reservaUser.getUsuarioComum().equals(usuario)) {
                resultado.add(reservaUser);
            }
        }
        return new ArrayList<>(resultado);
    }

    @Override
    public ArrayList<Reserva> buscarReservasPorBem(int idBem) {
        ArrayList<Reserva> resultado = new ArrayList<>();
        for (Reserva reservaBem : this.reservas) {
            if (reservaBem.getId() == idBem) {
                resultado.add(reservaBem);
            }
        }
        return new ArrayList<>(resultado);
    }


    @Override
    public ArrayList<Reserva> listarReservas() {
        return new ArrayList<>(reservas);
    }


    @Override
    public void atualizarReserva(Reserva reservaAtualizada) {
        for (int i = 0; i < reservas.size(); i++) {
            if (reservas.get(i).getId() == reservaAtualizada.getId()) {
                reservas.set(i, reservaAtualizada);
            }
        }
    }

}
