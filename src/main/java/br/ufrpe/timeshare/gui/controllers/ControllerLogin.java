package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.gui.application.ScreenManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ControllerLogin {
    @FXML
    private TextField txtUser;
    @FXML
    private TextField txtPassword;

    @FXML
    private void handleLogin() {
        System.out.println("Usuário: " + txtUser.getText());
        System.out.println("Senha: " + txtPassword.getText());
    }

//    @FXML
//    private void irParaCadastro() {
//        ScreenManager.getInstance().showCadastroScreen();
//    }

    @FXML
    public void irTelaCadastro(ActionEvent event) {
        ScreenManager.getInstance().showCadastroScreen();
    }
}
