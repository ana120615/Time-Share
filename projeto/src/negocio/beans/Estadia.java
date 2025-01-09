
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

    public int calcularDuracao () {

        // CALCULAR DURACAO COM BASE NA RESERVA QUE POSSUI ATRIBUTOS DE TEMPO
        return 1;
    }
}
