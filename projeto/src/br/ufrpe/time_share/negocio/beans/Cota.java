package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Cota {
    private int id;
    private LocalDate data;
    private double preco;
    private UsuarioComum proprietario;
    private boolean statusDeDisponibilidadeParaCompra;
    private boolean statusDeDisponibilidadeParaReserva;
    private Bem bemAssociado;

    //CONSTRUTOR
    public Cota(int id, LocalDate data, double preco) {
        this.setId(id);
        setData(data);
        this.setPreco(preco);
        this.statusDeDisponibilidadeParaCompra = true; //inicializado como disponivel
        this.statusDeDisponibilidadeParaReserva = true; //inicializado como disponivel
    }

    //METODOS GET E SET

    public LocalDate getData() {
        return data;
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

    public LocalDate getdata() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
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
                ", data=" + data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
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
            return id == cota.getId() && data.equals(cota.getData());
        }
        return false;
    }

}
