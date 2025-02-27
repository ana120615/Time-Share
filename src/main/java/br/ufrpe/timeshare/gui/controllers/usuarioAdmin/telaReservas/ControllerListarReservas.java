package br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaReservas;

import br.ufrpe.timeshare.excecoes.DadosInsuficientesException;
import br.ufrpe.timeshare.excecoes.OperacaoNaoPermitidaException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.gui.controllers.basico.ControllerBase;
import br.ufrpe.timeshare.gui.controllers.celulas.ControllerItemCellBem;
import br.ufrpe.timeshare.gui.controllers.celulas.ControllerItemCellReservaAdm;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.ControladorReservas;
import br.ufrpe.timeshare.negocio.beans.Bem;
import br.ufrpe.timeshare.negocio.beans.Estadia;
import br.ufrpe.timeshare.negocio.beans.Reserva;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerListarReservas implements ControllerBase {
    private Usuario usuario;
    private final ControladorReservas controladorReservas;
    private final ControladorBens controladorBens;

    public ControllerListarReservas() {
        this.controladorReservas = new ControladorReservas();
        this.controladorBens = new ControladorBens();
    }

    @FXML
    private ListView<Bem> listViewItens;
    @FXML
    private TextField nomeBemProcurado;
    @FXML
    private ListView<Reserva> listViewReservasDeUmBem;
    @FXML
    private ListView<Estadia> listViewReservasConcluidas;
    @FXML
    private TabPane tabPaneTelaReservasAdmPrincipal;
    @FXML
    private Tab tabReservasPrincipal;
    @FXML
    private Tab tabReservasDeUmBem;

    @Override
    public void receiveData(Object data) {
        System.out.println("receiveData chamado com: " + data);
        if (data instanceof Usuario) {
            this.usuario = (Usuario) data;
            System.out.println("Usuário definido: " + usuario.getNome());
            carregarListaDeBens();
        } else {
            System.err.println("Erro: receiveData recebeu um objeto inválido.");
        }
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

    @FXML
    public void buscarBens(ActionEvent event) {
        carregarListaDeBens();
    }

    public void carregarListaDeBens() {
        if (usuario == null) {
            System.err.println("Erro: Usuário está null em carregarListaDeBens()!");
            return;
        }

        // limpar a ListView antes de carregar novos itens
        listViewItens.getItems().clear();

        List<Bem> bens = new ArrayList<>();
        if (!nomeBemProcurado.getText().isEmpty()) {
            try {
                bens = controladorBens.listarBensUsuarioPorNome(nomeBemProcurado.getText().trim(), usuario.getId());
            } catch (DadosInsuficientesException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                bens = controladorBens.listarBensUsuario(usuario);
            } catch (DadosInsuficientesException e) {
                System.out.println(e.getMessage());
            }
        }

        if (bens == null || bens.isEmpty()) {
            System.err.println("Erro: lista de bens vazia ou null.");
            return;
        } else {
            System.out.println("Lista de bens carregada com " + bens.size() + " itens.");
        }

        ObservableList<Bem> listaDeItens = FXCollections.observableArrayList(bens);
        listViewItens.getItems().setAll(listaDeItens);

        listViewItens.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Bem> call(ListView<Bem> bemListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Bem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/ItemCell.fxml"));
                                HBox root = loader.load();
                                ControllerItemCellBem controller = loader.getController();
                                controller.setValorTela(1);
                                controller.setItem(item);
                                controller.setMainControllerListarReservas(ControllerListarReservas.this); // Passa a referência do controlador principal
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


    public void carregarReservasBem(Bem bem) {
        if (bem == null) {
            System.err.println("Erro: Bem está null em carregarCotasDoBem()!");
            return;
        }

        // Limpar a ListView antes de carregar novos itens
        listViewReservasDeUmBem.getItems().clear();
        List<Reserva> reservas = new ArrayList<>();

        try {
            reservas = controladorReservas.buscarReservaPorBem(bem.getId());
        } catch (OperacaoNaoPermitidaException e) {
            exibirAlertaErro("Erro", "Erro ao exibir reservas", e.getMessage());
        }

        if (reservas == null || reservas.isEmpty()) {
            System.err.println("lista de reservas vazia ou null.");
        } else {
            System.out.println("Lista de reservas carregada com " + reservas.size() + " itens.");
        }

        ObservableList<Reserva> listaDeCotas = FXCollections.observableArrayList(reservas);
        listViewReservasDeUmBem.getItems().setAll(listaDeCotas);

        listViewReservasDeUmBem.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Reserva> call(ListView<Reserva> cotaListView) {
                return new ListCell<Reserva>() {
                    protected void updateItem(Reserva item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/ItemCellReservasAdmin.fxml"));
                                HBox root = loader.load();
                                ControllerItemCellReservaAdm controller = loader.getController();
                                controller.setValorTela(1);
                                controller.setItem(item);
                                controller.setMainControllerListarReservas(ControllerListarReservas.this);
                                setGraphic(root);
                            } catch (IOException e) {
                                System.err.println("Erro ao carregar ItemCellCotaReservaAdmin.fxml: " + e.getMessage());
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        });
        mudarTabReservasDeUmBem(); // ir para tab reservas de um bem
    }

    public ListView<Bem> getListViewItens() {
        return listViewItens;
    }

    @FXML
    public void mudarTabReservasPrincipal(ActionEvent event) {
        tabPaneTelaReservasAdmPrincipal.getSelectionModel().select(tabReservasPrincipal);
    }

    public void mudarTabReservasDeUmBem() {
        tabPaneTelaReservasAdmPrincipal.getSelectionModel().select(tabReservasDeUmBem);
    }

    public void mudarTabReservasAndamento() {

    }

    @FXML
    public void voltarParaTelaAdm(ActionEvent event) {
        nomeBemProcurado.clear();
        listViewItens.getItems().clear();
        System.out.println("Botão voltar clicado.");
        tabPaneTelaReservasAdmPrincipal.getSelectionModel().select(tabReservasPrincipal);
        ScreenManager.getInstance().showAdmPrincipalScreen();
    }

}
