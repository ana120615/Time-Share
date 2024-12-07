package timeShare;

import java.util.Date;

public class Venda {
private int id;
private Date data;
private double valor;
private Fracao fracao;
private Usuario usuario;
private Bem bem;

public Venda(int id, Date data, double valor, Bem bem) {
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
public void setData(Date data) {
	this.data=data;
}
public Date getData() {
	return this.data;
}
public void setValor(double valor) {
	this.valor=valor;
}
public double getValor() {
	return this.valor;
}
public void setFracao(Fracao fracao) {
	this.fracao=fracao;
}
public Fracao getFracao() {
	return this.fracao;
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
	
}
public void confirmarVenda() {
	
}
}