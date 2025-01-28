package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reserva {
    private int id;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Usuario usuarioComum;
    private Bem bem;
    private boolean cancelada;

    public Reserva(int id, LocalDateTime dataInicio, LocalDateTime dataFim, Usuario usuarioComum, Bem bem) {
        this.id = id;
        this.cancelada = false;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.usuarioComum = usuarioComum;
        this.bem = bem;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    public void setCancelada(boolean cancelada) {
        this.cancelada = cancelada;
    }

    public void cancelarReserva() {
        this.cancelada = true;
    }

    public Bem getBem() {
        return bem;
    }

    public void setBem(Bem bem) {
        this.bem = bem;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Usuario getUsuarioComum() {
        return usuarioComum;
    }


    public void setUsuarioComum(Usuario usuarioComum) {
        this.usuarioComum = usuarioComum;
    }

    @Override
    public String toString() {
        return "Reserva: " +
                "id=" + id +
                ", dataInicio=" + dataInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+
                ", dataFim=" + dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ", usuarioComum=" + usuarioComum +
                ", bem=" + bem.getId()+ " " + bem.getNome()+
                ", cancelada="+ cancelada;
    }

    @Override
    public boolean equals(Object object) {
        boolean resultado = false;
        if (object instanceof Reserva) {
            Reserva reserva = (Reserva) object;
            resultado = this.id == reserva.id && this.dataInicio.equals(reserva.dataInicio)
                    && this.dataFim.equals(reserva.dataFim);
        }

        return resultado;
    }


}
