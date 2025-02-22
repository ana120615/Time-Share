package br.ufrpe.timeshare.gui.controllers.celulas;

import br.ufrpe.timeshare.gui.controllers.usuarioComum.telaVendaCotas.ControllerAdicionarCotaPopUp;
import br.ufrpe.timeshare.gui.controllers.usuarioComum.telaVendaCotas.ControllerTelaDeVenda;
import br.ufrpe.timeshare.negocio.beans.Cota;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ControllerItemCellCotaVenda {
    private Usuario usuario;

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
    private Cota cota;

    private ControllerAdicionarCotaPopUp mainControllerAdicionarCotaPopUp;
    private ControllerTelaDeVenda mainControllerVendaCotas;
    private int telaVenda;

    public void setItem(Cota item) {
        this.cota = item;
        itemLabelIdCota.setText(String.valueOf(item.getId()));
        itemLabelNomeBem.setText(item.getBemAssociado() != null ? item.getBemAssociado().getNome() : "Nome não disponível");
        itemLabelDataInicial.setText(item.getDataInicio() != null ? item.getDataInicio().toLocalDate().toString() : "Não disponível");
        itemLabelDataFinal.setText(item.getDataFim() != null ? item.getDataFim().toLocalDate().toString() : "Não disponível");
        itemLabelPrecoCota.setText(String.valueOf(item.getPreco()));
        itemLabelProprietarioCota.setText(item.getProprietario() != null ? item.getProprietario().getNome() : "Não disponível");
        itemLabelDisponibilidadeCompra.setText(item.getStatusDeDisponibilidadeParaCompra() ? "Disponível" : "Indisponível");

    }

    public void setTelaVenda (int telaVenda) {
        this.telaVenda = telaVenda;
    }

    public void setMainControllerAdicionarCotaPopUp(ControllerAdicionarCotaPopUp mainControllerAdicionarCotaPopUp) {
        this.mainControllerAdicionarCotaPopUp = mainControllerAdicionarCotaPopUp;
    }

    public void setMainControllerVendaCotas(ControllerTelaDeVenda mainControllerVendaCotas) {
        this.mainControllerVendaCotas = mainControllerVendaCotas;
    }

    // ACAO DE ADICIONAR E REMOVER COTA
    @FXML
    public void addCotaCarrinho (ActionEvent event) {
        if (telaVenda == 2) {
            exibirAlertaErro("Erro", "Operação inválida", "Você não pode adicionar cota na tela principal de venda.");
        } else if (telaVenda == 1){
            mainControllerAdicionarCotaPopUp.adicionarCotaCarrinhoVenda(cota);
        }
    }

    @FXML
    public void removeCotaCarrinho (ActionEvent event) {
        if (telaVenda == 2) {
            mainControllerVendaCotas.removerCotaCarrinhoVendaTelaPrincipal(cota);
        } else if (telaVenda == 1) {
            mainControllerAdicionarCotaPopUp.removerCotaCarrinhoVenda(cota);
        }
    }

    private void exibirAlertaErro(String titulo, String header, String contentText) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(header);
        alerta.setContentText(contentText);
        alerta.getDialogPane().setStyle("-fx-background-color:  #ffcccc;");
        alerta.showAndWait();
    }

}
