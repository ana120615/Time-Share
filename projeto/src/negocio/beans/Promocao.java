package negocio.beans;

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
}
