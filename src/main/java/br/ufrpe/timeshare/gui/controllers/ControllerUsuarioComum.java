package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.gui.application.ScreenManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ControllerUsuarioComum {

    @FXML
    public void irTelaBensOfertados(ActionEvent event) {
        ScreenManager.getInstance().showCadastroScreen();
    }

    @FXML
    public void irTelaGerenciamentoReservas(ActionEvent event) {
        ScreenManager.getInstance().showCadastroScreen();
    }

    @FXML
    public void irTelaMinhasCotas(ActionEvent event) {
        ScreenManager.getInstance().showCadastroScreen();
    }




}
