package br.ufrpe.timeshare.gui.controllers.celulas;

import br.ufrpe.timeshare.excecoes.*;
import br.ufrpe.timeshare.negocio.ControladorReservas;
import br.ufrpe.timeshare.negocio.beans.Estadia;
import br.ufrpe.timeshare.negocio.beans.Reserva;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.util.Optional;

public class ControllerItemCellEstadiaAdm {
    private final ControladorReservas controladorReservas;
    private Estadia estadia;
    @FXML
    private Label idReserva;
    @FXML
    private Label itemLabelNomeBem;
    @FXML
    private Label dataInicio;
    @FXML
    private Label dataFim;
    @FXML
    private Label nomeProprietario;

    public ControllerItemCellEstadiaAdm() {
        this.controladorReservas = new ControladorReservas();
    }

    public void setItem(Estadia item) {
        this.estadia = item;


        itemLabelNomeBem.setText(item.getReserva().getBem() != null ? item.getReserva().getBem().getNome() : "Nome do bem não disponível");
        nomeProprietario.setText(item.getReserva().getUsuarioComum() != null ? item.getReserva().getUsuarioComum().getNome() : "Nome do proprietario não disponível");
        idReserva.setText(item.getId() != 0 ? String.valueOf(item.getId()) : "0");
        dataInicio.setText(item.getDataInicio() != null ? item.getDataInicio().toLocalDate().toString() : "Data inicial não disponível");
        dataFim.setText(item.getDataFim() != null ? item.getDataFim().toLocalDate().toString() : "Em andamento");

        }
}
