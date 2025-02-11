package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import br.ufrpe.timeshare.negocio.beans.Bem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import java.io.IOException;
import java.util.List;

public class ControllerListarBens implements ControllerBase {
    private Usuario usuario;
    private ControladorBens controladorBens;

    public ControllerListarBens() {
        this.controladorBens = new ControladorBens();
    }

    @FXML private ListView<Bem> listViewItens;

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
        if (controladorBens == null) {
            controladorBens = new ControladorBens();
        }
    }

    private void carregarListaDeBens() {
        if (usuario == null) {
            System.err.println("Erro: Usuário está null em carregarListaDeBens()!");
            return;
        }

        List<Bem> bens = controladorBens.listarBensUsuario(usuario);
        if (bens == null || bens.isEmpty()) {
            System.err.println("Erro: lista de bens vazia ou null.");
            return;
        } else {
            System.out.println("Lista de bens carregada com " + bens.size() + " itens.");
        }

        ObservableList<Bem> listaDeItens = FXCollections.observableArrayList();
        for (Bem bem : bens) {
            listaDeItens.add(new Bem(
                    (int) bem.getId(),
                    bem.getNome(),
                    bem.getDescricao(),
                    bem.getLocalizacao(),
                    bem.getCapacidade(),
                    usuario,
                    "url"
            ));
        }

        listViewItens.setItems(listaDeItens);

        listViewItens.setCellFactory(new Callback<ListView<Bem>, ListCell<Bem>>() {
            @Override
            public ListCell<Bem> call(ListView<Bem> bemListView) {
                return new ListCell<Bem>() {
                    @Override
                    protected void updateItem(Bem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/ItemCell.fxml"));
                                if (loader.getLocation() == null) {
                                    System.err.println("Erro: Caminho do ItemCell.fxml não encontrado!");
                                    return;
                                }
                                HBox root = loader.load();
                                ControllerItemCell controller = loader.getController();
                                controller.setItem(item);
                                setGraphic(root);
                            } catch (IOException e) {
                                System.err.println("Erro ao carregar ItemCell.fxml: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                };
            }
        });
    }

    @FXML
    public void voltarParaTelaAdm(ActionEvent event) {
        System.out.println("Botão voltar clicado.");
        ScreenManager.getInstance().showAdmPrincipalScreen();
    }
}
