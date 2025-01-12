package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Estadia {
    private int id;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Reserva reserva;

    public Estadia(int id, Reserva reserva) {
        this.reserva = reserva;
        this.id = id;
        setDataInicio(reserva.getDataInicio());
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    //CALCULAR DURACAO DA ESTADIA
    public int calcularDuracao () {
      return (int) ChronoUnit.DAYS.between (reserva.getDataInicio(),reserva.getDataFim());
    }

    @Override
    public String toString() {
        return "Estadia{" +
                "id=" + id +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", reserva=" + reserva +
                '}';
    }
}
