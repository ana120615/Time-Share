package br.ufrpe.timeshare.negocio.beans;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Estadia extends Entidade {
    private int id;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Reserva reserva;

    public Estadia(int id, Reserva reserva) {
        this.reserva = reserva;
        this.id = id;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public long getId() {
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
      return (int) getDataInicio().until(getDataFim(), ChronoUnit.DAYS);
    }

    @Override
    public String toString() {
    String   comprovanteEstadia = "FLEX SHARE\n ";
    comprovanteEstadia += "--------------------------------------\n";
    comprovanteEstadia += "Data e hora de emissão: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))+"\n";
    comprovanteEstadia += "--------------------------------------\n";
    comprovanteEstadia +=  "Comprovante da estadia: " + id + "\n";
    comprovanteEstadia += "--------------------------------------\n";
    comprovanteEstadia += "Cliente: " + reserva.getUsuarioComum().getNome()+"\n";
    comprovanteEstadia +=  "CPF: " + reserva.getUsuarioComum().getId() + "\n";
    comprovanteEstadia+= "Bem: " + reserva.getBem().getId() + "-" + reserva.getBem().getNome()+"\n";
    comprovanteEstadia += "Periodo da estadia: " + dataInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+ " até " + dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+"\n";
        return comprovanteEstadia;      
    }
}
