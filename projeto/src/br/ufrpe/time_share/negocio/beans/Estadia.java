package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Estadia extends Entidade {
    private int id;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Reserva reserva;

    public Estadia(int id, Reserva reserva) {
        this.reserva = reserva;
        this.id = id;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public long getId() {
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
      return (int) getDataInicio().until(getDataFim(), ChronoUnit.DAYS);
    }

    @Override
    public String toString() {
        return "Estadia: " +
                "id=" + id +
                ", dataInicio=" + dataInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ", dataFim=" + dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ", bem=" + reserva.getBem().getNome() +
                ", usuario=" + reserva.getUsuarioComum().getNome();      
    }
}
