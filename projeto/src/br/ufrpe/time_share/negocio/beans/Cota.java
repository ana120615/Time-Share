package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Cota {
    private int id;
    private double preco;
    private UsuarioComum proprietario;
    private boolean statusDeDisponibilidadeParaCompra;
    private boolean statusDeDisponibilidadeParaReserva;
    private Bem bemAssociado;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private int quantidadeDiasDeReserva;

    //CONSTRUTOR
    public Cota(int id, LocalDate dataInicio, LocalDate dataFim, double preco) {
        this.setId(id);
        this.setDataInicio(dataInicio);
        this.setDataFim(dataFim);
        this.setPreco(preco);
        calculoQuantidadeDeDias();
        this.statusDeDisponibilidadeParaCompra = true; //inicializado como disponivel
        this.statusDeDisponibilidadeParaReserva = true; //inicializado como disponivel
    }

    //METODOS GET E SET


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

    public int getQuantidadeDiasDeReserva() {
        return quantidadeDiasDeReserva;
    }

    private void calculoQuantidadeDeDias (){
        this.quantidadeDiasDeReserva = (int) this.dataInicio.until(dataFim, ChronoUnit.DAYS);
    }

    public Bem getBemAssociado() {
        return bemAssociado;
    }

    public void setBemAssociado(Bem bemAssociado) {
        if (this.bemAssociado != bemAssociado) {
            this.bemAssociado = bemAssociado;
            if (bemAssociado != null && !bemAssociado.getCotas().contains(this)) {
                bemAssociado.adicionarCota(this);
            }
        }
    }

    public Usuario getProprietario() {
        return this.proprietario;
    }

    public void setProprietario(UsuarioComum proprietario) {
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
