package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;

public class UsuarioComum extends Usuario {
    public final String nivelAcesso = "COMUM";

    //Construtor
    public UsuarioComum(int cpf, String nome, String email, String senha, LocalDate dataNascimento) {
        super(cpf, nome, email, senha, dataNascimento);
    }

    public UsuarioComum(int cpf, String senha) {
        super(cpf, senha);
    }

    @Override
    public boolean eAdm() {
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + " " +
                "nivelAcesso=" + nivelAcesso;
    }

}
