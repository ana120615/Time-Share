package br.ufrpe.timeshare.gui.controllers.celulas;

public class ControllerMinhasReservas{

private Usuario usuarioLogado;
private ControladorReservas controladorReservas;
private Reserva reserva;

public void initialize(){
    this.controladorReservas = new ControladorReservas();

 // Usuario logado
Object data = ScreenManager.getInstance().getData();

if (data instanceof Usuario) {

 this.usuarioLogado = (Usuario) data;

}
}

public void setItem(Reserva reserva){
this.reserva=reserva;

if(reserva)

}

}