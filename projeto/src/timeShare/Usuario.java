package timeShare;

import java.util.ArrayList;
import java.util.Date;

public class Usuario extends Pessoa {
public Usuario(String nome, String cpf, String email, String senha) {
		super(nome, cpf, email, senha);
	}
private ArrayList <Fracao> fracoesCompradas;

public Venda adquirirFracao(int fracaoId, Bem bem) {
	if (bem.verificarDisponibilidade(fracaoId)) {
		
	}
}
public Reserva realizarReserva(int fracaoId, Date dataInicio, Date dataFim) {
	
}
public consultarReservas() {
	
}
}