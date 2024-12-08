package timeShare;

import java.time.LocalDateTime;

public class Cota {
    private int id;
    private int quantidadeCotas;
    private String periodo;
    private LocalDateTime ano;
    private double preco;
    private boolean status;

    public Cota(int id, int quantidadeCotas, double preco) {
        this.id = id;
        this.quantidadeCotas = quantidadeCotas;
        this.preco = preco;
        this.ano = LocalDateTime.now();
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

    public int getNumero() {
        return this.quantidadeCotas;
    }

    public void setNumero(int numero) {
        this.quantidadeCotas = numero;
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

    public String calcularDeslocamento(int anoAtual) {
        return "bilubili";
    }
}