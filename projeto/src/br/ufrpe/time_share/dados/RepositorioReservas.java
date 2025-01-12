package br.ufrpe.time_share.dados;

import java.time.LocalDate;
import java.util.ArrayList;

import br.ufrpe.time_share.negocio.beans.Reserva;

public class RepositorioReservas implements IRepositorioReservas {

    private ArrayList<Reserva> reservas;

    {
        this.reservas = new ArrayList<>();
    }

    @Override
    public void cadastrarReserva(Reserva reserva) {
        this.reservas.add(reserva);
    }

    @Override
    public boolean removerReserva(int idReserva) {
        boolean resultado = false;

        boolean reservaExiste = false;
        int i;
        for (i = 0; i < this.reservas.size() && !reservaExiste; i++) {
            if (reservas.get(i).getId() == idReserva) {
                reservaExiste = true;
            }
        }
        if (reservaExiste) {
            this.reservas.remove(i - 1);
            resultado = true;
        }
        return resultado;

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
        Reserva resultado = null;
        boolean reservaExiste = false;
        int i;
        for (i = 0; i < this.reservas.size() && !reservaExiste; i++) {
            if (this.reservas.get(i).getId() == idReserva) {
                reservaExiste = true;
            }
        }
        if (reservaExiste) {
            resultado = this.reservas.get(i - 1);
        }
        return resultado;
    }

    @Override
    public ArrayList<Reserva> buscarReservasPorUsuario(int idUsuario) {
        ArrayList<Reserva> resultado = new ArrayList<>();
        for (Reserva reservaUser : this.reservas) {
            if (reservaUser.getId() == idUsuario) {
                resultado.add(reservaUser);
            }
        }
        return resultado;
    }

    @Override
    public ArrayList<Reserva> buscarReservasPorBem(int idBem) {
        ArrayList<Reserva> resultado = new ArrayList<>();
        for (Reserva reservaBem : this.reservas) {
            if (reservaBem.getId() == idBem) {
                resultado.add(reservaBem);
            }
        }
        return resultado;
    }


    @Override
    public ArrayList<Reserva> listarReservas() {
        return this.reservas;
    }


    @Override
    public Reserva atualizarReserva(int idReserva, LocalDate novaDataInicio, LocalDate novaDataFim) {
        // Depende de Controladores
        return null;
    }

}
