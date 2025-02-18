package br.ufrpe.timeshare.gui.controllers.usuarioComum.telaReservas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.gui.controllers.celulas.ControllerItemCellBemCota;
import br.ufrpe.timeshare.gui.controllers.celulas.ControllerItemCellBemReserva;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.beans.Bem;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.util.Callback;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ControllerReservasComum {

    @FXML
    private ListView<Bem> todosBensListView;
    @FXML
    private TextField buscarTodosBensTextField;
    @FXML
    private Button buscarTodosBensButton;
    @FXML
    private ListView<Bem> meusBensListView;
    @FXML
    private TextField buscarMeusBensTextField;
    @FXML
    private Button buscarMeusBensButton; 
    @FXML
    private ScrollPane todosBensScrollPane;
    @FXML
    private ScrollPane meusBensScrollPane;

    private ControladorBens controladorBens;
    private Usuario usuarioLogado;

    public void inicializar() {
        this.controladorBens = new ControladorBens();

        // Usuário logado
        Object data = ScreenManager.getInstance().getData();
        if (data instanceof Usuario) {
            this.usuarioLogado = (Usuario) data;
        }

        configurarListViews();
        exibirBensIniciais(); // Exibe todos os bens ao inicializar a tela
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

    private void configurarListViews() {
        // Configuração para todosBensListView (bens ofertados)
        todosBensListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Bem> call(ListView<Bem> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Bem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/controllers/celulas/ItemCellBemReserva.fxml"));
                                HBox root = loader.load();
                                ControllerItemCellBemReserva controller = loader.getController();
                                controller.setItem(item);
                                Button reservaButton = (Button) root.lookup("#reservaButton"); 
                                reservaButton.setOnAction(event -> {
                                    controller.abrirMiniTelaReserva(event);  
                                });
    
                                setGraphic(root);
                            } catch (IOException e) {
                                exibirAlertaErro("Erro", "Erro ao exibir bens disponiveis", e.getMessage());
                            }
                        }
                    }
                };
            }
        });
    

    

        // Configuração para meusBensListView (bens com cotas)
        meusBensListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Bem> call(ListView<Bem> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Bem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/controllers/celulas/ItemCellBemCota.fxml"));
                                HBox root = loader.load();
                                ControllerItemCellBemCota controller = loader.getController();
                                controller.setItem(item);
                                setGraphic(root);
                            } catch (IOException e) {
                                exibirAlertaErro("Erro", "Erro ao exibir bens", e.getMessage());
                            }
                        }
                    }
                };
            }
        });
    }

    private void exibirBensIniciais() {
        buscarTodosBens();
        buscarMeusBens();
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
        } catch (Exception e) {
            exibirAlertaErro("Erro", "Erro ao tentar buscar bens", e.getMessage());
        }
    }

    @FXML
    private void buscarMeusBens() {
        try {
            String filtro = buscarMeusBensTextField.getText();
            List<Bem> bensComCotas;

            if (filtro == null || filtro.trim().isEmpty()) {
                bensComCotas = controladorBens.buscarBensPorProprietarioDeCotas(usuarioLogado);
            } else {
                bensComCotas = controladorBens.buscarBensPorAtributo(filtro);
            }

            ObservableList<Bem> items = FXCollections.observableArrayList(bensComCotas);
            meusBensListView.setItems(items);
        } catch (Exception e) {
            exibirAlertaErro("Erro", "Erro ao tentar buscar bens", e.getMessage());
        }
    }

}
