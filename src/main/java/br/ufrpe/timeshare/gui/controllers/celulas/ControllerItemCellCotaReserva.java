package br.ufrpe.timeshare.gui.controllers.celulas;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import br.ufrpe.timeshare.negocio.beans.Bem;
import br.ufrpe.timeshare.negocio.beans.Cota;
import java.io.File;

public class ControllerItemCellCotaReserva {

    @FXML
    private HBox itemCell;

    @FXML
    private Label nomeBemAssociado;
    
    @FXML
    private Label dataInicio;

    @FXML
    private Label dataFim;

    @FXML
    private Label nomeProprietario;

    @FXML
    private Label idCota;

    public void setItem(Cota cota) {
        if (cota != null) {
            nomeBemAssociado.setText(cota.getBem().getNome());
            dataInicio.setText(cota.getDataInicio()); //formatar p string
            dataFim.setText(cota.getDataFim()); //formatar p string
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