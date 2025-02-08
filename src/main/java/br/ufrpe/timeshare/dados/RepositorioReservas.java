package br.ufrpe.timeshare.dados;

import br.ufrpe.timeshare.negocio.beans.Reserva;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RepositorioReservas extends RepositorioGenerico<Reserva> implements IRepositorioReservas {

    // INSTANCIA UNICA DO REPOSITORIO
    private static volatile RepositorioReservas instancia;

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
    public boolean verificarConflitoNaReserva(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        boolean conflito = false;
        for (Reserva reservaAtual : lista) {

            if (reservaAtual.getDataInicio().isBefore(dataInicial) && reservaAtual.getDataFim().isAfter(dataInicial)) {
                conflito = true;
            } else if (reservaAtual.getDataInicio().isBefore(dataFinal) && reservaAtual.getDataFim().isAfter(dataFinal)) {
                conflito = true;
            } else if (reservaAtual.getDataInicio().isAfter(dataInicial) && reservaAtual.getDataFim().isBefore(dataFinal)) {
                conflito = true;
            } else if (reservaAtual.getDataInicio().isEqual(dataInicial) || reservaAtual.getDataFim().isEqual(dataFinal)) {
                conflito = true;
            }
        }
        return conflito;
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





