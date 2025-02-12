package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.negocio.beans.Bem;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;

import java.io.File;
import java.time.LocalDate;
import java.util.Objects;

public class ControllerEditarBemPopUp {
    private Bem bem;
    @FXML
    private Button btnFechar;

    @FXML
    private TextField nomeTextField;
    @FXML
    private TextField idBemTextField;
    @FXML
    private TextField localizacaoTextField;
    @FXML
    private TextArea descricaoTextArea;
    @FXML
    private ImageView imagemView;
    @FXML
    private Spinner<Integer> capacidadeSpinner;
    @FXML
    private Spinner<Integer> quantidadeCotasSpinner;
    @FXML
    private DatePicker dataInicialPicker;
    @FXML
    private TextField precoTextField;

    private ControllerListarBens mainController;

    public void setBem(Bem bem) {
        if (bem == null) {
            System.err.println("Erro: Bem está null em setBem()!");
            return;
        }
        this.bem = bem;
        nomeTextField.setText(bem.getNome());
        idBemTextField.setText(bem.getId()+"");
        localizacaoTextField.setText(bem.getLocalizacao());
        descricaoTextArea.setText(bem.getDescricao());
        imagemView.setImage(carregarImagem(bem.getCaminhoImagem()));

        // Inicializando os spinners caso não tenha sido feito ainda
        if (capacidadeSpinner.getValueFactory() == null) {
            capacidadeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 50));  // Ajuste os valores conforme necessário
        }
        capacidadeSpinner.getValueFactory().setValue(bem.getCapacidade());

        // Para quantidadeCotasSpinner, faça o mesmo
        if (quantidadeCotasSpinner.getValueFactory() == null) {
            quantidadeCotasSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 50));  // Ajuste conforme necessário
        }
        quantidadeCotasSpinner.getValueFactory().setValue(bem.getCotas().size());

        dataInicialPicker.setValue(LocalDate.parse(bem.getDiaInicial().toLocalDate().toString()));
        precoTextField.setText(String.valueOf(bem.getCotas().getFirst().getPreco()));
    }


    public void setMainController(ControllerListarBens mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void fecharPopup() {
        Stage stage = (Stage) btnFechar.getScene().getWindow();
        stage.close();
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

    public void btnSalvaAlteracoes() {
        System.out.println("Salvando alterações...");
    }

    public void btnExcluirBem () {
        System.out.println("Excluir bem...");
    }

    public void handleSelecionarImagem() {
        System.out.println("Selecionar imagem...");
    }
}
