package br.ufrpe.timeshare.gui.controllers.celulas;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import br.ufrpe.timeshare.excecoes.CotaJaReservadaException;
import br.ufrpe.timeshare.excecoes.DadosInsuficientesException;
import br.ufrpe.timeshare.excecoes.ForaPeriodoException;
import br.ufrpe.timeshare.excecoes.PeriodoJaReservadoException;
import br.ufrpe.timeshare.excecoes.PeriodoNaoDisponivelParaReservaException;
import br.ufrpe.timeshare.excecoes.ProprietarioNaoIdentificadoException;
import br.ufrpe.timeshare.excecoes.ReservaJaExisteException;
import br.ufrpe.timeshare.excecoes.ReservaNaoExisteException;
import br.ufrpe.timeshare.excecoes.UsuarioNaoPermitidoException;
import br.ufrpe.timeshare.gui.controllers.basico.ControllerBase;
import br.ufrpe.timeshare.negocio.ControladorReservas;
import br.ufrpe.timeshare.negocio.beans.Cota;
import br.ufrpe.timeshare.negocio.beans.Usuario;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ControllerItemCellCotaReserva implements ControllerBase {

    @FXML
    private HBox itemCell;

    @FXML
    private Label nomeBemAssociado;

    @FXML
    private Label dataInicio;

    @FXML
    private Label dataFim;

    @FXML
    private Label nomeProprietario;

    @FXML
    private Label idCota;

    private Cota cota;

    @FXML
    public Button reservarCotaButton;

    private ControladorReservas controladorReservas;

    private Usuario usuarioLogado;

    public void initialize() {

        this.controladorReservas = new ControladorReservas();

    }

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

    public void setItem(Cota cota) {
        this.cota = cota;
        if (cota != null) {

            idCota.setText(String.valueOf(cota.getId())); // Formata o ID para String
            nomeBemAssociado.setText(cota.getBemAssociado().getNome());
            // Formata as datas LocalDateTime para String
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            dataInicio.setText(cota.getDataInicio().format(formatter));
            dataFim.setText(cota.getDataFim().format(formatter));

            nomeProprietario.setText(cota.getProprietario().getNome());
        }
    }


    @FXML
    private void handleReservarCota() {
        String comprovante = null;
        if (cota != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação de Reserva");
            alert.setHeaderText("Confirmar reserva da cota " + cota.getId() + "?");
            alert.setContentText("Deseja realmente reservar esta cota?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Usuário clicou em OK, prosseguir com a reserva
                System.out.println("Reservar cota: " + cota.getId());
                try {
                    comprovante = controladorReservas.reservaPeriodoCota(cota, usuarioLogado);
                    exibirAlertaInfo("Operação realizada com sucesso!", "Cota reservada", comprovante);
                } catch (ProprietarioNaoIdentificadoException | UsuarioNaoPermitidoException |
                         DadosInsuficientesException
                         | ReservaJaExisteException | ForaPeriodoException | PeriodoJaReservadoException
                         | PeriodoNaoDisponivelParaReservaException | ReservaNaoExisteException
                         | CotaJaReservadaException e) {
                    exibirAlertaErro("Erro", "Problema ao reservar cota", e.getMessage());
                }
            } else {
                // Usuário clicou em Cancelar ou fechou o alerta
                System.out.println("Reserva cancelada para cota: " + cota.getId());
            }
        } else {
            exibirAlertaErro("Erro", "Cota não encontrada", "Não foi possível encontrar a cota para reserva.");
        }
    }


    private void exibirAlertaErro(String titulo, String header, String contentText) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle(titulo);

        alerta.setHeaderText(header);

        alerta.setContentText(contentText);

        alerta.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro

        alerta.showAndWait();

    }


    private void exibirAlertaInfo(String titulo, String header, String contentText) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle(titulo);

        alerta.setHeaderText(header);

        alerta.setContentText(contentText);

        alerta.showAndWait();

    }
}