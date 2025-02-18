package br.ufrpe.timeshare.gui.controllers.usuarioComum.telaReservas;

import br.ufrpe.timeshare.gui.application.ScreenManager;
import javafx.event.ActionEvent;

public class ControllerMinhasReservas {

//celulas para mostrar reservas
//botoes de realizar checkin, cancelar reserva e alterar periodo reserva

    //voltar para tela principal
    public void voltarParaPrincipalComum(ActionEvent event){
        ScreenManager.getInstance().showUsuarioComumPrincipalScreen();
    }
}
