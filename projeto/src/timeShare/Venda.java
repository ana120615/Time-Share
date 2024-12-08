package timeShare;

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


	public Venda(int id, double valor, Cota cota) {
		this.id = id;
		this.valorTotal = valor;
		this.cotas.add(cota);
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
		notaFiscal += "Nome do Time Share\n";
		notaFiscal += "--------------------------------------\n";
		notaFiscal += "CPF: " + usuario.getCpf() + "\n";
		notaFiscal += "cota:"; // fracao ainda está sem descrição
		notaFiscal += "Período: " + cota.getPeriodo();
		notaFiscal += "Valor: R$" + valorTotal + "\n";
		notaFiscal += "Data de Emissão: " + LocalDate.now() + "\n";
		String numeroNotaFiscal = UUID.randomUUID().toString();
		notaFiscal += "Número da Nota Fiscal: " + numeroNotaFiscal + "\n";

		return notaFiscal;


	}

	public void confirmarVenda() {
		System.out.println("Venda confrimada para usuário:" + usuario.getNome());

	}
}