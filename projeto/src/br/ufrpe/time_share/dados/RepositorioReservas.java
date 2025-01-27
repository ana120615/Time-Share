package br.ufrpe.time_share.dados;

import java.util.ArrayList;

import br.ufrpe.time_share.negocio.beans.Reserva;

public class RepositorioReservas extends RepositorioGenerico<Reserva> implements IRepositorioReservas {

    // INSTANCIA UNICA DO REPOSITORIO
    private static RepositorioReservas instancia;

    public RepositorioReservas() {
        super();
    }

    // METODO PARA OBTER A INSTANCIA DO REPOSITORIO
    public static RepositorioReservas getInstancia() {
        if (instancia == null) {
            synchronized (RepositorioReservas.class) { // Thread-safe
                if (instancia == null) {
                    instancia = new RepositorioReservas();
                }
            }
        }
        return instancia;
    }


    @Override
    public Reserva buscarReserva(Reserva reserva) {
        Reserva resultado = null;
        boolean reservaExiste = false;
        int i;
        for (i = 0; i < lista.size() && !reservaExiste; i++) {
            if (lista.get(i).equals(reserva)) {
                reservaExiste = true;
            }
        }
        if (reservaExiste) {
            resultado = lista.get(i - 1);
        }
        return resultado;
    }


    @Override
    public ArrayList<Reserva> buscarReservasPorUsuario(int idUsuario) {
        ArrayList<Reserva> resultado = new ArrayList<>();
        for (Reserva reservaUser : lista) {
            if (reservaUser.getId() == idUsuario) {
                resultado.add(reservaUser);
            }
        }
        return new ArrayList<>(resultado);
    }

    @Override
    public ArrayList<Reserva> buscarReservasPorBem(int idBem) {
        ArrayList<Reserva> resultado = new ArrayList<>();
        for (Reserva reservaBem : lista) {
            if (reservaBem.getId() == idBem) {
                resultado.add(reservaBem);
            }
        }
        return new ArrayList<>(resultado);
    }


}
