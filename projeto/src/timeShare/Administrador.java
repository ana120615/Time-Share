package timeShare;

import java.util.ArrayList;

public class Administrador extends Pessoa {

	public Administrador(String nome, String cpf, String email, String senha) {
		super(nome, cpf, email, senha);
	}

	private ArrayList<Bem> bensCadastrados;

	public void cadastrarBem(String nome, String descricao, String localizacao, int capacidade) {
		if (bensCadastrados == null) {
			bensCadastrados = new ArrayList<>();
		}
		Bem novo = new Bem(nome, descricao, localizacao, capacidade);
		bensCadastrados.add(novo);
	}

	public void definirPrecoFracao(Bem bem, int fracaoId, double preco) {
		if (bem.getFracoes() == null) {
			System.out.println("Nao ha fracoes para o bem.");
		}
		Fracao fracao = bem.getFracao(fracaoId);
		if (fracao != null) {
			fracao.setPreco(preco);
			System.out.println("Preco definido");
		} else {
			System.out.println("Erro");
		}
	}
	
	public void gerenciarFracoes(int bemId) {

	}
   public consultarRelatorios() {
	
}
}