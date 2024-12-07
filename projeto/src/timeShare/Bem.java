package timeShare;

import java.util.ArrayList;

public class Bem {
    private int id;
    private String nome;
    private String descricao;
    private String localizacao;
    private int capacidade;
    private ArrayList<Cota> fracoes;

    //Métodos get e set
    public Cota getFracao(int id) {
        for (Cota cota : fracoes) {
            if (cota.getId() == id) {
                return cota;
            }
        }
        return null;
    }

    public void setFracoes(ArrayList<Cota> fracoes) {
        this.fracoes = fracoes;
    }

    public ArrayList<Cota> getFracoes() {
        return this.fracoes;
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
    public boolean criarFracoes(int numero, double preco) {
        boolean resultado = false;
        double valor = preco / numero;
        if (fracoes == null) {
            fracoes = new ArrayList<>();
        }
        if (numero > 0 && preco > 0) {
            for (int i = 0; i < numero; i++) {
                int id = i;
                Cota cota = new Cota(id, id, valor);
                fracoes.add(cota);
                resultado = true;
            }
        } else {
            System.out.println("Erro");
        }
        return resultado;
    }

    public boolean verificarDisponibilidade(int id) {
        boolean resultado = false;
        for (Cota fracoes : fracoes) {
            if (fracoes.getId() == id) {
                resultado = fracoes.getStatus();
            }
        }
        return resultado;
    }

    public void gerarRelatorioDeUso() {

    }

}
