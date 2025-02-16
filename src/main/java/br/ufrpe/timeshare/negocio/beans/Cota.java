package br.ufrpe.timeshare.negocio.beans;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Cota extends Entidade implements Cloneable, Comparable<Cota> {
    private int id;
    private double preco;
    private Usuario proprietario;
    private boolean statusDeDisponibilidadeParaCompra;
    private boolean statusDeDisponibilidadeParaReserva;
    private Bem bemAssociado;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;


    // CONSTRUTOR
    public Cota(int id, LocalDateTime dataInicio, LocalDateTime dataFim, double preco, Bem bemAssociado) {
        this.setId(id);
        this.setDataInicio(dataInicio);
        this.setDataFim(dataFim);
        this.setPreco(preco);
        this.bemAssociado = bemAssociado;
        this.setStatusDeDisponibilidadeParaCompra(bemAssociado.isOfertado()); //inicializado como disponivel
        this.statusDeDisponibilidadeParaReserva = true; //inicializado como disponivel
    }

    // METODOS GET E SET

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


    public long getId() {
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

    public boolean getStatusDeDisponibilidadeParaReserva() {
        return this.statusDeDisponibilidadeParaReserva;
    }

    public void setStatusDeDisponibilidadeParaReserva(boolean statusDeDisponibilidadeParaReserva) {
        if (proprietario == null) {
            this.statusDeDisponibilidadeParaReserva = bemAssociado.isOfertado();
        } else {
            this.statusDeDisponibilidadeParaReserva = statusDeDisponibilidadeParaReserva;
        }
    }

    @Override
    public String toString() {
        return "Cota{" +
                "id=" + id +
                ", dataInicio=" + dataInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ", dataFim=" + dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ", preco=" + preco +
                ", proprietarioCPF=" + (proprietario != null ? proprietario.getId() : "null") +
                ", statusDeDisponibilidadeParaCompra=" + statusDeDisponibilidadeParaCompra +
                ", bemAssociado=" + (bemAssociado != null ? bemAssociado.getId() : "null") +
                '}';
    }

    @Override
    public Cota clone() {
        try {
            return (Cota) super.clone(); // Clonagem superficial (ok para Cota simples)
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Falha ao clonar Cota", e);
        }
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

    @Override
    public int compareTo(Cota cota) {
        return this.dataInicio.compareTo(cota.getDataInicio());
    }

}
