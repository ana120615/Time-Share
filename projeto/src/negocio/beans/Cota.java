package negocio.beans;
import java.time.LocalDate;

public class Cota {
    private int id;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String descricao;
    private double preco;
    private Usuario proprietario;
    private boolean status;

    public Cota(int id, double preco, String descricao, LocalDate dataInicio, LocalDate dataFim, Usuario proprietario) {
        this.setId(id);
        this.setPreco(preco);
        this.setDescricao(descricao);
        this.setDataInicio(dataInicio);
        this.setDataFim(dataFim);
        this.setProprietario(proprietario)
        this.status = true; //inicializado como disponivel
    }


    //Métodos get e set
    public Usuario getProprietario(){
    return this.proprietario;
    }
    public void setProprietario(Usuario proprietario){
        this.proprietario=proprietario;
    }
    public LocalDate getDataInicio() {
        return dataInicio;
    }


    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }
    public LocalDate getDataFim() {
        return dataFim;
    }


    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getPreco() {
        return this.preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public boolean isStatus() {
        return status;
    }
    
    //CALCULAR DESLOCAMENTO DA COTA
    public void calcularDeslocamento() {
            this.dataInicio = dataInicio.plusYears(1).plusWeeks(1);
            this.dataFim = dataFim.plusYears(1).plusWeeks(1);
    }
}
