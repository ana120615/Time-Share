package br.ufrpe.timeshare.gui.controllers.usuarioComum.telaVendaCotas;

import br.ufrpe.timeshare.excecoes.CotaNaoExisteException;
import br.ufrpe.timeshare.excecoes.DadosInsuficientesException;
import br.ufrpe.timeshare.excecoes.UsuarioNaoExisteException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.gui.controllers.basico.ControllerBase;
import br.ufrpe.timeshare.gui.controllers.celulas.ControllerItemCellBemOfertado;
import br.ufrpe.timeshare.gui.controllers.celulas.ControllerItemCellCotaVenda;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.ControladorVendas;
import br.ufrpe.timeshare.negocio.beans.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @FXML
    private ListView<Cota> listViewCotasSelecionadas;
    @FXML
    private Label labelValorTotal;
    @FXML
    private Label labelQuantidadeCotasCarrinho;
    @FXML
    private TextField campoPesquisaNome;
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
            if (vendaAtual == null) {
                try {
                    this.vendaAtual = controladorVendas.iniciarVenda(usuario.getId());
                } catch (UsuarioNaoExisteException e) {
                    System.out.println(e.getMessage());
                }
            }

            carregarListaDeBens();
            carregarListaCarrinho();
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

    @FXML
    public void pesquisarBensNome() {
        carregarListaDeBens();
    }

    private void carregarListaDeBens() {
        if (usuario == null) {
            System.err.println("Erro: Usuário está null em carregarListaDeBens()!");
            return;
        }

        listViewItens.getItems().clear();

        List<Bem> bens = new ArrayList<>();
        if (!campoPesquisaNome.getText().isEmpty()) {
            try {
                bens = controladorBens.listarBensPorNome(campoPesquisaNome.getText().trim());
            } catch (DadosInsuficientesException e) {
                System.out.println(e.getMessage());
            }
        } else {
            bens = controladorBens.listarBensOfertados();
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
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/ItemCellBemOfertado.fxml"));
                                VBox root = loader.load();
                                ControllerItemCellBemOfertado controller = loader.getController();

                                controller.setVendaAtual(vendaAtual);
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
        campoPesquisaNome.clear();
        ScreenManager.getInstance().showUsuarioComumPrincipalScreen();
    }

    @FXML
    private void mudarAbaBensOfertados(ActionEvent event) {
        carregarListaDeBens();
        tabPaneUsuarioComumTelaVenda.getSelectionModel().select(tabBensOfertados);
    }

    @FXML
    private void mudarAbaCarrinho(ActionEvent event) {
        tabPaneUsuarioComumTelaVenda.getSelectionModel().select(tabCarrinho);
    }


    // Parte Carrinho
    public void carregarListaCarrinho() {

        this.labelValorTotal.setText(String.valueOf(vendaAtual.calcularValorTotal()));
        this.labelQuantidadeCotasCarrinho.setText(String.valueOf(vendaAtual.getCarrinhoDeComprasCotas().size()));

        List<Cota> cotas = vendaAtual.getCarrinhoDeComprasCotas();
        if (cotas == null || cotas.isEmpty()) {
            System.err.println("Erro: lista de cotas vazia ou null.");
            return;
        } else {
            System.out.println("Lista de cotas carregada com " + cotas.size() + " itens.");
        }

        ObservableList<Cota> listaDeItens = FXCollections.observableArrayList(cotas);
        listViewCotasSelecionadas.getItems().setAll(listaDeItens);

        listViewCotasSelecionadas.setCellFactory(new Callback<>() {
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
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/ItemCellCotaVenda.fxml"));
                                HBox root = loader.load();
                                ControllerItemCellCotaVenda controller = loader.getController();
                                controller.setTelaVenda(2); //
                                controller.setItem(item);
                                controller.setMainControllerVendaCotas(ControllerTelaDeVenda.this);// Passa referência do controlador principal
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

    public void btnFinalizarCompra() {
        String promocao = "";

        if (vendaAtual.getCarrinhoDeComprasCotas().isEmpty()) {
            exibirAlertaErro("Erro", "Carrinho vazio", "Va em ofertas e adicione cotas para comprar!");
        } else {
            List<Reserva> reservas = controladorVendas.getReservasNoPeriodoVenda(vendaAtual);

            if (!reservas.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Existem reservas no periodo de suas cotas por outros usuarios!");
                alert.setHeaderText("Deseja cancelar reserva?");
                alert.setContentText("Aperte OK para confirmar e CANCELAR para nao.");

                // Exibindo o alerta e capturando a resposta
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    controladorVendas.cancelarReservasEmCota(reservas);
                } else {
                    System.out.println("Usuário cancelou a ação!");
                }
            }

            try {
                promocao = controladorVendas.verificarSeUsuarioPossuiDescontos(usuario.getId());
            } catch (UsuarioNaoExisteException e) {
                exibirAlertaErro("Erro", "Erro ao procurar usuario", e.getMessage());
            }

            if (!promocao.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Promocoes disponiveis na compra!");
                alert.setHeaderText(promocao);
                alert.setContentText("Deseja aplicar remocao?");

                // Exibindo o alerta e capturando a resposta
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        controladorVendas.aplicarDesconto(vendaAtual, usuario.getId());
                    } catch (UsuarioNaoExisteException e) {
                        exibirAlertaErro("Erro", "Erro ao buscar usuario", e.getMessage());
                    }
                } else {
                    System.out.println("Usuário cancelou a ação!");
                }
            }

            vendaAtual.finalizarCompra();
            exibirAlertaInformation("Operação finalizada", "NOTA FISCAL", vendaAtual.toString());

            // Limpar carrinho de compras
            vendaAtual.getCarrinhoDeComprasCotas().clear(); // Remove todos os itens do carrinho

            // Atualizar interface gráfica
            listViewCotasSelecionadas.getItems().clear();
            labelValorTotal.setText("0.00");
            labelQuantidadeCotasCarrinho.setText("0");

            try {
                vendaAtual = controladorVendas.iniciarVenda(usuario.getId());
            } catch (UsuarioNaoExisteException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public void removerCotaCarrinhoVendaTelaPrincipal(Cota cotaSelecionada) {
        if (cotaSelecionada == null) {
            System.err.println("Erro: Nenhuma cota foi selecionada!");
            exibirAlertaErro("Erro", "Cota não selecionada", "Selecione uma cota para remover do carrinho.");
            return;
        }

        System.out.println("Tentando remover a cota: " + cotaSelecionada.getId() + " - " + cotaSelecionada.getPreco());

        if (this.vendaAtual == null) {
            System.err.println("Erro: vendaAtual está null!");
            return;
        }

        try {
            controladorVendas.removeCotaCarrinho((int) cotaSelecionada.getId(), this.vendaAtual);
            System.out.println("Cota removida com sucesso!");
            exibirAlertaInformation("Sucesso", "Cota removida", "Cota removida do carrinho com sucesso.");
            carregarListaCarrinho();
        } catch (CotaNaoExisteException e) {
            System.out.println(e.getMessage());
        }
    }

    private void exibirAlertaErro(String titulo, String header, String contentText) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(header);
        alerta.setContentText(contentText);
        alerta.getDialogPane().setStyle("-fx-background-color:  #ffcccc;");
        alerta.showAndWait();
    }

    private void exibirAlertaInformation(String titulo, String header, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contentText);
        alert.getDialogPane().setStyle("-fx-background-color: #ccffcc;");
        alert.showAndWait();
    }

}
