package timeShare;

import java.util.ArrayList;
import java.time.LocalDate;

public class Administrador extends Pessoa {

    private ArrayList<Bem> bens;

    public Administrador(String nome, String cpf, String email, String senha, LocalDate dataNascimento) {
        super(nome, cpf, email, senha, dataNascimento);
        this.bens = new ArrayList<Bem>();
    }

    public void cadastrarBem(String nome, String descricao, String localizacao, int capacidade) {
        Bem novo = new Bem(nome, descricao, localizacao, capacidade, this);
        this.bens.add(novo);
    }

    public boolean removerBem(Bem bem) {
        boolean removido = false;
        for (Bem b : bens) {
            if (b.getId() == bem.getId()) {
                this.bens.remove(bem);
                removido = true;
            }
        }
        return removido;
    }


    public ArrayList<Bem> getBens() {
        ArrayList<Bem> resultado = new ArrayList<>();
        for (Bem b : bens) {
            resultado.add(b);
        }
        return resultado;
    }

    public String toString() {
        String resultado = "-------------------------\n";
        resultado += String.format("Nome: %s\nCPF: %s\n" +
                "Email: %s\nSenha: %s\n ", getNome(), getCpf(), getEmail(), getSenha());
        resultado += "\n-------------------------";
        return resultado;
    }
}

