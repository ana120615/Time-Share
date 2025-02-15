package br.ufrpe.timeshare.gui.controllers.celulas;

import br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaCotasDeslocamento.ControllerDeslocamentoDeCotasPopUP;
import br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaListarCotas.ControllerListarCotas;
import br.ufrpe.timeshare.gui.controllers.usuarioComum.telaMinhasCotas.ControllerMinhasCotas;
import br.ufrpe.timeshare.gui.controllers.usuarioComum.telaVendaCotas.ControllerAdicionarCotaPopUp;
import br.ufrpe.timeshare.gui.controllers.usuarioComum.telaVendaCotas.ControllerComprarCota;
import br.ufrpe.timeshare.gui.controllers.usuarioComum.telaVendaCotas.ControllerTelaDeVenda;
import br.ufrpe.timeshare.negocio.beans.Cota;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ControllerItemCellCota {

    @FXML private Label itemLabelNomeBem;
    @FXML private Label itemLabelDataInicial;
    @FXML private Label itemLabelDataFinal;
    @FXML private Label itemLabelIdCota;
    @FXML private Label itemLabelPrecoCota;
    @FXML private Label itemLabelProprietarioCota;
    @FXML private Label itemLabelDisponibilidadeCompra;

    @FXML private HBox rootCell; // Adicione essa linha

    private Cota cota;
    private ControllerListarCotas mainControllerCotas;
    private ControllerDeslocamentoDeCotasPopUP mainControllerDeslocamentoDeCotas;
    private ControllerMinhasCotas mainControllerMinhasCotas;
    private ControllerAdicionarCotaPopUp mainControllerAdicionarCotaPopUp;
    private ControllerComprarCota mainControllerComprarCota;
    private ControllerTelaDeVenda mainControllerVendaCotas;

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

    public void setMainControllerCotas(ControllerListarCotas mainControllerCotas) {
        this.mainControllerCotas = mainControllerCotas;
    }

    public void setMainControllerVendaCotas (ControllerTelaDeVenda mainControllerVendaCotas) {
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

    public void onActionadicionarCotaCarrinhoVenda (ActionEvent event) {
        mainControllerAdicionarCotaPopUp.adicionarCotaCarrinhoVenda(this.cota);
    }

    public void setMainControllerComprarCota (ControllerComprarCota mainControllerComprarCota) {
        this.mainControllerComprarCota = mainControllerComprarCota;
    }


}
