package timeShare;

import java.util.ArrayList;

public class Bem {
    private int id;
    private String nome;
    private String descricao;
    private String localizacao;
    private int capacidade;
    private ArrayList<Cota> cotas;

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

    public Bem(String nome, String descricao, String localizacao, int capacidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.capacidade = capacidade;
    }

    //Outros métodos
    public boolean criarCotas(int numero, double preco) {
        boolean resultado = false;
        double valor = preco / numero;
        if (cotas != null) {
        if (numero > 0 && preco > 0) {
            for (int i = 0; i < numero; i++) {
                int id = i;
                Cota cota = new Cota(id, id, valor);
                cotas.add(cota);
                resultado = true;
            }
        } else {
            System.out.println("Erro");
        }
    }
        return resultado;
    }

    public boolean verificarDisponibilidade(int id) {
        boolean resultado = false;
        for (Cota cotas : cotas) {
            if (cotas.getId() == id) {
                resultado = cotas.getStatus();
            }
        }
        return resultado;
    }

}
