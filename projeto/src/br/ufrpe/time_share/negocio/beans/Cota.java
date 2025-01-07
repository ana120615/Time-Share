package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDateTime;

public class Cota {
    private int id;
    private int quantidadeCotas;
    private String periodo;
    private String descricao;
    private LocalDateTime ano;
    private double preco;
    private boolean status;
    private Bem bem;

    public Cota(int id, int quantidadeCotas, double preco, String descricao, String periodo, Bem bem) {
        setId(id);
        setQuantidadeCotas(quantidadeCotas);
        setPreco(preco);
        setBem(bem);
        setDescricao(descricao);
        setPeriodo(periodo);
        this.status = true; //inicializado como disponivel
    }


    //Métodos get e set
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao == null) {
            throw new NullPointerException();
        }
        this.descricao = descricao;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getQuantidadeCotas() {
        return this.quantidadeCotas;
    }

    public void setQuantidadeCotas(int quantidadeCotas) {
        if (quantidadeCotas < 1) {
            throw new IllegalArgumentException();
        }
        this.quantidadeCotas = quantidadeCotas;
    }

    public double getPreco() {
        return this.preco;
    }

    public void setPreco(double preco) {
        if (preco < 0.0) {
            throw new IllegalArgumentException();
        }
        this.preco = preco;
    }

    public String getPeriodo() {
        return this.periodo;
    }

    public void setPeriodo(String periodo) {
        if (periodo == null) {
            throw new IllegalArgumentException();
        }
        this.periodo = periodo;
    }

    public LocalDateTime getAno() {
        return ano;
    }

    public void setAno(LocalDateTime ano) {
        this.ano = ano;
    }

    public boolean isStatus() {
        return status;
    }

    public Bem getBem() {
        return bem;
    }

    public void setBem(Bem bem) {
        if (bem == null) {
            throw new NullPointerException();
        }
        this.bem = bem;
    }


    //Outros metodos
    public String calcularDeslocamento(int anoAtual) {
        return "Colocar depois";
    }
}