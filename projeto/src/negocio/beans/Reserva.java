<<<<<<<< HEAD:projeto/src/br/ufrpe/time_share/negocio/beans/Reserva.java
package br.ufrpe.time_share.negocio.beans;
========
package negocio.beans;
>>>>>>>> laila_branch:projeto/src/negocio/beans/Reserva.java

import java.util.Date;
import java.util.Calendar;

public class Reserva {
private int id;
private Date dataInicio;
private Date dataFim;
private boolean status;
private double taxa;
private Usuario usuario;


public Reserva ( int id, Date dataInicio, Date dataFim, Usuario usuario){
    this.id = id; 
    this.status = true; 
    this.dataInicio = dataInicio; 
    this.dataFim = dataFim; 
    this.taxa = 0.00d;
    this.usuario = usuario; 
}

public double calcularTaxa(boolean foraPeriodo) {

    if (eAltaTemporada(dataInicio)){
        this.taxa =200.00d;

    }else{
        this.taxa = 75.00d;
    }
   return this.taxa; 
	
}
private boolean eAltaTemporada(Date data){
    Calendar calendario = Calendar.getInstance();
    calendario.setTime(data);
    int mes = calendario.get(Calendar.MONTH);

    return mes == Calendar.DECEMBER || mes == Calendar.JANUARY || mes == Calendar.FEBRUARY;
}
public void cancelarReserva() {
	this.status = false;
}

public double  getTaxa() {
    return taxa;


}

public void setTaxa( double taxa){
    this.taxa = taxa;
}
public void setStatus (boolean status){
    this.status = status; 
}
public boolean getStatus(){
    return this.status;
}
public Date getDataInicio() {
    return dataInicio;
}
public Usuario getUsuario() {
    return usuario;
}
public void setUsuario(Usuario usuario ){
    this.usuario = usuario;
}

public void setDataInicio(Date dataInicio) {
    this.dataInicio = dataInicio;
}

public Date getDataFim() {
    return dataFim;
}
public void setDataFim(Date dataFim) {
    this.dataFim = dataFim;
}

public int getId() {
    return id;
}

public void setId(int id) {
    this.id = id;
}
}
