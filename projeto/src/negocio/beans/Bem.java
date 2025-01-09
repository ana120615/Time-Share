package negocio.beans;
import dados.RepositorioCotas;

public class Bem {
    private int id;
    private String nome;
    private String descricao;
    private String localizacao;
    private int capacidade;
    private Usuario proprietario; 
    private RepositorioCotas repositorioCotas;

    public Bem(int id, String nome, String descricao, String localizacao, int capacidade, Usuario proprietario){
    setId(id);
    setCapacidade(capacidade);
    setDescricao(descricao);
    setLocalizacao(localizacao);
    setNome(nome);
    setProprietario(proprietario);
    this.repositorioCotas=new RepositorioCotas();
    }

    public void buscarCotas() {
      repositorioCotas.listar(); 
    }
    
    //METODOS GET E SET
    public Usuario getProprietario() {
        return proprietario;
    }
    public void setProprietario(Usuario proprietario) {
        this.proprietario = proprietario;
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
