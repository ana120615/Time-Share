package br.ufrpe.timeshare.negocio.beans;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Bem extends Entidade implements Cloneable, Comparable<Bem> {
    private int id;
    private String nome;
    private String descricao;
    private String localizacao;
    private int capacidade;
    private boolean ofertado;
    private ArrayList<Cota> cotas;
    private Usuario cadastradoPor;
    private LocalDateTime diaInicial;
    private String caminhoImagem;

    {
        this.cotas = new ArrayList<>(10);
    }

    //CONSTRUTOR
    public Bem(int id, String nome, String descricao, String localizacao, int capacidade, Usuario cadastradoPor, String caminhoImagem) {
        setId(id);
        setCapacidade(capacidade);
        setDescricao(descricao);
        setLocalizacao(localizacao);
        setNome(nome);
        setCadastradoPor(cadastradoPor);
        this.ofertado = false;
        setCaminhoImagem(caminhoImagem);
    }

    //METODOS GET E SET
    public LocalDateTime getDiaInicial() {
        return diaInicial;
    }

    public void setDiaInicial(LocalDateTime diaInicial) {
        this.diaInicial = diaInicial;
    }
    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }
    public long getId() {
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

    public ArrayList<Cota> getCotas() {
        return cotas;
    }

    public void setCotas(ArrayList<Cota> cotas) {
        this.cotas = cotas;
    }

    public Usuario getCadastradoPor() {
        return cadastradoPor;
    }

    public void setCadastradoPor(Usuario cadastradoPor) {
        this.cadastradoPor = cadastradoPor;
    }

    @Override
    public Bem clone() {
        try {
            // Chama o clone() da classe Object (que faz uma cópia superficial)
            Bem cloneBem = (Bem) super.clone();

            // Clonando as cotas (profundamente, para não compartilhar a mesma lista)
            cloneBem.setCotas(new ArrayList<>());
            for (Cota cota : this.cotas) {
                cloneBem.getCotas().add(cota.clone());
            }
            return cloneBem;
        } catch (CloneNotSupportedException e) {
            // throw new RuntimeException("Falha ao clonar Bem", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "\nBem{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", localizacao='" + localizacao + '\'' +
                ", capacidade=" + capacidade +
                ", ofertado=" + ofertado +
                ", cotas=" + cotas +
                '}' + "\n";
    }

    private int getQuantidadeCotasVendidas() {
        int quantidadeCotas = 0;
        if (this.ofertado && this.cotas != null) {
            for (Cota cota : this.cotas) {
                if (!cota.getStatusDeDisponibilidadeParaCompra()) {
                    quantidadeCotas++;
                }
            }
        }
        return quantidadeCotas;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Bem) {
            Bem bem = (Bem) obj;
            return this.id == bem.getId() && this.nome.equals(bem.getNome())
                    && this.descricao.equals(bem.getDescricao())
                    && this.localizacao.equals(bem.getLocalizacao());
        }
        return false;
    }

    @Override
    public int compareTo(Bem o) {
        return Integer.compare(o.getQuantidadeCotasVendidas(), this.getQuantidadeCotasVendidas());
    }

}
