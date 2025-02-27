package br.ufrpe.timeshare.negocio.beans;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Reserva extends Entidade implements Comparable<Reserva> {
    private int id;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Usuario usuarioComum;
    private Bem bem;

    public Reserva(int id, LocalDateTime dataInicio, LocalDateTime dataFim, Usuario usuarioComum, Bem bem) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.usuarioComum = usuarioComum;
        this.bem = bem;
    }

    public Bem getBem() {
        return bem;
    }

    public void setBem(Bem bem) {
        this.bem = bem;
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


    public Usuario getUsuarioComum() {
        return usuarioComum;
    }


    public void setUsuarioComum(Usuario usuarioComum) {
        this.usuarioComum = usuarioComum;
    }

    public int calcularDuracaoReserva(){
        return (int) this.getDataInicio().until(this.getDataFim(), ChronoUnit.DAYS);
    }


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


    @Override
    public int compareTo(Reserva outraReserva) {
        LocalDateTime agora = LocalDateTime.now();
        long diferencaEsta = Math.abs(ChronoUnit.SECONDS.between(agora, this.getDataInicio()));
        long diferencaOutra = Math.abs(ChronoUnit.SECONDS.between(agora, outraReserva.getDataInicio()));

        return Long.compare(diferencaEsta, diferencaOutra);
    }

}
