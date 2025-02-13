package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.excecoes.BemNaoExisteException;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.beans.Bem;
import br.ufrpe.timeshare.negocio.beans.Cota;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerAdicionarCotaPopUp {
    private final ControladorBens controladorBens = new ControladorBens();
    private Bem bem;
    private ControllerTelaDeVenda mainControllerVenda;

    @FXML
    private ListView<Cota> listViewCotasBemOfertado;

    @FXML
    public void initialize() {
        configurarCelulas();
    }

    public void setBem(Bem bem) {
        if (bem == null) {
            System.err.println("Erro: Bem está null em setBem()!");
            return;
        }
        this.bem = bem;
        carregarListaDeCotas();
    }

    public void setMainControllerVenda(ControllerTelaDeVenda mainControllerVenda) {
        this.mainControllerVenda = mainControllerVenda;
    }

    private void carregarListaDeCotas() {
        if (bem == null) {
            System.err.println("Erro: Bem está null em carregarListaDeCotas()!");
            return;
        }

        List<Cota> cotas = new ArrayList<>();
        try {
            cotas = controladorBens.listarCotasDeUmBem((int) bem.getId());
            System.out.println("Total de cotas carregadas: " + cotas.size());
        } catch (BemNaoExisteException e) {
            System.err.println("Erro: Bem não existe!");
        }

        if (cotas.isEmpty()) {
            System.err.println("Nenhuma cota encontrada para o bem: " + bem.getId());
        }

        ObservableList<Cota> listaDeItens = FXCollections.observableArrayList(cotas);
        listViewCotasBemOfertado.setItems(listaDeItens);
    }

    private void configurarCelulas() {
        listViewCotasBemOfertado.setCellFactory(new Callback<>() {
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
                                controller.setItem(item);
                                controller.setMainControllerAdicionarCotaPopUp(ControllerAdicionarCotaPopUp.this);
                                setGraphic(root);
                            } catch (IOException e) {
                                System.err.println("Erro ao carregar AdicionarCotaDeUmBemPopUp.fxml: " + e.getMessage());
                                e.printStackTrace();
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        });
    }
}
