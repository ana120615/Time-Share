package negocio.beans;
import dados.RepositorioCotas;

public class Bem {
    private int id;
    private String nome;
    private String descricao;
    private String localizacao;
    private int capacidade;
    private RepositorioCotas repositorioCotas;

    public Bem(int id, String nome, String descricao, String localizacao, int capacidade){
    setId(id);
    setCapacidade(capacidade);
    setDescricao(descricao);
    setLocalizacao(localizacao);
    setNome(nome);
    this.repositorioCotas=new RepositorioCotas();
    }
  
    //METODOS GET E SET
    public void buscarCotas() {
        this.repositorioCotas.listar();; // Retorna a lista de cotas associadas ao bem
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
}
