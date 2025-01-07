package br.ufrpe.time_share.negocio.beans;

import java.util.ArrayList;

public class Bem {
    private int id;
    private String nome;
    private String descricao;
    private String localizacao;
    private int capacidade;
    private ArrayList<Cota> cotas;
    private Administrador adm;

    public Bem(String nome, String descricao, String localizacao, int capacidade, Administrador adm) {
        setNome(nome);
        setDescricao(descricao);
        setLocalizacao(localizacao);
        setCapacidade(capacidade);
        setAdm(adm);
    }

    //Métodos get e set
    public Administrador getAdm() {
        return adm;
    }

    public void setAdm(Administrador adm) {
        if(adm == null) {
            throw new IllegalArgumentException();
        }
        this.adm = adm;
    }

    public Cota getCota(int id) {
        Cota cota = null;
        for (Cota c : cotas) {
            if (c.getId() == id) {
                cota = c;
            }
        }

        return cota;
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        if(id < 0) {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if(nome == null) {
            throw new IllegalArgumentException();
        }
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        if(descricao == null) {
            throw new IllegalArgumentException();
        }
        this.descricao = descricao;
    }

    public String getLocalizacao() {
        return this.localizacao;
    }

    public void setLocalizacao(String localizacao) {
        if(localizacao == null) {
            throw new IllegalArgumentException();
        }
        this.localizacao = localizacao;
    }

    public int getCapacidade() {
        return this.capacidade;
    }

    public void setCapacidade(int capacidade) {
        if(capacidade < 1) {
            throw new IllegalArgumentException();
        }
        this.capacidade = capacidade;
    }


    //Outros métodos
    public void criarCotas(int id, int quantidadeCotas, double preco, String descricao, String periodo) {
        Cota cota = new Cota (id, quantidadeCotas, preco, descricao, periodo, this);
        this.cotas.add(cota);
    }

    public String toString() {
        String resultado = "\n------------------------\n";
        resultado += String.format("ID: %d\nNome: %s\n" +
                "Descrição: %s\nLocalização: %s\n" +
                "Capacidade: %d", getId(), getNome(), getDescricao(), getLocalizacao(), getCapacidade());
        return resultado;
    }



}