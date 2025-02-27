package br.ufrpe.timeshare.gui.controllers.basico;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ControllerAjudaRespostas {

    @FXML
    private StackPane respostaPane;

    @FXML
    private Button voltarButton;

    public void exibirResposta(String resposta) {
        Label respostaLabel = new Label(resposta);
        respostaLabel.setWrapText(true); 
        respostaPane.getChildren().clear(); 
        respostaPane.getChildren().add(respostaLabel); 
    }

    @FXML
    public void voltar(ActionEvent event) {
        Stage stage = (Stage) voltarButton.getScene().getWindow();
        stage.close();
    }
}