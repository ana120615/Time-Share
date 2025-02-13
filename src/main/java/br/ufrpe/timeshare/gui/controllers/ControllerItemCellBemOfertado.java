package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.negocio.beans.Bem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ControllerItemCellBemOfertado {

    @FXML
    private Label nomeDoBem;
    @FXML
    private Label cotasDisponiveis;
    @FXML
    private Label precoCota;
    @FXML
    private ImageView itemImage;
    @FXML
    private Button itemButton;


    private Bem bem;
    private ControllerTelaDeVenda mainControllerVenda;

    public void setItem(Bem item) {
        this.bem = item;

        if (item == null) {
            nomeDoBem.setText("Item não encontrado");
            cotasDisponiveis.setText("Descrição não disponível");
            precoCota.setText("Preço não disponível");
            itemImage.setImage(carregarImagemPadrao());
            return;
        }

        nomeDoBem.setText(item.getNome() != null ? item.getNome() : "Nome não disponível");
        cotasDisponiveis.setText(item.getDescricao() != null ? item.getDescricao() : "Descrição não disponível");
        itemImage.setImage(carregarImagem(item.getCaminhoImagem()));
        precoCota.setText(item.getCotas() != null ? String.valueOf(item.getCotas().getFirst().getPreco()) : "Preço não disponível");

        // Verifique se itemButton foi corretamente injetado antes de definir ação
        if (itemButton != null) {
            itemButton.setOnAction(e -> showPopupAdicionarCotaCarrinho());
        } else {
            System.err.println("Erro: itemButton está nulo!");
        }
    }

    public void setMainControllerVenda(ControllerTelaDeVenda mainControllerVenda) {
        this.mainControllerVenda = mainControllerVenda;
    }

    private void showPopupAdicionarCotaCarrinho() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/AdicionarCotaDeUmBemPopUp.fxml"));
            StackPane popupRoot = loader.load(); // Carrega a interface corretamente

            // Obtém o controlador da tela do pop-up
            ControllerAdicionarCotaPopUp popupController = loader.getController();
            popupController.setBem(bem);
            popupController.setMainControllerVenda(mainControllerVenda);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(new Scene(popupRoot));
            popupStage.setTitle("Detalhes do Bem");
            popupStage.setMinWidth(932);
            popupStage.setMinHeight(650);

            // Aplica efeito de desfoque na tela principal
            if (mainControllerVenda != null && mainControllerVenda.getListViewItens().getScene() != null) {
                mainControllerVenda.getListViewItens().getScene().getRoot().setEffect(new GaussianBlur(10));
            }

            popupStage.showAndWait();

            // Remove efeito de desfoque ao fechar
            if (mainControllerVenda != null && mainControllerVenda.getListViewItens().getScene() != null) {
                mainControllerVenda.getListViewItens().getScene().getRoot().setEffect(null);
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
