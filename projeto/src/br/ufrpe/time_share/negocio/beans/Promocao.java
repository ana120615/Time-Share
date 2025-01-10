package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;

public class Promocao {
//Aplicando 15% de desconto
    public double aplicarPromocao(double valorTotal) {
        valorTotal-=0.15*valorTotal;
        return valorTotal;
    }
//VERIFICACAO DE VALIDADE DA PROMOCAO COM BASE NO ANIVERSARIO
    public boolean verificarValidade(Usuario usuario) {
        boolean resultado = false;
    if(usuario.verificarAniversario()){
        resultado=true;
    }
        return resultado;
    }

    // Lógica de temporada e taxa:
      //public double calcularTaxaTemporada(boolean foraPeriodo) {
       // if (eAltaTemporada(dataInicio)) {
        //   this.taxa = 200.00d;
        //} else {
          //  this.taxa = 75.00d;
        //}
        //return this.taxa;
    //}

    //private boolean eAltaTemporada(LocalDate data) {
     //   int mes = data.getMonthValue();
       // return mes == 12 || mes == 1 || mes == 2;
   // }
}
