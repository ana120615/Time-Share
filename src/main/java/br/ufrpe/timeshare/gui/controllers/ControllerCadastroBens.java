package br.ufrpe.timeshare.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

import br.ufrpe.timeshare.dados.IRepositorioBens;
import br.ufrpe.timeshare.dados.IRepositorioCotas;
import br.ufrpe.timeshare.dados.RepositorioBens;
import br.ufrpe.timeshare.dados.RepositorioCotas;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.beans.Usuario;

public class ControllerCadastroBens {

    @FXML
    private TextField nomeTextField;
    @FXML
    private TextField idBemTextField;
    @FXML
    private TextField localizacaoTextField;
    @FXML
    private TextArea descricaoTextArea;
    @FXML
    private Button btnCadastrarBem;
    @FXML
    private ImageView imagemView;
    @FXML
    private Button adicionarImagemButton;
    @FXML
    private Spinner<Integer> capacidadeSpinner;
    @FXML
    private Spinner<Integer> quantidadeCotasSpinner;
    @FXML
    private DatePicker dataInicialPicker;
    @FXML
    private TextField precoTextField;

    private Usuario usuarioLogado;
    private ControladorBens controladorBens;
    private File imagemSelecionada;

    @FXML
    private void initialize() {
        IRepositorioBens repositorioBens = RepositorioBens.getInstancia();
        IRepositorioCotas repositorioCotas = RepositorioCotas.getInstancia();
        controladorBens = new ControladorBens(repositorioBens, repositorioCotas);

        capacidadeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 1));
        quantidadeCotasSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 52, 0));
    }

    private void exibirAlerta(String mensagem, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle("Mensagem");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    @FXML
    private void handleCadastrarBem(ActionEvent event) {
        Object data = ScreenManager.getInstance().getData();

        if (data instanceof Usuario) {
            usuarioLogado = (Usuario) data;

            try {
                String nome = nomeTextField.getText();
                int id = Integer.parseInt(idBemTextField.getText());
                String localizacao = localizacaoTextField.getText();
                String descricao = descricaoTextArea.getText();
                int capacidade = capacidadeSpinner.getValue();
                int quantidadeCotas = quantidadeCotasSpinner.getValue();
                double precoDeUmaCota = Double.parseDouble(precoTextField.getText());
                LocalDateTime diaInicial = dataInicialPicker.getValue() != null ? dataInicialPicker.getValue().atStartOfDay() : null;

                String caminhoImagem = salvarImagem(); // Salva a imagem e obtem o caminho relativo

                controladorBens.cadastrar(id, nome, descricao, localizacao, capacidade, usuarioLogado, diaInicial, quantidadeCotas, precoDeUmaCota);
                exibirAlerta("Bem cadastrado com sucesso!", AlertType.INFORMATION);

                // Limpa os campos após o cadastro (opcional)
                limparCampos();

            } catch (NumberFormatException e) {
                exibirAlerta("Erro: Verifique os campos ID e Preço.", AlertType.ERROR);
            } catch (Exception e) {
                exibirAlerta("Erro ao cadastrar bem: " + e.getMessage(), AlertType.ERROR);
                e.printStackTrace(); 
            }
        }
    }

    @FXML
    private void handleSelecionarImagem(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Imagem");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif"));

        Stage stage = (Stage) adicionarImagemButton.getScene().getWindow();
        imagemSelecionada = fileChooser.showOpenDialog(stage);

        if (imagemSelecionada != null) {
            imagemView.setImage(new Image(imagemSelecionada.toURI().toString()));
        }
    }

    private String salvarImagem() {
        if (imagemSelecionada == null) {
            return null;
        }

        try {
            
            Path diretorio = Paths.get("images"); 
            if (!Files.exists(diretorio)) {
                Files.createDirectories(diretorio);
            }
            String nomeArquivo = UUID.randomUUID().toString() + "_" + imagemSelecionada.getName();
            Path destino = diretorio.resolve(nomeArquivo);
            Files.copy(imagemSelecionada.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
            return "images/" + nomeArquivo; 

        } catch (IOException e) {
            exibirAlerta("Erro ao salvar a imagem: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    public void voltarParaTelaAdm(ActionEvent event) {
        ScreenManager.getInstance().showAdmPrincipalScreen();
    }

    private void limparCampos() {
        nomeTextField.clear();
        idBemTextField.clear();
        localizacaoTextField.clear();
        descricaoTextArea.clear();
        capacidadeSpinner.getValueFactory().setValue(1);
        quantidadeCotasSpinner.getValueFactory().setValue(0);
        dataInicialPicker.setValue(null);
        precoTextField.clear();
        imagemView.setImage(null); 
        imagemSelecionada = null; 
    }


}