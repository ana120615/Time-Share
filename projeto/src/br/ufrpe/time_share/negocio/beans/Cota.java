package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Cota {
    private int id;
    private double preco;
    private Usuario proprietario;
    private boolean statusDeDisponibilidadeParaCompra;
    private boolean statusDeDisponibilidadeParaReserva;
    private Bem bemAssociado;
    private LocalDate dataInicio;
    private LocalDate dataFim;


    // CONSTRUTOR
    public Cota(int id, LocalDate dataInicio, LocalDate dataFim, double preco, Bem bemAssociado) {
        this.setId(id);
        this.setDataInicio(dataInicio);
        this.setDataFim(dataFim);
        this.setPreco(preco);
        this.bemAssociado = bemAssociado;
        this.setStatusDeDisponibilidadeParaCompra(bemAssociado.isOfertado()); //inicializado como disponivel
        this.statusDeDisponibilidadeParaReserva = true; //inicializado como disponivel
    }

    // METODOS GET E SET

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

    private int calculoDoPeriodoDeDiasEntreAsDatas(){
        return  (int) this.dataInicio.until(dataFim, ChronoUnit.DAYS);
    }

    public Bem getBemAssociado() {
        return bemAssociado;
    }

    public Usuario getProprietario() {
        return this.proprietario;
    }

    public void setProprietario(Usuario proprietario) {
        this.proprietario = proprietario;
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getStatusDeDisponibilidadeParaCompra() {
        return this.statusDeDisponibilidadeParaCompra;
    }

    public void setStatusDeDisponibilidadeParaCompra(boolean statusDeDisponibilidadeParaCompra) {
        this.statusDeDisponibilidadeParaCompra = statusDeDisponibilidadeParaCompra;
    }

    public double getPreco() {
        return this.preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public boolean isStatusDeDisponibilidadeParaCompra() {
        return statusDeDisponibilidadeParaCompra;
    }

    public boolean isStatusDeDisponibilidadeParaReserva() {
        return this.statusDeDisponibilidadeParaReserva;
    }

    public void setStatusDeDisponibilidadeParaReserva(boolean statusDeDisponibilidadeParaReserva) {
        this.statusDeDisponibilidadeParaReserva = statusDeDisponibilidadeParaReserva;
    }

    @Override
    public String toString() {
        return "Cota{" +
                "id=" + id +
                ", dataInicio=" + dataInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ", dataFim=" + dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ", preco=" + preco +
                ", proprietarioCPF=" + (proprietario != null ? proprietario.getCpf() : "null") +
                ", statusDeDisponibilidadeParaCompra=" + statusDeDisponibilidadeParaCompra +
                ", bemAssociado=" + (bemAssociado != null ? bemAssociado.getId() : "null") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Cota) {
            Cota cota = (Cota) o;
            return id == cota.getId() && dataInicio.equals(cota.getDataInicio()) &&
                    dataFim.equals(cota.getDataFim()) && bemAssociado.equals(cota.getBemAssociado());
        }
        return false;
    }

}
