package timeShare;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;


public class Venda {
private int id;
private LocalDate data;
private double valor;
private Cota cota; 
private Usuario usuario;
private Bem bem;

public Venda(int id, LocalDate data, double valor, Bem bem) {
	this.id=id;
	this.data=data;
	this.valor=valor;
	this.bem=bem;
}

//Métodos get e set
public void setId(int id) {
	this.id=id;
}
public int getId() {
	return this.id;
}
public void setData(LocalDate data) {
	this.data=data;
}
public LocalDate getData() {
	return this.data;
}
public void setValor(double valor) {
	this.valor=valor;
}
public double getValor() {
	return this.valor;
}
public void setFracao(Cota cota) {
	this.cota = cota;
}
public Cota getFracao() {
	return this.cota;
}
public void setUsuario(Usuario usuario) {
	this.usuario=usuario;
}
public Usuario getUsuario() {
	return this.usuario;
}
public void setBem(Bem bem) {
	this.bem=bem;
}
public Bem getBem() {
	return this.bem;
}

//Outros métodos

public String gerarNotaFiscal() {
   String notaFiscal = "Nota Fiscal: \n ";

   notaFiscal += "Cliente: " + usuario.getNome() + "/n";
   notaFiscal+= "--------------------------------------\n";
   notaFiscal+= "Nome do Time Share\n";
   notaFiscal+= "--------------------------------------\n";
   notaFiscal += "CPF: " + usuario.getCpf() + "\n";
   notaFiscal +=  "cota:" ; // fracao ainda está sem descrição
   notaFiscal+= "Período: " + cota.getPeriodo();
   notaFiscal += "Valor: R$" + valor + "\n";
   notaFiscal += "Data de Emissão: " + new Date() + "\n";
    String numeroNotaFiscal = UUID.randomUUID().toString();
	notaFiscal += "Número da Nota Fiscal: " + numeroNotaFiscal + "\n";
   
   return notaFiscal;


}
public void confirmarVenda() {
	System.out.println("Venda confrimada para usuário:" + usuario.getNome());
	
}
}