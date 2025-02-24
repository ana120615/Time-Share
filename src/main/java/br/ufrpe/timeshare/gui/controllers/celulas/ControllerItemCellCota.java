package br.ufrpe.timeshare.gui.controllers.celulas;

import br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaBensCotas.ControllerDeslocamentoDeCotasPopUP;
import br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaBensCotas.ControllerTelaDeBensECotas;
import br.ufrpe.timeshare.gui.controllers.usuarioComum.telaMinhasCotas.ControllerMinhasCotas;
import br.ufrpe.timeshare.gui.controllers.usuarioComum.telaMinhasCotas.ControllerRepassarDireitoUsoPopUp;
import br.ufrpe.timeshare.gui.controllers.usuarioComum.telaVendaCotas.ControllerAdicionarCotaPopUp;
import br.ufrpe.timeshare.gui.controllers.usuarioComum.telaVendaCotas.ControllerTelaDeVenda;
import br.ufrpe.timeshare.negocio.beans.Cota;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class ControllerItemCellCota {

    @FXML
    private Label itemLabelNomeBem;
    @FXML
    private Label itemLabelDataInicial;
    @FXML
    private Label itemLabelDataFinal;
    @FXML
    private Label itemLabelIdCota;
    @FXML
    private Label itemLabelPrecoCota;
    @FXML
    private Label itemLabelProprietarioCota;
    @FXML
    private Label itemLabelDisponibilidadeCompra;
    private HBox rootCell; // Adicione essa linha
    @FXML
    private Button idButtonCelulaCota;

    private int valorTelaDeDeslocamento;
    private int valorTelaDeVenda;

    private Cota cota;
    private ControllerDeslocamentoDeCotasPopUP mainControllerDeslocamentoDeCotas;
    private ControllerMinhasCotas mainControllerMinhasCotas;
    private ControllerAdicionarCotaPopUp mainControllerAdicionarCotaPopUp;
    private ControllerTelaDeVenda mainControllerVendaCotas;
    private ControllerTelaDeBensECotas mainControllerDeslocamentoCotas;
    private ControllerRepassarDireitoUsoPopUp mainControllerRepassarDireitoUsoPopUp;

    public void setItem(Cota item) {
        this.cota = item;
        itemLabelIdCota.setText(String.valueOf(item.getId()));
        itemLabelNomeBem.setText(item.getBemAssociado() != null ? item.getBemAssociado().getNome() : "Nome não disponível");
        itemLabelDataInicial.setText(item.getDataInicio() != null ? item.getDataInicio().toLocalDate().toString() : "Não disponível");
        itemLabelDataFinal.setText(item.getDataFim() != null ? item.getDataFim().toLocalDate().toString() : "Não disponível");
        itemLabelPrecoCota.setText(String.valueOf(item.getPreco()));
        itemLabelProprietarioCota.setText(item.getProprietario() != null ? item.getProprietario().getNome() : "Não disponível");
        itemLabelDisponibilidadeCompra.setText(item.getStatusDeDisponibilidadeParaCompra() ? "Disponível" : "Indisponível");

        if (valorTelaDeDeslocamento == 2) {
            idButtonCelulaCota.setOnAction(e -> showPopupDeslocamentoCotas()); // Agora chama o pop-up ao clicar no botão
        }
        if (valorTelaDeDeslocamento == 3) {
            idButtonCelulaCota.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirme a Ação");
                alert.setHeaderText("O que deseja fazer?");
                alert.setContentText("Escolha entre as opções:");

                ButtonType botaoDeslocar = new ButtonType("Deslocar Cota", ButtonBar.ButtonData.YES);
                ButtonType botaoRepassar = new ButtonType("Repassar Direito de Uso", ButtonBar.ButtonData.NO);
                ButtonType botaoCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(botaoDeslocar, botaoRepassar, botaoCancelar);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent()) {
                    if (result.get() == botaoDeslocar) {
                        showPopupDeslocamentoCotas();
                    } else if (result.get() == botaoRepassar) {
                        showPopupRepassarDireitoUso();
                    } else {
                        alert.close();
                    }
                }
            });
        }


        if (valorTelaDeVenda == 1) { // ADICIONAR A COTA NO CARRINHO
            idButtonCelulaCota.setOnAction(e -> {
                mainControllerAdicionarCotaPopUp.adicionarCotaCarrinhoVenda(cota);
            });

        }
    }


    public void setMainControllerVendaCotas(ControllerTelaDeVenda mainControllerVendaCotas) {
        this.mainControllerVendaCotas = mainControllerVendaCotas;
    }

    public void setMainControllerCotasDeslocamento(ControllerDeslocamentoDeCotasPopUP mainControllerCota) {
        this.mainControllerDeslocamentoDeCotas = mainControllerCota;
    }

    public void setMainControllerMinhasCotas(ControllerMinhasCotas mainControllerCota) {
        this.mainControllerMinhasCotas = mainControllerCota;
    }

    public void setMainControllerAdicionarCotaPopUp(ControllerAdicionarCotaPopUp mainControllerAdicionarCotaPopUp) {
        this.mainControllerAdicionarCotaPopUp = mainControllerAdicionarCotaPopUp;
    }

    public void setMainControllerDeslocamentoCotas(ControllerTelaDeBensECotas mainControllerDeslocamentoCotas) {
        this.mainControllerDeslocamentoCotas = mainControllerDeslocamentoCotas;
    }

    public void setMainControllerRepassarDireitoUsoPopUp(ControllerRepassarDireitoUsoPopUp mainControllerRepassarDireitoUsoPopUp) {
        this.mainControllerRepassarDireitoUsoPopUp = mainControllerRepassarDireitoUsoPopUp;
    }


    //SET VALORES DE TELA
    public void setValorTelaDeDeslocamento(int valorTelaDeDeslocamento) {
        this.valorTelaDeDeslocamento = valorTelaDeDeslocamento;
    }

    public void setValorTelaDeVenda(int valorTelaDeVenda) {
        this.valorTelaDeVenda = valorTelaDeVenda;
    }


    //POP UPS
    private void showPopupDeslocamentoCotas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/TelaDeslocamentoCotasPopUp.fxml"));
            BorderPane popupRoot = loader.load(); // Carrega a interface corretamente

            // Obtém o controlador da tela do pop-up
            ControllerDeslocamentoDeCotasPopUP popupController = loader.getController();
            popupController.setCota(cota);
            popupController.setMainController(mainControllerDeslocamentoCotas);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(new Scene(popupRoot));
            popupStage.setTitle("Detalhes do Bem");
            popupStage.setMinWidth(932);
            popupStage.setMinHeight(650);

            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPopupRepassarDireitoUso() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/RepassarDireitoUsoPopUp.fxml"));
            StackPane popupRoot = loader.load(); // Carrega a interface corretamente

            // Obtém o controlador da tela do pop-up
            ControllerRepassarDireitoUsoPopUp popupController = loader.getController();
            popupController.setCota(cota);
            popupController.setMainController(mainControllerMinhasCotas);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(new Scene(popupRoot));
            popupStage.setTitle("Detalhes do Bem");
            popupStage.setMinWidth(932);
            popupStage.setMinHeight(650);

            // Aplica efeito de desfoque na tela principal
            if (mainControllerMinhasCotas != null && mainControllerMinhasCotas.getListViewItens().getScene() != null) {
                mainControllerMinhasCotas.getListViewItens().getScene().getRoot().setEffect(new GaussianBlur(10));
            }

            popupStage.showAndWait();

            // Remove efeito de desfoque ao fechar
            if (mainControllerMinhasCotas != null && mainControllerMinhasCotas.getListViewItens().getScene() != null) {
                mainControllerMinhasCotas.getListViewItens().getScene().getRoot().setEffect(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
