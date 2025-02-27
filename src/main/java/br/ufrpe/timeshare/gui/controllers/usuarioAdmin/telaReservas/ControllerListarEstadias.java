package br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaReservas;

import br.ufrpe.timeshare.excecoes.DadosInsuficientesException;
import br.ufrpe.timeshare.excecoes.OperacaoNaoPermitidaException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.gui.controllers.basico.ControllerBase;
import br.ufrpe.timeshare.gui.controllers.celulas.ControllerItemCellEstadiaAdm;
import br.ufrpe.timeshare.negocio.ControladorReservas;
import br.ufrpe.timeshare.negocio.beans.Estadia;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerListarEstadias implements ControllerBase {
    private Usuario usuario;
    private final ControladorReservas controladorReservas;
    @FXML
    private ListView<Estadia> listViewItensEstadias;

    public ControllerListarEstadias() {
        this.controladorReservas = new ControladorReservas();
    }

    @Override
    public void receiveData(Object data) {
        System.out.println("receiveData chamado com: " + data);
        if (data instanceof Usuario) {
            this.usuario = (Usuario) data;
            System.out.println("Usuário definido: " + usuario.getNome());

        } else {
            System.err.println("Erro: receiveData recebeu um objeto inválido.");
        }
        carregarListaDeEstadias();
    }

    @FXML
    public void initialize() {
        System.out.println("initialize() chamado.");
    }

    private void exibirAlertaErro(String titulo, String header, String contentText) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(header);
        alerta.setContentText(contentText);
        alerta.getDialogPane().setStyle("-fx-background-color:  #ffcccc;"); // Vermelho claro
        alerta.showAndWait();
    }

    public void carregarListaDeEstadias() {
        if (usuario == null) {
            exibirAlertaErro("Erro", "Problema ao carregar lista de estadias", "O usuário está null");
            return;
        }

        // limpar a ListView antes de carregar novos itens
        listViewItensEstadias.getItems().clear();
        List<Estadia> estadias = new ArrayList<>();
        try {
            estadias = controladorReservas.getEstadiasDosBensDoUsuarioAdm(usuario);
        } catch (OperacaoNaoPermitidaException | DadosInsuficientesException e) {
            exibirAlertaErro("Erro", "Problema ao carregar lista de estadias", e.getMessage());
        }


        ObservableList<Estadia> listaDeItens = FXCollections.observableArrayList(estadias);
        listViewItensEstadias.getItems().setAll(listaDeItens);

        listViewItensEstadias.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Estadia> call(ListView<Estadia> EstadiasListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Estadia item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/ItemCellEstadiaAdmin.fxml"));
                                HBox root = loader.load();
                                ControllerItemCellEstadiaAdm controller = loader.getController();
                                controller.setItem(item);
                                setGraphic(root);

                            } catch (IOException e) {
                                System.err.println("Erro ao carregar ItemCell.fxml: " + e.getMessage());
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        });
    }

    public void voltarParaTelaAdm(ActionEvent event) {
        ScreenManager.getInstance().showAdmPrincipalScreen();
    }
}
