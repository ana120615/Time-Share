package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;

public class UsuarioAdm extends Usuario {
    //Construtor
    public UsuarioAdm(int cpf, String nome, String email, String senha, LocalDate dataNascimento){
        super(cpf, nome, email, senha, dataNascimento);
    }
    //METODO TO STRING
    public String toString(){
    return "USUARIO ADMINISTRADOR: NOME- "+ this.getNome() + " CPF- "  +String.format("%d", this.getCpf())+ " EMAIL- " +this.getEmail()+ " SENHA- " +this.getSenha()+" IDADE- " +String.format("%d", this.calcularIdade());
    }

    @Override
    public boolean eAdm() {
        return true;
    }
}