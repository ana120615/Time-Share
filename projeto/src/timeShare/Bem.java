package timeShare;

import java.util.ArrayList;

public class Bem {
    private int id;
    private String nome;
    private String descricao;
    private String localizacao;
    private int capacidade;
    private ArrayList<Cota> cotas;

    public Bem(String nome, String descricao, String localizacao, int capacidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.capacidade = capacidade;
    }

    //Métodos get e set
    public Cota getCota(int id) {
        for (Cota cota : cotas) {
            if (cota.getId() == id) {
                return cota;
            }
        }
        return null;
    }

    public void setCotas(ArrayList<Cota> cotas) {
        this.cotas = cotas;
    }

    public void getCotas() {
        for (Cota c: cotas) {
            System.out.println(c);
        }
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
    public void criarCotasParaBem(Cota cota) {
        if (cota != null) {
            cotas.add(cota);
            System.out.println("Inserção concluída.");
        }
        else {
            System.out.println("Falha");
        }
    }

}
