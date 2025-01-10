package negocio.beans;

public class Estadia {
    private int id;
    private Reserva reserva;

    public Estadia(int id, Reserva reserva) {
        this.reserva = reserva;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    //CALCULAR DURACAO DA ESTADIA
    public int calcularDuracao () {
      return (int) ChronoUnit.DAYS.between (this.reserva.getDataInicio(),this.reserva.getDataFim());
    }
}
