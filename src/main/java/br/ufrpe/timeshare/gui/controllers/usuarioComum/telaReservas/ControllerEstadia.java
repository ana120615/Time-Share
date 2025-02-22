package br.ufrpe.timeshare.gui.controllers.usuarioComum.telaReservas;
import br.ufrpe.timeshare.excecoes.CotaJaReservadaException;
import br.ufrpe.timeshare.excecoes.DadosInsuficientesException;
import br.ufrpe.timeshare.excecoes.EstadiaNaoExisteException;
import br.ufrpe.timeshare.excecoes.ForaPeriodoException;
import br.ufrpe.timeshare.excecoes.OperacaoNaoPermitidaException;
import br.ufrpe.timeshare.excecoes.PeriodoJaReservadoException;
import br.ufrpe.timeshare.excecoes.PeriodoNaoDisponivelParaReservaException;
import br.ufrpe.timeshare.excecoes.ReservaJaCanceladaException;
import br.ufrpe.timeshare.excecoes.ReservaJaExisteException;
import br.ufrpe.timeshare.excecoes.ReservaNaoExisteException;
import br.ufrpe.timeshare.excecoes.UsuarioNaoPermitidoException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.gui.controllers.basico.ControllerBase;
import br.ufrpe.timeshare.negocio.ControladorReservas;
import br.ufrpe.timeshare.negocio.beans.Estadia;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.util.List;
import java.util.Optional;

public class ControllerEstadia implements ControllerBase {
    @FXML
    private AnchorPane paneProlongar;
    
    @FXML
    private Spinner<Integer> spinnerDias;

    @FXML
    private ListView<Estadia> listViewEstadias;

    @FXML
    private Label labelEstadiaAtiva;
    
    @FXML
    private Button btnCheckOut, btnProlongar;

    private ControladorReservas controladorReservas;

    private Usuario usuarioLogado;
        
    private Estadia estadiaAtiva; // Armazena a estadia ativa do usuário
    
    @FXML
    public void initialize() {
        controladorReservas=new ControladorReservas();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 1);
        spinnerDias.setValueFactory(valueFactory);
    }
    
    @Override
    public void receiveData(Object data) {
        System.out.println("receiveData chamado com: " + data);
        if (data instanceof Usuario) {
            this.usuarioLogado = (Usuario) data;
            System.out.println("Usuário definido: " + usuarioLogado.getNome());
            carregarEstadias(usuarioLogado);
        } else {
            System.err.println("Erro: receiveData recebeu um objeto inválido.");
        }
    }

    private void carregarEstadias(Usuario usuario) {
        
        List<Estadia> estadias = controladorReservas.listarEstadiasUsuario(usuario);
        listViewEstadias.getItems().setAll(estadias);

        // Obtém a estadia ativa, se houver
        //implementar isso no controlador de reservas
        for(Estadia estadia: estadias){
            if(estadia.getReserva()!=null && estadia.getDataFim()==null){
                estadiaAtiva = estadia;
                break;
            }
        }
        if (estadiaAtiva != null) {
            labelEstadiaAtiva.setText(estadiaAtiva.toString());
        } else {
            labelEstadiaAtiva.setText("Nenhuma estadia ativa encontrada.");
        }

        if(btnCheckOut != null){
            btnCheckOut.setDisable(estadiaAtiva == null);
        }
        if(btnProlongar != null){
            btnProlongar.setDisable(estadiaAtiva == null);
        }

    }
    
    // Método para realizar o Check-out
    @FXML
    private void handleCheckOut() {
        if (estadiaAtiva == null) {
            exibirAlertaInfo("Nenhuma estadia ativa", "Nao foi encontrada nenhuma estadia ativa para o usuario","estadia ativa null");
            return;
        }

        // Confirmação do usuário
        Optional<ButtonType> confirmacao = mostrarConfirmacao("Deseja realmente finalizar esta estadia?");
        if (confirmacao.isPresent() && confirmacao.get() == ButtonType.OK) {
            
            try {
                controladorReservas.checkout((int)estadiaAtiva.getId());
                exibirAlertaInfo("Sucesso!","Check out realizado com sucesso","Volte sempre!");
                limparEstadiaAtiva();
            carregarEstadias(usuarioLogado);
            } catch (ReservaNaoExisteException | ReservaJaCanceladaException | EstadiaNaoExisteException e) {
                exibirAlertaErro("Erro", "Problema ao realizar check out", e.getMessage());
            }
        }
    }

    private void limparEstadiaAtiva() {
        estadiaAtiva = null; // Remove a referência à estadia ativa
        labelEstadiaAtiva.setText(""); // Limpa o label que exibe a estadia ativa
        btnCheckOut.setDisable(true); // Desabilita o botão de checkout
        btnProlongar.setDisable(true); // Desabilita o botão de prolongar
    }
    
    //FALTA CONSERTAR ISSO
    // Método para prolongar a estadia
    //Pede ao usuario a quantidade de dias
    @FXML
    private void handleProlongar() {
        if (estadiaAtiva == null) {
            exibirAlertaInfo("Nenhuma estadia ativa", "Nao foi encontrada nenhuma estadia ativa para o usuario", "estadia ativa null");
            return;
        }

        // Exibe o painel de prolongamento
        paneProlongar.setVisible(true);
    }

    @FXML
    private void handleConfirmarProlongamento() {
        int diasProlongamento = spinnerDias.getValue();
       String comprovante = null;
        // Exibe uma janela de confirmação
        Optional<ButtonType> confirmacao = mostrarConfirmacao("Deseja realmente prolongar a estadia por " + diasProlongamento + " dias?");
        if (confirmacao.isPresent() && confirmacao.get() == ButtonType.OK) {
            try {
                comprovante=controladorReservas.prolongarEstadia((int) estadiaAtiva.getId(), diasProlongamento);
                exibirAlertaInfo("Sucesso!", "Estadia prolongada com sucesso", "Aproveite!");
                exibirAlertaInfo("Estadia prolongada", "Comprovante da reserva associada", comprovante);
            } catch (Exception e) {
                exibirAlertaErro("Erro", "Problema ao prolongar estadia", e.getMessage());
            }

            // Esconde o painel após confirmação
            paneProlongar.setVisible(false);
            carregarEstadias(usuarioLogado);
        }
    }

    @FXML
    private void handleCancelarProlongamento() {
        // Apenas esconde o painel de prolongamento sem fazer nada
        paneProlongar.setVisible(false);
    }

    
    private void exibirAlertaErro(String titulo, String header, String contentText) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(header);
        alerta.setContentText(contentText);
        alerta.getDialogPane().setStyle("-fx-background-color:  #ffcccc;"); // Vermelho claro
        alerta.showAndWait();
    }

    private void exibirAlertaInfo(String titulo, String header, String contentText) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        
        alerta.setTitle(titulo);
        
        alerta.setHeaderText(header);
        
         alerta.setContentText(contentText);
        
         alerta.showAndWait();
        
        }

    // Método para exibir uma confirmação antes de uma ação
    private Optional<ButtonType> mostrarConfirmacao(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        return alert.showAndWait();
    }

    @FXML
    private void handleVoltar() {
       ScreenManager.getInstance().showUsuarioComumPrincipalScreen();
        System.out.println("Voltando para a tela anterior...");
    }
}


