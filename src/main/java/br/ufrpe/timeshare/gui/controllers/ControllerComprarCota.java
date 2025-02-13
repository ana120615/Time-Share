package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.excecoes.*;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.ControladorVendas;
import br.ufrpe.timeshare.negocio.beans.Cota;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import br.ufrpe.timeshare.negocio.beans.Venda;
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

public class ControllerComprarCota implements ControllerBase {
    @FXML
    private TextField idBemProcurado;
    @FXML
    private TextField idCotaAdicionar;
    @FXML
    private TextField idCotaRemover;
    @FXML
    private Label totalCompra;
    @FXML
    private ListView<Cota> listViewItensCotas;


    private Usuario usuario;
    private Venda vendaAtual;
    private final ControladorBens controladorBens;
    private final ControladorVendas controladorVendas;

    public ControllerComprarCota() {
        this.controladorBens = new ControladorBens();
        this.controladorVendas = new ControladorVendas();
    }

    @Override
    public void receiveData(Object data) {
        System.out.println("receiveData chamado com: " + data);
        if (data instanceof Usuario) {
            this.usuario = (Usuario) data;
            System.out.println("Usuário definido: " + usuario.getNome());
            try {
                vendaAtual = controladorVendas.iniciarVenda(usuario.getId());
            } catch (UsuarioNaoExisteException e) {
                System.err.println("Erro: Usuário não existe!");
                exibirAlertaErro("Erro", "Erro ao iniciar venda", "Usuário não existe!");
            }

        } else {
            System.err.println("Erro: receiveData recebeu um objeto inválido.");
        }

        totalCompra.setText("Total: R$ " + vendaAtual.calcularValorTotal());
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


    @FXML
    public void initialize() {
        System.out.println("initialize() chamado.");
    }

    @FXML
    public void buscarCotas(ActionEvent event) {
        carregarListaDeCotas();
    }

    private void carregarListaDeCotas() {
        if (usuario == null) {
            System.err.println("Erro: Usuário está null em carregarListaDeCotas()!");
            return;
        }

        // limpar a ListView antes de carregar novos itens
        listViewItensCotas.getItems().clear();

        List<Cota> cotas = new ArrayList<>();
        if (!idBemProcurado.getText().isEmpty()) {
            try {
                cotas = controladorBens.listarCotasDeUmBem(Integer.parseInt(idBemProcurado.getText()));
            } catch (BemNaoExisteException e) {
                System.err.println("Bem com este id nao existe!");
                exibirAlertaErro("Erro", "Erro ao procurar bem", "Bem com este id nao existe!");
                return;
            }
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
                                controller.setMainControllerComprarCota(ControllerComprarCota.this); // Passa referência do controlador principal
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
    public void voltarParaTelaUsuarioComum(ActionEvent event) {
        ScreenManager.getInstance().showUsuarioComumPrincipalScreen();
    }

    @FXML
    public void adicionarCota(ActionEvent event) {
        if (vendaAtual == null) {
            System.err.println("Erro: Venda atual está null em adicionarCota()!");
            return;
        }

        if (idCotaAdicionar.getText().isEmpty()) {
            System.err.println("Erro: Campo de id da cota a adicionar está vazio.");
            return;
        }

        try {
            controladorVendas.adicionarCotaCarrinho(Integer.parseInt(idCotaAdicionar.getText()), vendaAtual);
            carregarListaDeCotas();
            idCotaAdicionar.clear();
            idCotaRemover.clear();
            totalCompra.setText("Total: R$ " + vendaAtual.calcularValorTotal());
        } catch (CotaNaoExisteException | CotaNaoOfertadaException e) {
            System.err.println("Erro: Cota nao ofertada ou nao existe");
            exibirAlertaErro("Erro", "Erro ao adicionar cota", "Cota nao existe ou nao ofertada");
        }
    }

    @FXML
    public void removerCota(ActionEvent event) {
        if (vendaAtual == null) {
            System.err.println("Erro: Venda atual está null em adicionarCota()!");
            return;
        }

        if (idCotaRemover.getText().isEmpty()) {
            System.err.println("Erro: Campo de id da cota a adicionar está vazio.");
            return;
        }

        try {
            controladorVendas.removeCotaCarrinho(Integer.parseInt(idCotaRemover.getText()), vendaAtual);
            carregarListaDeCotas();
            totalCompra.setText("Total: R$ " + vendaAtual.calcularValorTotal());

            idCotaAdicionar.clear();
            idCotaRemover.clear();

        } catch (CotaNaoExisteException e) {
            System.err.println("Erro");
            exibirAlertaErro("Erro", "Erro ao adicionar cota", "Não existe!");
        }
    }

    @FXML
    public void finalizarCompra(ActionEvent event) {
        if (vendaAtual == null) {
            System.err.println("Erro: Venda atual está null em adicionarCota()!");
            return;
        }

        try {
            String recibo = controladorVendas.finalizarCompra(vendaAtual);
            exibirAlertaInformation("Operação finalizada", "Compra realizada com sucesso!", recibo);
            carregarListaDeCotas();
            totalCompra.setText("Total: R$ " + vendaAtual.calcularValorTotal());

            idBemProcurado.clear();
            idCotaAdicionar.clear();
            idCotaRemover.clear();

        } catch (CompraNaoFinalizada e) {
            System.err.println("Erro: Usuário não existe!");
            exibirAlertaErro("Erro", "Erro ao adicionar cota", "Verifique se esta tudo correto");
        }
    }


}
