package br.ufrpe.timeshare.gui.controllers.usuarioComum.telaReservas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.gui.controllers.basico.ControllerBase;
import br.ufrpe.timeshare.gui.controllers.celulas.ControllerItemCellCotaReserva;
import br.ufrpe.timeshare.gui.controllers.celulas.ControllerItemCellBemReserva;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.beans.Bem;
import br.ufrpe.timeshare.negocio.beans.Cota;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.util.List;

public class ControllerReservasComum implements ControllerBase {

    @FXML
    private ListView<Bem> todosBensListView;
    @FXML
    private TextField buscarTodosBensTextField;
    @FXML
    private ListView<Cota> minhasCotasListView;
    @FXML
    private TabPane tabPaneUsuarioComumTelaReseva;
    @FXML
    private Tab tabBensDisponiveis;
    @FXML
    private Tab tabMinhasCotas;

    private ControladorBens controladorBens;
    private Usuario usuarioLogado;

    @FXML
    public void initialize() {
        this.controladorBens = new ControladorBens();
    }

    @Override
    public void receiveData(Object data) {
        System.out.println("receiveData chamado com: " + data);
        if (data instanceof Usuario) {
            this.usuarioLogado = (Usuario) data;
            limparMinhasCotas();
            exibirBensIniciais();
            System.out.println("Usuário definido: " + usuarioLogado.getNome());
        } else {
            System.err.println("Erro: receiveData recebeu um objeto inválido.");
        }
    }

    //para mensagem de erro
    private void exibirAlertaErro(String titulo, String header, String contentText) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(header);
        alerta.setContentText(contentText);
        alerta.getDialogPane().setStyle("-fx-background-color:  #ffcccc;"); // Vermelho claro
        alerta.showAndWait();
    }

    private void configurarListViewTodosBens() {
        todosBensListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Bem> call(ListView<Bem> listView) {
                return new ListCell<>() {
                    private FXMLLoader loader;
                    private VBox root;
                    private ControllerItemCellBemReserva controller;

                    @Override
                    protected void updateItem(Bem item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            if (loader == null) {
                                try {
                                    loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/ItemCellBemReserva.fxml"));
                                    root = loader.load();
                                    controller = loader.getController();
                                } catch (IOException e) {
                                    exibirAlertaErro("Erro", "Erro ao exibir bens disponíveis", e.getMessage());
                                    return;
                                }
                            }

                            // Atualiza os dados da célula
                            controller.setItem(item);

                            // Configura ação do botão
                            Button reservaButton = (Button) root.lookup("#reservaButton");
                            if (reservaButton != null) {
                                reservaButton.setOnAction(event -> controller.abrirMiniTelaReserva(event));
                            }

                            setGraphic(root);
                        }
                    }
                };
            }
        });
    }


    // Configuração para minhasCotasListView
    private void configurarListViewMinhasCotas() {
        minhasCotasListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Cota> call(ListView<Cota> listView) {
                return new ListCell<>() {
                    private FXMLLoader loader;
                    private HBox root;
                    private ControllerItemCellCotaReserva controller;

                    @Override
                    protected void updateItem(Cota item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            if (loader == null) {
                                try {
                                    loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/ItemCellCotaReserva.fxml"));
                                    root = loader.load();
                                    controller = loader.getController();

                                    // *** PASSA O usuarioLogado PARA ControllerItemCellCotaReserva ***
                                    controller.receiveData(usuarioLogado); // Linha importante!

                                } catch (IOException e) {
                                    exibirAlertaErro("Erro", "Erro ao exibir cotas", e.getMessage());
                                    return;
                                }
                            }

                            controller.setItem(item);

                            setGraphic(root);
                        }
                    }
                };
            }
        });
    }

    @FXML
    private void limparMinhasCotas() {
        minhasCotasListView.getItems().clear();
    }

    private void exibirBensIniciais() {

        if (usuarioLogado != null) {
            buscarMinhasCotas();
        } else {
            exibirAlertaErro("Erro", "Erro em exibir cotas", "usuarioLogado não foi inicializado");
        }
        buscarTodosBens();
    }

    @FXML
    private void buscarTodosBens() {
        try {
            String filtro = buscarTodosBensTextField.getText();
            List<Bem> bensOfertados;

            if (filtro == null || filtro.trim().isEmpty()) {
                bensOfertados = controladorBens.listarBensOfertados();
            } else {
                bensOfertados = controladorBens.buscarBensPorAtributo(filtro);
            }

            ObservableList<Bem> items = FXCollections.observableArrayList(bensOfertados);
            todosBensListView.setItems(items);
            configurarListViewTodosBens();
        } catch (Exception e) {
            exibirAlertaErro("Erro", "Erro ao tentar buscar bens", e.getMessage());
        }
    }

    @FXML
    private void buscarMinhasCotas() {
        try {
            List<Cota> cotas = controladorBens.listarCotasDeUmUsuario(usuarioLogado);
            if (cotas == null || cotas.isEmpty()) {
                System.out.println("Usuário não possui cotas.");
                minhasCotasListView.getItems().clear(); 
                return;
            }
            ObservableList<Cota> items = FXCollections.observableArrayList(cotas);
            minhasCotasListView.setItems(items);
            configurarListViewMinhasCotas();
        } catch (Exception e) {
            exibirAlertaErro("Erro", "Erro ao exibir cotas", e.getMessage());
        }
    }

    @FXML
    public void voltarTelaPrincipalComum(ActionEvent event) {
        ScreenManager.getInstance().showUsuarioComumPrincipalScreen();
    }

    @FXML
    public void mudarAbaTodosBens(ActionEvent event) {
        tabPaneUsuarioComumTelaReseva.getSelectionModel().select(tabBensDisponiveis);
    }

    @FXML
    public void mudarAbaMinhasCotas(ActionEvent event) {
        tabPaneUsuarioComumTelaReseva.getSelectionModel().select(tabMinhasCotas);
    }


}
