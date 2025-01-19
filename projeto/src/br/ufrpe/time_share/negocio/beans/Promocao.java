package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;

public class Promocao {

    private double taxaDesconto;

    //CONSTRUTOR
    public Promocao() {
        this.taxaDesconto = 0.00;
    }

    //METODOS GET E SET
    public void setTaxaDesconto(double taxaDesconto) {
        this.taxaDesconto = taxaDesconto;
    }

    public double getTaxaPromocaoAniversario() {
        return 0.15d;
    }

    public double getTaxaPromocaoTemporada(LocalDate data) {
        if (eAltaTemporada(data)) {
            return 0.25d;
        } else {
            return 0.05d;
        }
    }

    public double calcularTaxaPromocao(LocalDate dataInicio, Usuario usuario) {
        this.taxaDesconto = 0.0;
        if (dataInicio != null) {
            this.taxaDesconto += calcularTaxaTemporada(dataInicio);
        }
        if (usuario != null) {
            this.taxaDesconto += calcularTaxaAniversario(usuario);
        }
        return taxaDesconto;
    }


    // PROMOCAO COM BASE NO ANIVERSARIO
    private double calcularTaxaAniversario(Usuario usuario) {
        double taxaAniversario = 0;
        if (usuario.verificarAniversario()) {
            taxaAniversario = 0.15d;
        }
        return taxaAniversario;
    }

    //LOGICA DE TEMPORADA E TAXA:
    private double calcularTaxaTemporada(LocalDate dataInicio) {
        double valorTaxa = 0;
        if (eAltaTemporada(dataInicio)) {
            valorTaxa = 0.25d;
        } else {
            valorTaxa = 0.05d;
        }
        return valorTaxa;
    }

    private boolean eAltaTemporada(LocalDate data) {
        int mes = data.getMonthValue();
        return mes == 12 || mes == 1 || mes == 2;
    }
}
