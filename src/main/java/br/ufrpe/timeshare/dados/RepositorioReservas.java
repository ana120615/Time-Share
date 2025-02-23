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
    public boolean verificarConflitoNaReserva(long idBem, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        boolean conflito = false;
        for (Reserva reservaAtual : buscarReservasPorBem(idBem)) {
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

    @Override
    public List<Reserva> buscarReservasPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Reserva> resultado = new ArrayList<>();
    
        for (Reserva reserva : lista) {
            LocalDateTime inicioReserva = reserva.getDataInicio();
            LocalDateTime fimReserva = reserva.getDataFim();
    
            // Verifica se a reserva está dentro do período ou se coincide com as datas fornecidas
            if ((inicioReserva.isAfter(dataInicio) && inicioReserva.isBefore(dataFim)) || 
                (fimReserva.isAfter(dataInicio) && fimReserva.isBefore(dataFim)) || 
                inicioReserva.isEqual(dataInicio) || 
                fimReserva.isEqual(dataFim)) {
                resultado.add(reserva);
            }
        }
    
        return resultado;
    }
    

}





