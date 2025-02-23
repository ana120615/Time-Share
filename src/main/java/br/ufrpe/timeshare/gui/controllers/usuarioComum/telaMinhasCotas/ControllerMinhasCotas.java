package br.ufrpe.timeshare.gui.controllers.usuarioComum.telaMinhasCotas;

import br.ufrpe.timeshare.excecoes.BemNaoExisteException;
import br.ufrpe.timeshare.excecoes.UsuarioNaoExisteException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.gui.controllers.basico.ControllerBase;
import br.ufrpe.timeshare.gui.controllers.celulas.ControllerItemCellCota;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.beans.Cota;
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
import java.util.List;

public class ControllerMinhasCotas implements ControllerBase {

    @FXML
    private ListView<Cota> listViewItensCotas;

    private Usuario usuario;
    private final ControladorBens controladorBens;

    public ControllerMinhasCotas() {
        this.controladorBens = new ControladorBens();
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
        carregarListaDeCotas();
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
    public void initialize() {
        System.out.println("initialize() chamado.");
    }

    public void carregarListaDeCotas() {
        if (usuario == null) {
            System.err.println("Erro: Usuário está null em carregarListaDeCotas()!");
            return;
        }

        // limpar a ListView antes de carregar novos itens
        listViewItensCotas.getItems().clear();

        List<Cota> cotas;
        try {
            cotas = controladorBens.listarCotasDeUmUsuario(usuario);
        } catch (BemNaoExisteException | UsuarioNaoExisteException e) {
            System.err.println("Bem com este id nao existe!");
            exibirAlertaErro("Erro", "Erro ao procurar bem", "Bem com este id nao existe!");
            return;
        }

        if (cotas == null || cotas.isEmpty()) {
            System.err.println("Erro: lista de bens vazia ou null.");
            return;
        } else {
            System.out.println("Lista de bens carregada com " + cotas.size() + " itens.");
        }

        ObservableList<Cota> listaDeItens = FXCollections.observableArrayList(cotas);
        listViewItensCotas.getItems().setAll(listaDeItens);

        listViewItensCotas.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Cota> call(ListView<Cota> cotaListView) {
                return new ListCell<Cota>() {
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
                                controller.setValorTelaDeDeslocamento(3);
                                controller.setItem(item);
                                controller.setMainControllerMinhasCotas(ControllerMinhasCotas.this); // Passa referência do controlador principal
                                setGraphic(root);
                            } catch (IOException e) {
                                System.err.println("Erro ao carregar ItemCellCota.fxml: " + e.getMessage());
                                e.printStackTrace();
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        });
    }

    public ListView<Cota> getListViewItens() {
        return listViewItensCotas;
    }

    @FXML
    public void irParaTelaAdm(ActionEvent event) {
        listViewItensCotas.getItems().clear();
        System.out.println("Botão voltar clicado.");
        ScreenManager.getInstance().showUsuarioComumPrincipalScreen();
    }

}
