package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.negocio.beans.Cota;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ControllerItemCellCota {

    @FXML private Label itemLabelNomeBem;
    @FXML private Label itemLabelDataInicial;
    @FXML private Label itemLabelDataFinal;
    @FXML private Label itemLabelIdCota;
    @FXML private Label itemLabelPrecoCota;
    @FXML private Label itemLabelProprietarioCota;
    @FXML private Label itemLabelDisponibilidadeCompra;
    @FXML private Label itemLabelDisponibilidadeReserva;


    private Cota cota;
    private ControllerListarCotas mainControllerCotas;
    private ControllerDeslocamentoDeCotas mainControllerDeslocamentoDeCotas;
    private ControllerMinhasCotas mainControllerMinhasCotas;
    private ControllerAdicionarCotaPopUp mainControllerAdicionarCotaPopUp;

    public void setItem(Cota item) {
        this.cota = item;
        itemLabelIdCota.setText(String.valueOf(item.getId()));
        itemLabelNomeBem.setText(item.getBemAssociado() != null ? item.getBemAssociado().getNome() : "Nome não disponível");
        itemLabelDataInicial.setText(item.getDataInicio() != null ? item.getDataInicio().toLocalDate().toString() : "Não disponível");
        itemLabelDataFinal.setText(item.getDataFim() != null ? item.getDataFim().toLocalDate().toString() : "Não disponível");
        itemLabelPrecoCota.setText(String.valueOf(item.getPreco()));
        itemLabelProprietarioCota.setText(item.getProprietario() != null ? item.getProprietario().getNome() : "Não disponível");
        itemLabelDisponibilidadeCompra.setText(item.getStatusDeDisponibilidadeParaCompra() ? "Disponível" : "Indisponível");
        itemLabelDisponibilidadeReserva.setText(item.getStatusDeDisponibilidadeParaReserva() ? "Disponível" : "Indisponível");

    }

    public void setMainControllerCotas(ControllerListarCotas mainControllerCotas) {
        this.mainControllerCotas = mainControllerCotas;
    }

    public void setMainControllerCotasDeslocamento(ControllerDeslocamentoDeCotas mainControllerCota) {
        this.mainControllerDeslocamentoDeCotas = mainControllerCota;
    }

    public void setMainControllerMinhasCotas(ControllerMinhasCotas mainControllerCota) {
        this.mainControllerMinhasCotas = mainControllerCota;
    }

    public void setMainControllerAdicionarCotaPopUp(ControllerAdicionarCotaPopUp mainControllerAdicionarCotaPopUp) {
        this.mainControllerAdicionarCotaPopUp = mainControllerAdicionarCotaPopUp;
    }

    public void adicionarCotaCarrinhoVenda (ActionEvent event) {
        mainControllerAdicionarCotaPopUp.adicionarCotaCarrinhoVenda();
    }


}
