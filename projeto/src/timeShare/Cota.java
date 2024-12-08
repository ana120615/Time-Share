package timeShare;


import java.util.Date;
import java.util.Calendar;

public class Cota {
    private int id;
    private int numero;
    private String periodo;
    private int ano;
    private Date dataInicio;
    private Date dataFim;
    private double preco;
    private boolean status;

    //Métodos get e set
    public int getAno(){
        return this.ano;
    }
    public void setAno(int ano){
        this.ano=ano;
    }
    public Date getDataInicio(){
        return this.dataInicio;
    }
    public void setDataInicio(Date dataInicio){
        this.dataInicio=dataInicio;
    }
    public Date getDataFim(){
        return this.dataFim;
    }
    public void setDataFim(Date dataFim){
        this.dataFim=dataFim;
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

    public int getNumero() {
        return this.numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public double getPreco() {
        return this.preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getPeriodo() {
        return this.periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Cota(int id, int numero, double preco) {
        this.id = id;
        this.numero = numero;
        this.preco = preco;
        this.status = true; //inicializada como disponivel
    }

    public void calcularDeslocamento() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDataInicio());
        calendar.add(Calendar.YEAR,1);
        calendar.add(Calendar.WEEK_OF_YEAR,1);
        Date novaInicio = calendar.getTime();

        calendar.setTime(getDataFim());
        calendar.add(Calendar.YEAR,1);
        calendar.add(Calendar.WEEK_OF_YEAR,1);
        Date novaFim = calendar.getTime();
        System.out.println("Periodo: "+ novaInicio+" - "+novaFim);
        
        //Talvez só deslocar depois de usar a cota
        setDataInicio(novaInicio);
        setDataFim(novaFim);
    }
}