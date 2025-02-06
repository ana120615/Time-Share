package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.gui.application.ScreenManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class
ControllerUsuarioComum {


    @FXML
    public void irTelaConfiguracoesUsuarioComum(ActionEvent event) {
        ScreenManager.getInstance().showConfiguracoesUsuarioComumScreen();
    }

    @FXML
    public void irTelaPrincipalUsuarioComum(ActionEvent event) {
        ScreenManager.getInstance().showUsuarioComumPrincipalScreen();
    }

    @FXML
    public void deslogar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Deseja realmente sair?");
        alert.setContentText("Clique em OK para confirmar ou Cancelar para voltar.");

        // Exibindo o alerta e capturando a resposta
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            ScreenManager.getInstance().showLoginScreen();
            System.out.println("Usuário confirmou a ação!");
        } else {
            System.out.println("Usuário cancelou a ação!");
        }

    }


}
