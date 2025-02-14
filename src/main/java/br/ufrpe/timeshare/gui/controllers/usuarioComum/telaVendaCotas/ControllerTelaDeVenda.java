package br.ufrpe.timeshare.gui.controllers.usuarioComum.telaVendaCotas;

import br.ufrpe.timeshare.excecoes.UsuarioNaoExisteException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.gui.controllers.basico.ControllerBase;
import br.ufrpe.timeshare.gui.controllers.celulas.ControllerItemCellBemOfertado;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.ControladorVendas;
import br.ufrpe.timeshare.negocio.beans.Bem;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import br.ufrpe.timeshare.negocio.beans.Venda;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class ControllerTelaDeVenda implements ControllerBase {
    private Usuario usuario;
    private Venda vendaAtual;
    private final ControladorBens controladorBens;
    private final ControladorVendas controladorVendas;

    @FXML
    private TabPane tabPaneUsuarioComumTelaVenda; // Estado inicial
    @FXML
    private Tab tabBensOfertados;
    @FXML
    private Tab tabCarrinho;
    @FXML
    private ListView<Bem> listViewItens;
    private ControllerTelaDeVenda mainControllerVenda;

    public ControllerTelaDeVenda() {
        this.controladorBens = new ControladorBens();
        this.controladorVendas = new ControladorVendas();
    }

    @Override
    public void receiveData(Object data) {
        System.out.println("receiveData chamado com: " + data);
        if (data instanceof Usuario) {
            this.usuario = (Usuario) data;
            System.out.println("Usuário definido: " + usuario.getNome());
            carregarListaDeBens();
            try {
                this.vendaAtual = controladorVendas.iniciarVenda((int) usuario.getId());
            } catch (UsuarioNaoExisteException e) {
                System.out.println(e.getMessage());
            }

        } else {
            System.err.println("Erro: receiveData recebeu um objeto inválido.");
        }


    }

    @FXML
    public void initialize() {
        System.out.println("initialize() chamado.");
    }

    public void setMainControllerVenda(ControllerTelaDeVenda mainControllerVenda) {
        this.mainControllerVenda = mainControllerVenda;
    }
    private void carregarListaDeBens() {
        if (usuario == null) {
            System.err.println("Erro: Usuário está null em carregarListaDeBens()!");
            return;
        }

        List<Bem> bens = controladorBens.listarBensOfertados();
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
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/ItemCellBemOfertado.fxml"));
                                VBox root = loader.load();
                                ControllerItemCellBemOfertado controller = loader.getController();
                                controller.setItem(item);
                                controller.setMainControllerVenda(ControllerTelaDeVenda.this); // Passa referência do controlador principal
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
    public Usuario getUsuario() {
        return usuario;
    }
    public Venda getVendaAtual() {
        return vendaAtual;
    }

    @FXML
    public void irParaTelaPrincipalUsuarioComum(ActionEvent event) {
        ScreenManager.getInstance().showUsuarioComumPrincipalScreen();
    }

    @FXML
    private void mudarAbaBensOfertados(ActionEvent event) {
        tabPaneUsuarioComumTelaVenda.getSelectionModel().select(tabBensOfertados);
    }

    @FXML
    private void mudarAbaCarrinho(ActionEvent event) {
        tabPaneUsuarioComumTelaVenda.getSelectionModel().select(tabCarrinho);
    }
}
