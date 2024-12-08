package timeShare;

import java.util.ArrayList;

public class Bem {
    private int id;
    private String nome;
    private String descricao;
    private String localizacao;
    private int capacidade;
    private ArrayList<Cota> cotas;
    private Administrador adm;

    public Bem(String nome, String descricao, String localizacao, int capacidade, Administrador adm) {
        this.nome = nome;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.capacidade = capacidade;
        this.adm = adm;
    }

    //Métodos get e set

    public Administrador getAdm() {
        return adm;
    }

    public void setAdm(Administrador adm) {
        this.adm = adm;
    }

    public Cota getCota(int id) {
        for (Cota c : cotas) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public void setCotas(ArrayList<Cota> cotas) {
        this.cotas = cotas;
    }

    public ArrayList<Cota> getCotas() {
        return this.cotas;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
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


    //Outros métodos
    public void criarCotas(Cota cota) {
        if (cota != null) {
            cotas.add(cota);
        }
        else {
            System.out.println("Erro.");
        }
    }

    public boolean verificarDisponibilidade(int id) {
        boolean resultado = false;
        for (Cota fracoes : cotas) {
            if (fracoes.getId() == id) {
                resultado = fracoes.getStatus();
            }
        }
        return resultado;
    }

    public void gerarRelatorioDeUso() {

    }

}
