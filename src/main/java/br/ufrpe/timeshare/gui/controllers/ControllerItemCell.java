package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.negocio.beans.Bem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.PropertySheet;

public class ControllerItemCell {
    @FXML private Button itemButton;
    @FXML private ImageView itemImage;
    @FXML private Label itemLabelDescricao;
    @FXML private Label itemLabelNome;

    private PropertySheet.Item item; // Armazena o item atual

    public void setItem(Bem item) {
        itemLabelNome.setText(item.getNome());
        // itemImage.setImage(new Image(getClass().getResourceAsStream(item.getImagemPath())));

        // Adiciona ação ao botão
        itemButton.setOnAction(e -> {
            System.out.println("Clicou em: " + item.getNome());
        });
    }
}
