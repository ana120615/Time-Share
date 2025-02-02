package br.ufrpe.timeshare.negocio.beans;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Reserva extends Entidade{
    private int id;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Usuario usuarioComum;
    private Bem bem;
    private boolean cancelada;

    public Reserva(int id, LocalDate dataInicio, LocalDate dataFim, Usuario usuarioComum, Bem bem) {
        this.id = id;
        this.cancelada = false;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.usuarioComum = usuarioComum;
        this.bem = bem;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    public void setCancelada(boolean cancelada) {
        this.cancelada = cancelada;
    }

    public void cancelarReserva() {
        this.cancelada = true;
    }

    public Bem getBem() {
        return bem;
    }

    public void setBem(Bem bem) {
        this.bem = bem;
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

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Usuario getUsuarioComum() {
        return usuarioComum;
    }


    public void setUsuarioComum(Usuario usuarioComum) {
        this.usuarioComum = usuarioComum;
    }

    public int calcularDuracaoReserva(){
        return (int) this.getDataInicio().until(this.getDataFim(), ChronoUnit.DAYS);
    }


    //verificar se precisa formatar os numeros
    @Override
    public String toString() {
     String comprovanteReserva = "FLEX SHARE\n ";
     comprovanteReserva += "--------------------------------------\n";
     comprovanteReserva += "Data e hora de emissão: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))+"\n";
     comprovanteReserva += "--------------------------------------\n";
     comprovanteReserva +=  "Comprovante da reserva: " + id + "\n";
     comprovanteReserva += "--------------------------------------\n";
     comprovanteReserva += "Cliente: " + usuarioComum.getNome()+"\n";
     comprovanteReserva +=  "CPF: " + usuarioComum.getId() + "\n";
     comprovanteReserva += "Periodo reservado: " + dataInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+ " até " + dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+"\n";
     comprovanteReserva+= "Bem reservado: " + bem.getId() + "-" + bem.getNome()+"\n";
        return comprovanteReserva;
    }

    @Override
    public boolean equals(Object object) {
        boolean resultado = false;
        if (object instanceof Reserva) {
            Reserva reserva = (Reserva) object;
            resultado = this.id == reserva.id && this.dataInicio.equals(reserva.dataInicio)
                    && this.dataFim.equals(reserva.dataFim);
        }

        return resultado;
    }


}
