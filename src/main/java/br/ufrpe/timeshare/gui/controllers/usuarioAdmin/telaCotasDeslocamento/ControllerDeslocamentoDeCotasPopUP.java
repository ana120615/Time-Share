package br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaCotasDeslocamento;

import br.ufrpe.timeshare.excecoes.BemNaoExisteException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.gui.controllers.celulas.ControllerItemCellCota;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.beans.Bem;
import br.ufrpe.timeshare.negocio.beans.Cota;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class ControllerDeslocamentoDeCotasPopUP {

    @FXML
    private DatePicker dataDeslocamentoPicker;
    @FXML
    private ListView<Cota> listViewItensCotas;

    private Bem bem;
    private final ControladorBens controladorBens;
    private ControllerDeslocamentoCotas mainController;

    public ControllerDeslocamentoDeCotasPopUP() {
        this.controladorBens = new ControladorBens();
    }

    public void setBem(Bem bem) {
        if (bem == null) {
            System.err.println("Erro: Bem está null em setBem()!");
            return;
        }
        this.bem = bem;
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


    @FXML
    public void calcularDeslocamento(ActionEvent event) {
        carregarCotasDeslocadas();
    }

    private void carregarCotasDeslocadas() {

        // limpar a ListView antes de carregar novos itens
        listViewItensCotas.getItems().clear();

        if (dataDeslocamentoPicker.getValue() == null) {
            exibirAlertaErro("Erro", "Campos obrigatórios não preenchidos", "Por favor, preencha todos os campos.");
            return;
        }

        List<Cota> cotas;
        try {
            cotas = controladorBens.calcularDeslocamentoDasCotas(Integer.parseInt(String.valueOf(bem.getId())), dataDeslocamentoPicker.getValue().atStartOfDay());
        } catch (BemNaoExisteException e) {
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
                                controller.setItem(item);
                                controller.setMainControllerCotasDeslocamento(ControllerDeslocamentoDeCotasPopUP.this); // Passa referência do controlador principal
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
    public void voltarParaTelaAdm(ActionEvent event) {
        dataDeslocamentoPicker.setValue(null);
        listViewItensCotas.getItems().clear();
        System.out.println("Botão voltar clicado.");
        ScreenManager.getInstance().showAdmPrincipalScreen();
    }

    public void setMainController(ControllerDeslocamentoCotas mainControllerDeslocamento) {
        this.mainController = mainControllerDeslocamento;
    }

}
