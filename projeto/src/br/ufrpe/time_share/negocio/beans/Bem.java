package br.ufrpe.time_share.negocio.beans;

import java.util.ArrayList;
import java.util.List;

public class Bem {
    private int id;
    private String nome;
    private String descricao;
    private String localizacao;
    private int capacidade;
    private boolean ofertado;
    private ArrayList<Cota> cotas;

    public Bem(int id, String nome, String descricao, String localizacao, int capacidade) {
        setId(id);
        setCapacidade(capacidade);
        setDescricao(descricao);
        setLocalizacao(localizacao);
        setNome(nome);
        this.ofertado = false;

    }

    //METODOS GET E SET


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocalizacao() {
        return this.localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public int getCapacidade() {
        return this.capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public boolean isOfertado() {
        return ofertado;
    }

    public void setOfertado(boolean ofertado) {
        this.ofertado = ofertado;
    }

    public List<Cota> getCotas() {
        return cotas;
    }

    public void criarCotas(ArrayList<Cota> cotas) {
        this.cotas = cotas;
    }

    public void adicionarCota(Cota cota) {
        cotas.add(cota);
    }


    @Override
    public String toString() {
        return "Bem{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", localizacao='" + localizacao + '\'' +
                ", capacidade=" + capacidade +
                ", ofertado=" + ofertado +
                ", cotas=" + cotas +
                '}';
    }
}
