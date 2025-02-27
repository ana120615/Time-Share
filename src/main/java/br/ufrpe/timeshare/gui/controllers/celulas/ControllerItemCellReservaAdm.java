package br.ufrpe.timeshare.gui.controllers.celulas;

import br.ufrpe.timeshare.excecoes.*;
import br.ufrpe.timeshare.gui.controllers.basico.ControllerBase;
import br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaReservas.ControllerListarReservas;
import br.ufrpe.timeshare.negocio.ControladorReservas;
import br.ufrpe.timeshare.negocio.beans.Reserva;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

public class ControllerItemCellReservaAdm implements ControllerBase {
    private final ControladorReservas controladorReservas;

    public ControllerItemCellReservaAdm() {
        this.controladorReservas = new ControladorReservas();
    }

    @FXML
    private Button itemButton;
    @FXML
    private ImageView itemImage;
    @FXML
    private Label idReserva;
    @FXML
    private Label itemLabelNomeBem;
    @FXML
    private Label dataInicio;
    @FXML
    private Label dataFim;
    @FXML
    private Label nomeProprietario;

    private Reserva reserva;
    private Usuario usuarioLogado;
    private int valorTela;
    private ControllerListarReservas mainControllerListarReservas;


    public void setValorTela(int valorTela) {
        this.valorTela = valorTela;
    }

    public void setItem(Reserva item) {
        this.reserva = item;

//        if (item == null) {
//            itemImage.setImage(carregarImagemPadrao());
//            return;
//        }

        itemLabelNomeBem.setText(item.getBem().getNome() != null ? item.getBem().getNome() : "Nome do bem não disponível");
        nomeProprietario.setText(item.getUsuarioComum().getNome() != null ? item.getUsuarioComum().getNome() : "Nome do proprietario não disponível");
        //itemImage.setImage(carregarImagem(item.getBem().getCaminhoImagem()));
        idReserva.setText(item.getId() != 0 ? String.valueOf(item.getId()) : "0");
        dataInicio.setText(item.getDataInicio() != null ? item.getDataInicio().toLocalDate().toString() : "Data inicial não disponível");
        dataFim.setText(item.getDataFim() != null ? item.getDataFim().toLocalDate().toString() : "Data final não disponível");

        if (valorTela == 1) {
            itemButton.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Dados da reserva");
                alert.setHeaderText("O que deseja fazer?");
                alert.setContentText(item.toString());

                ButtonType botaoCancelarReserva = new ButtonType("Cancelar Reserva", ButtonBar.ButtonData.YES);
                ButtonType botaoCancelarOperacao = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(botaoCancelarReserva, botaoCancelarOperacao);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent()) {
                    if (result.get() == botaoCancelarReserva) {
                        try {
                            controladorReservas.cancelarReserva((int) this.reserva.getId(), this.reserva.getUsuarioComum());
                            mainControllerListarReservas.carregarReservasBem(reserva.getBem());
                        } catch (ReservaNaoExisteException | ReservaJaCanceladaException | CotaJaReservadaException |
                                 UsuarioNaoPermitidoException |
                                 ReservaNaoReembolsavelException | DadosInsuficientesException | NullPointerException |
                                 OperacaoNaoPermitidaException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else {
                        alert.close();
                    }
                }
            });
        }
    }


    public void setMainControllerListarReservas(ControllerListarReservas mainControllerListarReservas) {
        this.mainControllerListarReservas = mainControllerListarReservas;
    }

//    private Image carregarImagem(String caminhoImagem) {
//        if (caminhoImagem == null || caminhoImagem.isEmpty()) {
//            return carregarImagemPadrao();
//        }
//
//        try {
//            File file = new File(caminhoImagem);
//            if (file.exists()) {
//                return new Image(file.toURI().toString());  // Carrega caminho absoluto
//            } else {
//                return new Image(Objects.requireNonNull(getClass().getResourceAsStream(caminhoImagem))); // Tenta carregar do classpath
//            }
//        } catch (Exception e) {
//            System.err.println("Erro ao carregar imagem: " + e.getMessage());
//            return carregarImagemPadrao();
//        }
//    }
//
//
//    private Image carregarImagemPadrao() {
//        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/br/ufrpe/timeshare/gui/application/images/picture.png")));
//    }

    @Override
    public void receiveData(Object data) {
        System.out.println("receiveData chamado com: " + data);
        if (data instanceof Usuario) {
            this.usuarioLogado = (Usuario) data;
            System.out.println("Usuário definido: " + usuarioLogado.getNome());
        } else {
            System.err.println("Erro: receiveData recebeu um objeto inválido.");
        }
    }
}
