package br.ufrpe.timeshare.dados;

import br.ufrpe.timeshare.negocio.beans.Reserva;

import java.time.LocalDateTime;
import java.util.List;

public interface IRepositorioReservas extends IRepositorio<Reserva> {

    Reserva buscarReserva(Reserva reserva);

    List<Reserva> buscarReservasPorUsuario(long idUsuario);

    List<Reserva> buscarReservasPorBem(long idBem);

    boolean verificarConflitoNaReserva(long idBem, LocalDateTime dataInicial, LocalDateTime dataFinal);
}
