package br.ufrpe.timeshare.dados;

import br.ufrpe.timeshare.negocio.beans.Reserva;

import java.time.LocalDate;
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
    public boolean verificarConflitoNaReserva(long idBem, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        boolean conflito = false;

        LocalDate dataInicioAtual = dataInicial.toLocalDate();
        LocalDate dataFinalAtual = dataFinal.toLocalDate();

        for (Reserva reservaAtual : buscarReservasPorBem(idBem)) {
            LocalDate reservaDataInicial = reservaAtual.getDataInicio().toLocalDate();
            LocalDate reservaDataFinal = reservaAtual.getDataFim().toLocalDate();

            if (reservaDataInicial.isBefore(dataInicioAtual) && reservaDataFinal.isAfter(dataFinalAtual)) {
                conflito = true;
            } else if (reservaDataInicial.isBefore(dataFinalAtual) && reservaDataFinal.isAfter(dataInicioAtual)) {
                conflito = true;
            } else if (reservaDataInicial.isAfter(dataInicioAtual) && reservaDataFinal.isBefore(dataFinalAtual)) {
                conflito = true;
            } else if (reservaDataInicial.isEqual(dataInicioAtual) || reservaDataFinal.isEqual(dataFinalAtual)) {
                conflito = true;
            }
        }
        return conflito;
    }

    @Override
    public List<Reserva> buscarReservasPorUsuario(long idUsuario) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva reservaUser : lista) {
            if (reservaUser.getUsuarioComum().getId() == idUsuario) {
                resultado.add(reservaUser);
            }
        }
        return resultado;
    }

    @Override
    public List<Reserva> buscarReservasPorBem(long idBem) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva reservaBem : lista) {
            if (reservaBem.getBem().getId() == idBem) {
                resultado.add(reservaBem);
            }
        }
        return resultado;
    }  

}





