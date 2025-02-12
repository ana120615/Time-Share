package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.negocio.beans.Bem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.Objects;

public class ControllerItemCell {

    @FXML
    private Button itemButton;

    @FXML
    private ImageView itemImage;

    @FXML
    private Label itemLabelDescricao;

    @FXML
    private Label itemLabelNome;

    public void setItem(Bem item) {
        if (item == null) {
            itemLabelNome.setText("Item não encontrado");
            itemLabelDescricao.setText("Descrição não disponível");
            itemImage.setImage(carregarImagemPadrao());
            return;
        }

        itemLabelNome.setText(item.getNome() != null ? item.getNome() : "Nome não disponível");
        itemLabelDescricao.setText(item.getDescricao() != null ? item.getDescricao() : "Descrição não disponível");

        itemImage.setImage(carregarImagem(item.getCaminhoImagem()));

        itemButton.setOnAction(e -> {
            System.out.println("Clicou em: " + item.getNome());
        });
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
