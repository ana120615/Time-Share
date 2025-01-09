package negocio.beans;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import java.util.ArrayList;

public class Venda {
	private int id;
	private LocalDateTime data;
	private double valorTotal;
	private Cota cota;
	private Usuario usuario;
	private ArrayList<Cota> cotas;
	private Promocao promocao;
	


	public Venda(int id, Usuario usuario, Cota cota) {
		this.id = id;
		this.valorTotal = 0;
		this.cotas.add(cota);
		this.usuario = usuario;
	
		
	
	}

	//Métodos get e set
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public double getValorTotal() {
		return this.valorTotal;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public Cota getCota() {
		return this.cota;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}


//Outros métodos

	public String gerarNotaFiscal() {
		String notaFiscal = "Nota Fiscal: \n ";

		notaFiscal += "Cliente: " + usuario.getNome() + "\n";
		notaFiscal += "--------------------------------------\n";
		notaFiscal += " Flex Share \n";
		notaFiscal += "--------------------------------------\n";
		notaFiscal += "CPF: " + usuario.getCpf() + "\n";
		notaFiscal += "cota:" + cota.getDescricao()  + "\n"; 
		notaFiscal += "Período: " + cota.getPeriodo() + "\n";
		notaFiscal += "Valor: R$" + valorTotal + "\n";
		notaFiscal += "Data de Emissão: " + LocalDate.now() + "\n";
		String numeroNotaFiscal = UUID.randomUUID().toString();
		notaFiscal += "Número da Nota Fiscal: " + numeroNotaFiscal + "\n";

		return notaFiscal;


	}

	public void calcularValorTotal() {
		double resultado = 0;
		for (Cota c : cotas) {
			resultado += c.getPreco();
		}
		this.valorTotal = resultado;
	}

	public boolean adicionarCota (Cota cota) {
		boolean resultado = false;
		if (cota != null) {
			cotas.add(cota);
			resultado = true;
		}
		return resultado;
	}

	public boolean removerCota (Cota cota) {
		boolean resultado = false;
		if (cota != null) {
			cotas.remove(cota);
			resultado = true;
		}
		return resultado;
	}

	public boolean finalizarCompra() {
		/*
		REALIZAR A FINALIZAÇÂO DE COMPRA DE TAL FORMA QUE ALIMENTE A LISTA DE COTAS DO USUÁRIO.
		 */
		return false;
	}

	public ArrayList<Cota> listarCotas() {
		ArrayList<Cota> resultado = new ArrayList<>();
		for (Cota c : cotas) {
			resultado.add(c);
		}
		return resultado;
	}

	public double aplicarPromocao () {
		/*
		UTILIZAR O VALOR TOTAL DA VENDA MENOS UM DESCONTO DADO POR UM ANIVERSÁRIO OU PERIODO ESPECIFICO
		 */
		return 0;
	}
	public boolean verificarPromocao () {
		/*
		RETORNAR A VERACIDADE PARA PODER APLICAR A PROMOÇÃO. TAMBÉM PODERIA SER UMA CONDICIONAL DENTRO
		DE APLICARPROMOCAO
		 */
		return false;
	}


}