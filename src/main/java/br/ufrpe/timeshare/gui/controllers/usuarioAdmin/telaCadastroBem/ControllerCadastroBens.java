package br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaCadastroBem;

import br.ufrpe.timeshare.excecoes.BemJaExisteException;
import br.ufrpe.timeshare.excecoes.BemNaoExisteException;
import br.ufrpe.timeshare.excecoes.UsuarioNaoExisteException;
import br.ufrpe.timeshare.excecoes.UsuarioNaoPermitidoException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

public class ControllerCadastroBens {
    private ControladorBens controladorBens;
    private Usuario usuarioLogado;

    {
        controladorBens = new ControladorBens();
    }

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

    private File imagemSelecionada;

    @FXML
    private void initialize() {
        controladorBens = new ControladorBens();
        capacidadeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        quantidadeCotasSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 52, 1));
    }

    private void exibirAlertaErro(String titulo, String header, String contentText) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(header);
        alerta.setContentText(contentText);
        alerta.getDialogPane().setStyle("-fx-background-color:  #ffcccc;"); // Vermelho claro
        alerta.showAndWait();
    }

    private void exibirAlertaInformation(String titulo, String header, String contentText) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contentText);
        alert.getDialogPane().setStyle("-fx-background-color: #ccffcc;"); // Verde claro
        alert.showAndWait();
    }

    @FXML
    private void handleCadastrarBem(ActionEvent event) {
        Object data = ScreenManager.getInstance().getData();

        if (data instanceof Usuario) {
            usuarioLogado = (Usuario) data;

            // Validação dos campos
            String nome = nomeTextField.getText();
            String idString = idBemTextField.getText();
            String localizacao = localizacaoTextField.getText();
            String descricao = descricaoTextArea.getText();
            String precoString = precoTextField.getText();

            // Verifica se os campos estão preenchidos corretamente
            if (nome.isEmpty() || idString.isEmpty() || localizacao.isEmpty() || descricao.isEmpty() || precoString.isEmpty()) {
                exibirAlertaErro("Erro", "Campos obrigatórios não preenchidos", "Por favor, preencha todos os campos.");
                return;
            }

            // Verificação se os campos id e preco são números válidos
            int id;
            double precoDeUmaCota;
            try {
                id = Integer.parseInt(idString);
                precoDeUmaCota = Double.parseDouble(precoString);
            } catch (NumberFormatException e) {
                exibirAlertaErro("Erro", "Formato inválido", "Por favor, insira valores numéricos válidos para ID e preço.");
                return;
            }

            int capacidade = capacidadeSpinner.getValue();
            int quantidadeCotas = quantidadeCotasSpinner.getValue();
            LocalDateTime diaInicial = dataInicialPicker.getValue() != null ? dataInicialPicker.getValue().atStartOfDay() : null;

            if (diaInicial == null || quantidadeCotas == 0 || capacidade == 0) {
                exibirAlertaErro("Erro", "Campos obrigatórios não preenchidos", "Por favor, preencha todos os campos corretamente.");
                return;
            }

            String caminhoImagem = salvarImagem(); // Salva a imagem e obtem o caminho relativo

            try {
                controladorBens.cadastrar(id, nome, descricao, localizacao, capacidade, usuarioLogado, diaInicial, quantidadeCotas, precoDeUmaCota, caminhoImagem);
                exibirAlertaInformation("Cadastro concluído", "Bem cadastrado com sucesso!", "Bem " + nome + " com " + quantidadeCotas + " cotas.");

                // Limpa os campos após o cadastro (opcional)
                limparCampos();
            } catch (UsuarioNaoPermitidoException | BemJaExisteException | UsuarioNaoExisteException |
                     BemNaoExisteException e) {
                exibirAlertaErro("Erro", "Erro ao cadastrar bem", e.getMessage());
            } catch (RuntimeException e) {
                exibirAlertaErro("Erro", "Erro ao cadastrar bem", "Tente novamente.");
            }
        }
    }

    @FXML
    private void handleSelecionarImagem(ActionEvent event) {
        // Criação de um seletor de arquivos
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Imagem");

        // Filtrando tipos de imagem aceitos
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif")
        );

        // Abrindo o seletor de arquivos e obtendo o arquivo selecionado
        File imagemSelecionadaTemp = fileChooser.showOpenDialog(null);

        if (imagemSelecionadaTemp != null) {
            // Atualizando a variável global imagemSelecionada com o arquivo selecionado
            imagemSelecionada = imagemSelecionadaTemp;

            // Exibindo a imagem no ImageView
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
            exibirAlertaErro("Erro", "Erro ao salvar a imagem", e.getMessage());
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
        quantidadeCotasSpinner.getValueFactory().setValue(1);
        dataInicialPicker.setValue(null);
        precoTextField.clear();
        imagemView.setImage(null);
        imagemSelecionada = null;
    }
}
