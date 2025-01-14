package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;

public class Promocao {


    private double taxa;

    //CONSTRUTOR
    public Promocao() {
        this.taxa = 0.00;
    }

    //METODOS GET E SET
    public void setTaxa(double taxa) {
        this.taxa = taxa;
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
        this.taxa = 0.0;
        if (dataInicio != null) {
            this.taxa += calcularTaxaTemporada(dataInicio);
        }
        if (usuario != null) {
            this.taxa += calcularTaxaAniversario(usuario);
        }
        return taxa;
    }


    // PROMOCAO COM BASE NO ANIVERSARIO
    public double calcularTaxaAniversario(Usuario usuario) {
        double taxaAniversario = 0;
        if (usuario.verificarAniversario()) {
            taxaAniversario = 0.15d;
        }
        return taxaAniversario;
    }

    //LOGICA DE TEMPORADA E TAXA:
    public double calcularTaxaTemporada(LocalDate dataInicio) {
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
