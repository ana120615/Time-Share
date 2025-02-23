package br.ufrpe.timeshare.gui.controllers.usuarioComum.telaMinhasCotas;

import br.ufrpe.timeshare.excecoes.*;
import br.ufrpe.timeshare.negocio.ControladorVendas;
import br.ufrpe.timeshare.negocio.beans.Cota;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ControllerRepassarDireitoUsoPopUp {
    private ControladorVendas controladorVendas;

    {
        controladorVendas = new ControladorVendas();
    }

    private Cota cota;
    @FXML
    private Button btnFechar;

    @FXML
    private TextField nomeTextField;
    @FXML
    private TextField idBemTextField;
    @FXML
    private TextField capacidadeTextField;
    @FXML
    private TextField localizacaoTextField;
    @FXML
    private TextArea descricaoTextArea;
    @FXML
    private ImageView imagem;
    @FXML
    private TextField precoTextField;
    @FXML
    private TextField dataIncial;
    @FXML
    private TextField dataFim;
    @FXML
    private TextField cpfProprietario;
    @FXML
    private TextField cpfDestinatario;

    private ControllerMinhasCotas mainController;


    @FXML
    private void initialize() {
        System.out.println("inicialize");
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


    public void setCota(Cota cota) {
        if (cota == null) {
            System.err.println("Erro: Bem está null em setBem()!");
            return;
        }
        this.cota = cota;
        nomeTextField.setText(cota.getBemAssociado().getNome());
        idBemTextField.setText(cota.getBemAssociado().getId() + "");
        localizacaoTextField.setText(cota.getBemAssociado().getLocalizacao());
        descricaoTextArea.setText(cota.getBemAssociado().getDescricao());
        imagem.setImage(carregarImagem(cota.getBemAssociado().getCaminhoImagem()));
        precoTextField.setText(String.valueOf(cota.getPreco()));
        dataIncial.setText(cota.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dataFim.setText(cota.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        cpfProprietario.setText(String.valueOf(cota.getProprietario().getId()));
        capacidadeTextField.setText(String.valueOf(cota.getBemAssociado().getCapacidade()));
    }


    public void setMainController(ControllerMinhasCotas mainController) {
        this.mainController = mainController;
    }


    @FXML
    private void fecharPopup() {
        Stage stage = (Stage) btnFechar.getScene().getWindow();
        stage.close();
    }

    public void btnRepassarDireito() {

        // Validação dos campos
        String cpfDest = cpfDestinatario.getText();

        // Verifica se os campos estão preenchidos corretamente
        if (cpfDest == null || cpfDest.trim().isEmpty()) {
            exibirAlertaErro("Erro", "Campos obrigatórios não preenchidos", "Por favor, preencha todos os campos.");
            return;
        }

        try {
            controladorVendas.transferenciaDeDireitos(cota.getProprietario().getId(), Long.parseLong(cpfDest), cota.getId());
            exibirAlertaInformation("Operacao concluída", "Operacao realizada com sucesso!", "Fechando tela de transferencia...");
            System.out.println("Bem alterado com sucesso!");

            //fechar a janela após a operacao
            fecharPopup();

            //atualizar a listagem na tela principal, se necessário
            if (mainController != null) {
                mainController.carregarListaDeCotas();
            }
        } catch (CotaNaoExisteException | UsuarioNaoExisteException | TransferenciaInvalidaException e) {
            exibirAlertaErro("Erro", "Erro ao tentar finalizar transferencia", e.getMessage());
        }

    }

    private Image carregarImagem(String caminhoImagem) {

        if (caminhoImagem == null || caminhoImagem.isEmpty()) {
            return carregarImagemPadrao();
        }
        try {
            File file = new File(caminhoImagem);
            if (file.exists()) {
                return new Image(file.toURI().toString());  // Carrega caminho absoluto
            } else {
                imagem.setFitHeight(64);
                imagem.setFitWidth(64);
                imagem.setOpacity(0.7);
                return new Image(Objects.requireNonNull(getClass().getResourceAsStream(caminhoImagem))); // Tenta carregar do classpath
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem: " + e.getMessage());
            return carregarImagemPadrao();
        }
    }

    private Image carregarImagemPadrao() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/br/ufrpe/timeshare/gui/application/images/picture.png")));
    }

}
