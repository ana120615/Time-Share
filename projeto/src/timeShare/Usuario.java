package timeShare;

import java.time.LocalDate;


public class Usuario extends Pessoa {
    public Usuario(String nome, String cpf, String email, String senha, LocalDate dataNascimento) {
        super(nome, cpf, email, senha, dataNascimento);
    }



}