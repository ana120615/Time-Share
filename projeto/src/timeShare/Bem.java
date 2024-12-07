package timeShare;

import java.util.ArrayList;

public class Bem {
	private int id;
	private String nome;
	private String descricao;
	private String localizacao;
	private int capacidade;
	private ArrayList<Fracao> fracoes;

//Métodos get e set
	public Fracao getFracao(int id) {
		for (Fracao fracao : fracoes) {
			if (fracao.getId() == id) {
				return fracao;
			}
		}
		return null;
	}

	public void setFracoes(ArrayList<Fracao> fracoes) {
		this.fracoes = fracoes;
	}

	public ArrayList<Fracao> getFracoes() {
		return this.fracoes;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getLocalizacao() {
		return this.localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public int getCapacidade() {
		return this.capacidade;
	}

	public void setCapacidade(int capacidade) {
		this.capacidade = capacidade;
	}

	public Bem(String nome, String descricao, String localizacao, int capacidade) {
		this.nome = nome;
		this.descricao = descricao;
		this.localizacao = localizacao;
		this.capacidade = capacidade;
	}

//Outros métodos
	public boolean criarFracoes(int numero, double preco) {
		boolean resultado = false;
		double valor = preco / numero;
		if (fracoes == null) {
			fracoes = new ArrayList<>();
		}
		if (numero > 0 && preco > 0) {
			for (int i = 0; i < numero; i++) {
				int id = i;
				Fracao fracao = new Fracao(id, id, valor);
				fracoes.add(fracao);
				resultado = true;
			}
		} else {
			System.out.println("Erro");
		}
		return resultado;
	}

	public boolean verificarDisponibilidade(int id) {
		boolean resultado = false;
		for (Fracao fracoes : fracoes) {
			if (fracoes.getId() == id) {
				resultado = fracoes.getStatus();
			}
		}
		return resultado;
	}

	public void gerarRelatorioDeUso() {

	}

}
