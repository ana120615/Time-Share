package br.ufrpe.timeshare.gui.controllers.celulas;

import br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaBensCotas.ControllerTelaDeBensECotas;
import br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaBensCotas.ControllerEditarBemPopUp;
import br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaReservas.ControllerListarReservas;
import br.ufrpe.timeshare.negocio.beans.Bem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class ControllerItemCellBem {

    @FXML
    private Button itemButton;
    @FXML
    private ImageView itemImage;
    @FXML
    private Label itemLabelDescricao;
    @FXML
    private Label itemLabelNome;
    @FXML
    private Label itemLabelQuantidadeCotas;
    @FXML
    private Label itemLabelLocalizacao;

    private Bem bem;
    private int valorTela;
    private ControllerTelaDeBensECotas mainControllerDeslocamento;
    private ControllerListarReservas mainControllerListarReservas;


    public void setValorTela(int valorTela) {
        this.valorTela = valorTela;
    }

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
        itemLabelQuantidadeCotas.setText(item.getCotas() != null ? String.valueOf(item.getCotas().size()) : "0");
        itemLabelLocalizacao.setText(item.getLocalizacao() != null ? item.getLocalizacao() : "Localização não disponível");

        if (valorTela == 1) {
            itemButton.setOnAction(e -> mainControllerListarReservas.carregarReservasBem(this.bem)); // Agora chama o pop-up ao clicar no botão
        } else if (valorTela == 2) {
            itemButton.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirme a Ação");
                alert.setHeaderText("O que deseja fazer?");
                alert.setContentText("Escolha entre as opções:");

                ButtonType botaoCotas = new ButtonType("Mostrar Cotas", ButtonBar.ButtonData.YES);
                ButtonType botaoEditar = new ButtonType("Editar Bem", ButtonBar.ButtonData.NO);
                ButtonType botaoCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(botaoCotas, botaoEditar, botaoCancelar);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent()) {
                    if (result.get() == botaoCotas) {
                        mainControllerDeslocamento.mudarTabCotas(bem);
                    } else if (result.get() == botaoEditar) {
                        showPopup();
                    } else {
                        alert.close();
                    }
                }
            });
        }
    }


    public void setMainControllerDeslocamento(ControllerTelaDeBensECotas mainControllerDeslocamento) {
        this.mainControllerDeslocamento = mainControllerDeslocamento;
    }

    public void setMainControllerListarReservas(ControllerListarReservas mainControllerListarReservas) {
        this.mainControllerListarReservas = mainControllerListarReservas;
    }

    private void showPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/EditarBemPopUp.fxml"));
            StackPane popupRoot = loader.load(); // Carrega a interface corretamente

            // Obtém o controlador da tela do pop-up
            ControllerEditarBemPopUp popupController = loader.getController();
            popupController.setBem(bem);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(new Scene(popupRoot));
            popupStage.setTitle("Detalhes do Bem");
            popupStage.setMinWidth(932);
            popupStage.setMinHeight(650);

            // Aplica efeito de desfoque na tela principal

            popupStage.showAndWait();

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
