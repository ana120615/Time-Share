package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;
import java.util.ArrayList;

public class UsuarioAdm extends Usuario {
    public final String nivelAcesso = "ADM";
    private ArrayList<Bem> bens;

    {
        this.bens = new ArrayList<>(10);
    }

    //CONSTRUTORES
    public UsuarioAdm(int cpf, String nome, String email, String senha, LocalDate dataNascimento) {
        super(cpf, nome, email, senha, dataNascimento);
    }

    public UsuarioAdm(int cpf, String senha) {
        super(cpf, senha);
    }

    //OUTROS METODOS
    public String consultarBens() {
        String resultado = "";
        for (Bem bem : this.bens) {
            resultado += bem + "\n";
        }
        return resultado;
    }


    public void cadastrarBem(Bem bem) {
        if (bem != null) {
            this.bens.add(bem);
        }
    }



    @Override
    public String toString() {
        return super.toString() + " " +
                "nivelAcesso=" + nivelAcesso;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UsuarioAdm) {
            UsuarioAdm usuario = (UsuarioAdm) obj;
            return super.equals(usuario);
        }
        return false;
    }
}
