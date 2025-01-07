package negocio.beans;

import java.time.LocalDateTime;

public class Cota {
    private int id;
    private String periodo;
    private String descricao;
    private LocalDateTime ano;
    private double preco;
    private boolean status;

    public Cota(int id, double preco, String descricao, String periodo) {
        setId(id);
        setPreco(preco);
        setDescricao(descricao);
        setPeriodo(periodo);
        this.status = true; //inicializado como disponivel
    }


    //Métodos get e set
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getPreco() {
        return this.preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getPeriodo() {
        return this.periodo;
    }

    public void setPeriodo(String periodo) {
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
    
    //Outros metodos
    public String calcularDeslocamento(int anoAtual) {
        return "Colocar depois";
    }
}
