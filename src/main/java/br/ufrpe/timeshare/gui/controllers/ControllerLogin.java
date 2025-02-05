package br.ufrpe.timeshare.gui.controllers;

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
}
