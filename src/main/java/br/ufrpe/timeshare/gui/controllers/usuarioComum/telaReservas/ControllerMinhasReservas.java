package br.ufrpe.timeshare.gui.controllers.usuarioComum.telaReservas;

import br.ufrpe.timeshare.gui.application.ScreenManager;
import javafx.event.ActionEvent;

public class ControllerMinhasReservas {

//celulas para mostrar reservas
//botoes de realizar checkin, cancelar reserva e alterar periodo reserva


@FXML
private ListView <VBox> reservasListView;

private ObservableList<Reservas> reservas = FXCollections.observableArrayList();

ControladorReservas controladorReservas;

private Usuario usuarioLogado;

public void initialize(){
controladorReservas = new controladorReservas();
Object data = ScreenManager.getInstance().getData();
if(data instanceof Usuario){
    this.usuarioLogado = (Usuario) data;
}
reservas.addAll(controladorReservas.listarReservasUsuario(usuarioLogado));
exibirReservas();
}

private void exibirReservas(){
    ObservableList<VBox> itens = FXCollections.observableArrayList();
   for(Reserva reserva: reservas){
    VBox item = criarItemReserva(reserva);
    itens.add(item);
   }
   reservasListView.setItems(itens);
}

private VBox criarItemReserva(Reserva reserva){
Label labelReserva = new Label(reserva.toString());

Button 
}

    //voltar para tela principal
    public void voltarParaPrincipalComum(ActionEvent event){
        ScreenManager.getInstance().showUsuarioComumPrincipalScreen();
    }
}
