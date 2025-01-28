package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Reserva;
import br.ufrpe.time_share.negocio.beans.Usuario;

import java.util.ArrayList;

public interface IRepositorioReservas {

    void cadastrarReserva(Reserva reserva);

    void removerReserva(Reserva reserva);

    void atualizarReserva(Reserva reservaAtualizada);

    Reserva buscarReserva(Reserva reserva);

    ArrayList<Reserva> buscarReservasPorUsuario(Usuario usuario);

    ArrayList<Reserva> buscarReservasPorBem(int idBem);

    Reserva buscarReservasPorId(int idReserva);

    ArrayList<Reserva> listarReservas();

}
