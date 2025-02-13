package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.excecoes.DadosInsuficientesException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.beans.Bem;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerListarBens implements ControllerBase {
    private Usuario usuario;
    private final ControladorBens controladorBens;

    public ControllerListarBens() {
        this.controladorBens = new ControladorBens();
    }

    @FXML
    private ListView<Bem> listViewItens;
    @FXML private TextField nomeBemProcurado;

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
    public void buscarBens(ActionEvent event) throws DadosInsuficientesException {
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
        if(!nomeBemProcurado.getText().isEmpty()) {
            try {
                bens = controladorBens.listarBensUsuarioPorNome(String.valueOf(nomeBemProcurado), (int)usuario.getId());
            } catch (DadosInsuficientesException e) {
                System.out.println(e.getMessage());
            }
        }

        else {
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
                                ControllerItemCell controller = loader.getController();
                                controller.setItem(item);
                                controller.setMainControllerBens(ControllerListarBens.this); // Passa referência do controlador principal
                                setGraphic(root);
                            } catch (IOException e) {
                                System.err.println("Erro ao carregar ItemCell.fxml: " + e.getMessage());
                                e.printStackTrace();
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        });
    }

    public ListView<Bem> getListViewItens() {
        return listViewItens;
    }


    @FXML
    public void voltarParaTelaAdm(ActionEvent event) {
        System.out.println("Botão voltar clicado.");
        ScreenManager.getInstance().showAdmPrincipalScreen();
    }
}
