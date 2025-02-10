package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.negocio.beans.Usuario;
import br.ufrpe.timeshare.negocio.beans.Bem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import java.io.IOException;

public class ControllerListarBens implements ControllerBase {
    private Usuario usuario;
    @FXML private ListView<Bem> listViewItens;

    @Override
    public void receiveData(Object data) {
        if (data instanceof Usuario) {
            this.usuario = (Usuario) data;
        }
    }

    @FXML
    public void initialize() {
        ObservableList<Bem> listaDeItens = FXCollections.observableArrayList(
                new Bem (2222, "Lar doce Lar", "Centro da cidade", "Recife", 5, usuario, "teste"),
                new Bem (3333, "Casa do Mar", "Centro da cidade", "Recife", 5, usuario, "teste2")
        );

        listViewItens.setItems(listaDeItens);

        // Personaliza o ListView usando o ItemCell.fxml
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
                                HBox root = loader.load();
                                ControllerItemCell controller = loader.getController();
                                controller.setItem(item);
                                setGraphic(root);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
            }
        });
    }
}
