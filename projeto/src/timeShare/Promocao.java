package timeShare;

import java.time.LocalDateTime;

public class Promocao {

    private double taxaDesconto;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;


    public double aplicarTaxa(Usuario usuario, double valorTotal) {
        double resultado = 0;
        /*

        IMPLEMENTAR LÓGICA DO RETORNO DA TAXA APLICADA COM BASE NAS INFORMAÇÕES DO USUÁRIO E
        VALOR TOTAL

        */
        return resultado;
    }

    public boolean verificarValidade() {
        boolean resultado = false;
        /*

        IMPLEMENTAR A VERIFICAÇÃO DA VALIDADE DA PROMOÇÃO

        */
        return resultado;
    }
}
