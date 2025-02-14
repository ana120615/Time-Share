package br.ufrpe.timeshare.gui.controllers.celulas;

import br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaMeusBens.ControllerEditarBemPopUp;
import br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaMeusBens.ControllerListarBens;
import br.ufrpe.timeshare.negocio.beans.Bem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ControllerItemCellBem {

    @FXML
    private Button itemButton;

    @FXML
    private ImageView itemImage;

    @FXML
    private Label itemLabelDescricao;

    @FXML
    private Label itemLabelNome;

    private Bem bem;
    private ControllerListarBens mainControllerBens;

    public void setItem(Bem item) {
        this.bem = item;

        if (item == null) {
            itemLabelNome.setText("Item não encontrado");
            itemLabelDescricao.setText("Descrição não disponível");
            itemImage.setImage(carregarImagemPadrao());
            return;
        }

        itemLabelNome.setText(item.getNome() != null ? item.getNome() : "Nome não disponível");
        itemLabelDescricao.setText(item.getDescricao() != null ? item.getDescricao() : "Descrição não disponível");
        itemImage.setImage(carregarImagem(item.getCaminhoImagem()));

        itemButton.setOnAction(e -> showPopup()); // Agora chama o pop-up ao clicar no botão
    }

    public void setMainControllerBens(ControllerListarBens mainControllerBens) {
        this.mainControllerBens = mainControllerBens;
    }

    private void showPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/EditarBemPopUp.fxml"));
            StackPane popupRoot = loader.load(); // Carrega a interface corretamente

            // Obtém o controlador da tela do pop-up
            ControllerEditarBemPopUp popupController = loader.getController();
            popupController.setBem(bem);
            popupController.setMainController(mainControllerBens);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(new Scene(popupRoot));
            popupStage.setTitle("Detalhes do Bem");
            popupStage.setMinWidth(932);
            popupStage.setMinHeight(650);

            // Aplica efeito de desfoque na tela principal
            if (mainControllerBens != null && mainControllerBens.getListViewItens().getScene() != null) {
                mainControllerBens.getListViewItens().getScene().getRoot().setEffect(new GaussianBlur(10));
            }

            popupStage.showAndWait();

            // Remove efeito de desfoque ao fechar
            if (mainControllerBens != null && mainControllerBens.getListViewItens().getScene() != null) {
                mainControllerBens.getListViewItens().getScene().getRoot().setEffect(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Image carregarImagem(String caminhoImagem) {
        if (caminhoImagem == null || caminhoImagem.isEmpty()) {
            return carregarImagemPadrao();
        }

        try {
            File file = new File(caminhoImagem);
            if (file.exists()) {
                return new Image(file.toURI().toString());  // Carrega caminho absoluto
            } else {
                return new Image(Objects.requireNonNull(getClass().getResourceAsStream(caminhoImagem))); // Tenta carregar do classpath
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem: " + e.getMessage());
            return carregarImagemPadrao();
        }
    }



    private Image carregarImagemPadrao() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/br/ufrpe/timeshare/gui/application/images/picture.png")));
    }
}
