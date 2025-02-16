package br.ufrpe.timeshare.gui.controllers.celulas;

import br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaCotas.ControllerDeslocamentoDeCotasPopUP;
import br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaCotas.ControllerTelaDeCotas;
import br.ufrpe.timeshare.gui.controllers.usuarioComum.telaMinhasCotas.ControllerMinhasCotas;
import br.ufrpe.timeshare.gui.controllers.usuarioComum.telaVendaCotas.ControllerAdicionarCotaPopUp;
import br.ufrpe.timeshare.gui.controllers.usuarioComum.telaVendaCotas.ControllerTelaDeVenda;
import br.ufrpe.timeshare.negocio.beans.Cota;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
    private ControllerTelaDeCotas mainControllerDeslocamentoCotas;

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
        } else if (valorTelaDeDeslocamento == 3) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirme a Acao");
            alert.setHeaderText("O que deseja fazer?");
            alert.setContentText("Escolha entre as opcoes");

            // Criando os botões personalizados
            ButtonType botaoSim = new ButtonType("Deslocar cota", ButtonBar.ButtonData.YES);
            ButtonType botaoNao = new ButtonType("Repassar direito de uso", ButtonBar.ButtonData.NO);
            ButtonType botaoTalvez = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

            // Adicionando os botões ao alerta
            alert.getButtonTypes().setAll(botaoSim, botaoNao, botaoTalvez);

            // Exibindo o alerta e capturando a resposta
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == botaoSim) {
                idButtonCelulaCota.setOnAction(e -> showPopupDeslocamentoCotas()); // Agora chama o pop-up ao clicar no botão
            } else if (result.isPresent() && result.get() == botaoNao) {
                //idButtonCelulaCota.setOnAction(e -> showPopupDeslocamentoCotas()); // Agora chama o pop-up ao clicar no botão
            } else {
                alert.close();
            }
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

    public void setMainControllerDeslocamentoCotas(ControllerTelaDeCotas mainControllerDeslocamentoCotas) {
        this.mainControllerDeslocamentoCotas = mainControllerDeslocamentoCotas;
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

}
