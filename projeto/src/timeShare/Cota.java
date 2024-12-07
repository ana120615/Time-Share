package timeShare;

public class Cota {
private int id;
private int numero;
private String periodo;
private int ano;
private double preco;
private boolean status;

//Métodos get e set
public int getId() {
	return this.id;
}
public void setId(int id) {
	this.id=id;
}
public boolean getStatus() {
	return this.status;
}
public void setStatus(boolean status) {
	this.status = status;
}
public int getNumero() {
	return this.numero;
}
public void setNumero(int numero) {
	this.numero=numero;
}
public double getPreco () {
	return this.preco;
}
public void setPreco(double preco) {
	this.preco=preco;
}
public String getPeriodo() {
	return this.periodo;
}
public void setPeriodo(String periodo) {
	this.periodo=periodo;
}

public Cota(int id, int numero, double preco) {
	this.id=id;
	this.numero=numero;
	this.preco=preco;
	this.status=true; //inicializada como disponivel
}
public String calcularDeslocamento(int anoAtual) {
	return "bilubili";
}
}