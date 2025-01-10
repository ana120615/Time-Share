package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;

public class Cota {
    private int id;
    private LocalDate data;
    private double preco;
    private UsuarioComum proprietario;
    private boolean statusDeDisponibilidadeParaCompra;
    private Bem bemAssociado;

    public Cota(int id, LocalDate data, double preco, Bem bemAssociado) {
        this.setId(id);
        this.setPreco(preco);
        this.setBemAssociado(bemAssociado);
        this.statusDeDisponibilidadeParaCompra = true; //inicializado como disponivel
    }

    //Métodos get e set

    public LocalDate getData() {
        return data;
    }

    public Bem getBemAssociado() {
        return bemAssociado;
    }

    public void setBemAssociado(Bem bemAssociado) {
        if (bemAssociado != null) {
            this.bemAssociado = bemAssociado;
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

    public boolean getStatus() {
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


    @Override
    public String toString() {
        return "Cota{" +
                "id=" + id +
                ", data=" + data +
                ", preco=" + preco +
                ", proprietario=" + proprietario +
                ", statusDeDisponibilidadeParaCompra=" + statusDeDisponibilidadeParaCompra +
                ", bemAssociado=" + bemAssociado +
                '}';
    }
}
