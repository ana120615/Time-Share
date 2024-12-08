package timeShare;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Usuario extends Pessoa {
private ArrayList <Cota> cotasCompradas;
private ArrayList <Reserva> reservasFeitas;
public Usuario(String nome, String cpf, String email, String senha, LocalDate dataNascimento) {
		super(nome, cpf, email, senha, dataNascimento);
		this.cotasCompradas= new ArrayList<>();
		this.reservasFeitas = new ArrayList<>();
	}

public boolean possuiCota(int cotaId) {
	boolean resultado = false;
    for (Cota cota : cotasCompradas) {
        if (cota.getId() == cotaId) {
            resultado = true; 
        }
    }
    return resultado;
}

public Venda adquirirCota(int cotaId, Bem bem, Date dataInicio, Date dataFim) {
	LocalDate dataVenda= LocalDate.now();
	Cota cota = bem.getCota(cotaId);
	double valor = cota.getPreco();
	Venda nova = null;
	if(cota!=null) {
		if(calcularIdade()>=18){
	if (bem.verificarDisponibilidade(cotaId)) {
		this.cotasCompradas.add(cota);
		cota.setStatus(false);
		cota.setDataInicio(dataInicio);
		cota.setDataFim(dataFim);
		System.out.println("Cota adquirida");
		nova = new Venda(cotaId,dataVenda,valor,bem);
	}
	else {
		System.out.println("Cota indisponivel");
	}
		}
		else{
			System.out.println("Idade insuficiente");
		}
}
	return nova;
}

public Reserva realizarReserva(int cotaId, Date dataInicio, Date dataFim, Bem bem) {
	Random random = new Random();
	double taxa;
	int idReserva = random.nextInt(100); 
	Cota cota = bem.getCota(cotaId);
	Reserva novaReserva=null;
	if(possuiCota(cotaId)) {
		if(calcularIdade()>=18){
		 novaReserva = new Reserva (idReserva, dataInicio, dataFim);
		
		if (dataInicio.equals(cota.getDataInicio()) && dataFim.equals(cota.getDataFim())) {
             novaReserva.setTaxa(novaReserva.calcularTaxa(false));
			 taxa=novaReserva.getTaxa();
        } else {
             novaReserva.setTaxa(novaReserva.calcularTaxa(true));
			 taxa=novaReserva.getTaxa();
        }
		System.out.println("TAXA: " + taxa);
		reservasFeitas.add(novaReserva);
		
	}
	else{
		System.out.println("Idade insuficiente");;
	}
	}
	else{
		System.out.println("Cota nao encontrada");
	}
	return novaReserva;
}
public void consultarReservas() {
	
	System.out.println("RESERVAS: ");
	for (Reserva reserva : reservasFeitas) {
        System.out.println(reserva);
    }
}
}
