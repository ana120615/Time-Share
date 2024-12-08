package timeShare;
import java.time.LocalDateTime;

public class Promocao {
    private double taxaDesconto;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    //Métodos get e set
public void setTaxaDesconto(double taxaDesconto){
    this.taxaDesconto=taxaDesconto;
}
public double getTaxaDesconto(){
    return this.taxaDesconto;
}
public void setDataInicio(LocalDateTime dataInicio){
    this.dataInicio=dataInicio;
}
public LocalDateTime getDataInicio(LocalDateTime dataInicio){
    return this.dataInicio;
}
public void setDataFim(LocalDateTime dataFim){
    this.dataFim=dataFim;
}
public LocalDateTime getDataFim(LocalDateTime dataFim){
   return this.dataFim;
}

//Outros métodos

    public double aplicarTaxa(Usuario usuario, double valorTotal){
        valorTotal -= valorTotal*getTaxaDesconto();
    return valorTotal;
    }

    public boolean verificarValidade(){
        //NÃO FIZ ESSE :( SÓ INCIALIZEI
        boolean resultado = false;
        return resultado;
    }

}
