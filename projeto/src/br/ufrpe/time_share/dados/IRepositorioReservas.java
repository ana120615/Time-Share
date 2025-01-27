package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Reserva;

import java.util.ArrayList;

public interface IRepositorioReservas extends IRepositorio<Reserva> {

    void cadastrarReserva(Reserva reserva);

    void removerReserva(Reserva reserva);

    Reserva buscarReserva(Reserva reserva);

    ArrayList<Reserva> buscarReservasPorUsuario(int idUsuario);

    ArrayList<Reserva> buscarReservasPorBem(int idBem);

    Reserva buscarReservasPorId(int idReserva);

    ArrayList<Reserva> listarReservas();

}
