package timeShare;

import java.time.LocalDateTime;

public class Cota {
    private int id;
    private int quantidadeCotas;
    private String periodo;
    private LocalDateTime ano;
    private double preco;
    private boolean status;
    private Bem bem;

    public Cota(int id, int quantidadeCotas, double preco, Bem bem) {
        this.id = id;
        this.quantidadeCotas = quantidadeCotas;
        this.preco = preco;
        setBem(bem);
        this.status = true; //inicializada como disponivel
    }

    //Métodos get e set
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

    public int getQuantidadeCotas() {
        return this.quantidadeCotas;
    }

    public void setQuantidadeCotas(int quantidadeCotas) {
        this.quantidadeCotas = quantidadeCotas;
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

    public Bem getBem() {
        return bem;
    }

    public void setBem(Bem bem) {
        if(bem == null) {
            throw new IllegalArgumentException();
        }
        this.bem = bem;
    }

    public String calcularDeslocamento(int anoAtual) {
        return "Colocar depois";
    }
}