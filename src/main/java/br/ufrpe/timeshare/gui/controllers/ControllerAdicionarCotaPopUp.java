package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.excecoes.BemNaoExisteException;
import br.ufrpe.timeshare.excecoes.CotaNaoExisteException;
import br.ufrpe.timeshare.excecoes.CotaNaoOfertadaException;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.ControladorVendas;
import br.ufrpe.timeshare.negocio.beans.Bem;
import br.ufrpe.timeshare.negocio.beans.Cota;
import br.ufrpe.timeshare.negocio.beans.Venda;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerAdicionarCotaPopUp {
    private final ControladorBens controladorBens;
    private final ControladorVendas controladorVendas;

    {
        controladorBens = new ControladorBens();
        controladorVendas = new ControladorVendas();
    }
    private Bem bem;
    private ControllerTelaDeVenda mainControllerVenda;

    @FXML
    private ListView<Cota> listViewCotasBemOfertado;

    @FXML
    public void initialize() {
        configurarCelulas();
    }

    private void exibirAlertaErro(String titulo, String header, String contentText) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(header);
        alerta.setContentText(contentText);
        alerta.getDialogPane().setStyle("-fx-background-color:  #ffcccc;"); // Vermelho claro
        alerta.showAndWait();
    }

    private void exibirAlertaInformation(String titulo, String header, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contentText);
        alert.getDialogPane().setStyle("-fx-background-color: #ccffcc;"); // Verde claro
        alert.showAndWait();
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


    public void adicionarCotaCarrinhoVenda () {
        Cota cotaSelecionada = listViewCotasBemOfertado.getSelectionModel().getSelectedItem();
        if (cotaSelecionada == null) {
            exibirAlertaErro("Erro", "Cota não selecionada", "Selecione uma cota para adicionar ao carrinho.");
            return;
        }

        if (mainControllerVenda == null) {
            System.err.println("Erro: mainControllerVenda está null em adicionarCotaCarrinhoVenda()!");
            return;
        }
        Venda venda = mainControllerVenda.getVendaAtual();
        if (venda == null) {
            System.err.println("Erro: venda está null em adicionarCotaCarrinhoVenda()!");
            return;
        }
        try {
            controladorVendas.adicionarCotaCarrinho((int) cotaSelecionada.getId(), venda);
            exibirAlertaInformation("Sucesso", "Cota adicionada", "Cota adicionada ao carrinho com sucesso.");
        } catch (CotaNaoExisteException | CotaNaoOfertadaException e) {
            System.out.println(e.getMessage());
        }

    }
}
