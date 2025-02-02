package br.ufrpe.timeshare.dados;

import java.util.ArrayList;
import java.util.List;

import br.ufrpe.timeshare.negocio.beans.Reserva;

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
    public List<Reserva> buscarReservasPorUsuario(long idUsuario) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva reservaUser : lista) {
            if (reservaUser.getId() == idUsuario) {
                resultado.add(reservaUser);
            }
        }
        return resultado;
    }

    @Override
    public List<Reserva> buscarReservasPorBem(long idBem) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva reservaBem : lista) {
            if (reservaBem.getId() == idBem) {
                resultado.add(reservaBem);
            }
        }
        return resultado;
    }
}





