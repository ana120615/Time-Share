package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;

public class Reserva {
    private int id;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean status;
    private double taxa;
    private Usuario usuario;
    private Bem bem;

    public Reserva(int id, LocalDate dataInicio, LocalDate dataFim, Usuario usuario, Bem bem) {
        this.id = id;
        this.status = true;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.taxa = 0.00d;
        this.usuario = usuario;
        this.bem = bem;
    }

    public double calcularTaxa(boolean foraPeriodo) {
        if (eAltaTemporada(dataInicio)) {
            this.taxa = 200.00d;
        } else {
            this.taxa = 75.00d;
        }
        return this.taxa;
    }

    private boolean eAltaTemporada(LocalDate data) {
        int mes = data.getMonthValue();
        return mes == 12 || mes == 1 || mes == 2;
    }

    public void cancelarReserva() {
        this.status = false;
    }

    public Bem getBem() {
        return bem;
    }

    public void setBem(Bem bem) {
        this.bem = bem;
    }

    public double getTaxa() {
        return taxa;
    }

    public void setTaxa(double taxa) {
        this.taxa = taxa;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return this.status;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
