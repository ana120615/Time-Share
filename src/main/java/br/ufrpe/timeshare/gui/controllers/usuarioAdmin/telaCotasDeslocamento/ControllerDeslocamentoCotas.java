package br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaCotasDeslocamento;

import br.ufrpe.timeshare.excecoes.BemNaoExisteException;
import br.ufrpe.timeshare.excecoes.DadosInsuficientesException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.gui.controllers.basico.ControllerBase;
import br.ufrpe.timeshare.gui.controllers.celulas.ControllerItemCellBem;
import br.ufrpe.timeshare.gui.controllers.celulas.ControllerItemCellCota;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.beans.Bem;
import br.ufrpe.timeshare.negocio.beans.Cota;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerDeslocamentoCotas implements ControllerBase {
    private Usuario usuario;
    private final ControladorBens controladorBens;

    public ControllerDeslocamentoCotas() {
        this.controladorBens = new ControladorBens();
    }

    @FXML
    private ListView<Bem> listViewItens;
    @FXML
    private TextField nomeBemProcurado;
    @FXML
    private ListView<Cota> listViewCotas;
    @FXML
    private TabPane tabPaneTelaDeslocarCotasPrincipal;
    @FXML
    private Tab tabBens;
    @FXML
    private Tab tabCotas;

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
                                controller.setValorTela(2);
                                controller.setItem(item);
                                controller.setMainControllerDeslocamento(ControllerDeslocamentoCotas.this); // Passa a referência do controlador principal
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

    public TabPane getTabPaneTelaDeslocarCotasPrincipal() {
        return tabPaneTelaDeslocarCotasPrincipal;
    }

    public void carregarCotasDoBem(Bem bem) {
        if (bem == null) {
            System.err.println("Erro: Bem está null em carregarCotasDoBem()!");
            return;
        }

        // Limpar a ListView antes de carregar novos itens
        listViewCotas.getItems().clear();
        List<Cota> cotas = new ArrayList<>();

        try {
            cotas = controladorBens.listarCotasDeUmBem((int) bem.getId());
        } catch (BemNaoExisteException e) {
            exibirAlertaErro("Erro", "Erro ao exibir cotas", e.getMessage());
        }

        if (cotas == null || cotas.isEmpty()) {
            System.err.println("Erro: lista de cotas vazia ou null.");
            return;
        } else {
            System.out.println("Lista de cotas carregada com " + cotas.size() + " itens.");
        }

        ObservableList<Cota> listaDeCotas = FXCollections.observableArrayList(cotas);
        listViewCotas.getItems().setAll(listaDeCotas);

        listViewCotas.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Cota> call(ListView<Cota> cotaListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Cota item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/ItemCellCota.fxml"));
                                HBox root = loader.load();
                                ControllerItemCellCota controller = loader.getController();
                                controller.setValorTelaDeDeslocamento(2);
                                controller.setItem(item);
                                controller.setMainControllerDeslocamentoCotas(ControllerDeslocamentoCotas.this);
                                setGraphic(root);
                            } catch (IOException e) {
                                System.err.println("Erro ao carregar ItemCellCota.fxml: " + e.getMessage());
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
    public void mudarTabBens(ActionEvent event) {
        tabPaneTelaDeslocarCotasPrincipal.getSelectionModel().select(tabBens);
    }

    @FXML
    public void mudarTabCotas(Bem bem) {
        carregarCotasDoBem(bem);
        tabPaneTelaDeslocarCotasPrincipal.getSelectionModel().select(tabCotas);
    }

    @FXML
    public void voltarParaTelaAdm(ActionEvent event) {
        nomeBemProcurado.clear();
        listViewItens.getItems().clear();
        System.out.println("Botão voltar clicado.");
        ScreenManager.getInstance().showAdmPrincipalScreen();
    }
}
