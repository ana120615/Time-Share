package timeShare;

public class Estadia {
    private int id;
    //Métodos get e set
    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return this.id;
    }

    //Outros métodos

    //Talvez colocar algum marcador de que já usou as cotas na estadia
    public int calcularDuracao (Reserva reserva){
        long difmili = reserva.getDataFim().getTime() - reserva.getDataInicio().getTime();
        int difdias = (int) (difmili/(1000*60*60*24));
        return difdias;
    }
}
