package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.negocio.beans.Bem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.File;
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
    }

    public void setMainControllerVenda(ControllerTelaDeVenda mainControllerVenda) {
        this.mainControllerVenda = mainControllerVenda;
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
