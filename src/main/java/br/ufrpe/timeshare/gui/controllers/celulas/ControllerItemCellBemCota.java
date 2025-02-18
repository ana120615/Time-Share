package br.ufrpe.timeshare.gui.controllers.celulas;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import br.ufrpe.timeshare.negocio.beans.Bem;

import java.io.File;

public class ControllerItemCellBemCota {

    @FXML
    private HBox itemCell;
    @FXML
    private ImageView imagemBem;
    @FXML
    private Label nomeBem;
    @FXML
    private Label descricaoBem;
    @FXML
    private Label localizacaoBem;
    @FXML
    private Label capacidadeBem;

    public void setItem(Bem bem) {
        if (bem != null) {
            nomeBem.setText(bem.getNome());
            descricaoBem.setText(bem.getDescricao());
            localizacaoBem.setText(bem.getLocalizacao());
            capacidadeBem.setText(String.valueOf(bem.getCapacidade()));

            if (bem.getCaminhoImagem() != null && !bem.getCaminhoImagem().isEmpty()) {
                File file = new File(bem.getCaminhoImagem());
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    imagemBem.setImage(image);
                } else {
                    imagemBem.setImage(null); 
                }
            } else {
                imagemBem.setImage(null); 
            }
        }
    }
}