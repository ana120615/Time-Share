package timeShare;

import java.util.ArrayList;
import java.time.LocalDate;

public class Administrador extends Pessoa {

    private ArrayList<Bem> bens;

    public Administrador(String nome, String cpf, String email, String senha, LocalDate dataNasicmento) {
        super(nome, cpf, email, senha, dataNascimento);
        this.bens = new ArrayList<Bem>();
    }

    public void cadastrarBem(String nome, String descricao, String localizacao, int capacidade) {
        Bem novo = new Bem(nome, descricao, localizacao, capacidade);
        this.bens.add(novo);
    }

    public void removerBem(Bem bem) {
        this.bens.remove(bem);
    }

    public void gerenciarCotas(int bemId) {

    }

    public consultarRelatorios() {

    }

    public ArrayList<Bem> getBens(int id) {
        for(Bem b : bens) {
            if(b.getId() == id) {

            }
        };
    }

}