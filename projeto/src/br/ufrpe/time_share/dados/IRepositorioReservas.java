package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Reserva;

import java.util.ArrayList;
import java.util.List;

public interface IRepositorioReservas extends IRepositorio<Reserva> {

    Reserva buscarReserva(Reserva reserva);

    List<Reserva> buscarReservasPorUsuario(long idUsuario);

    List<Reserva> buscarReservasPorBem(long idBem);


}
