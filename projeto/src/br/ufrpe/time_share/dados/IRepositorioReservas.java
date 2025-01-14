package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Reserva;

import java.util.ArrayList;

public interface IRepositorioReservas {

    void cadastrarReserva(Reserva reserva);

    boolean removerReserva( int idReserva);

    void atualizarReserva(Reserva reservaAtualizada);

    Reserva buscarReserva(Reserva reserva);

    ArrayList<Reserva> buscarReservasPorUsuario(int idUsuario);

    ArrayList<Reserva> buscarReservasPorBem(int idBem);

    Reserva buscarReservasPorId(int idReserva);

    ArrayList<Reserva> listarReservas();

}
