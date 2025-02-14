package br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaMeusBens;

import br.ufrpe.timeshare.excecoes.BemJaOfertadoException;
import br.ufrpe.timeshare.excecoes.BemNaoExisteException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.beans.Bem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class ControllerEditarBemPopUp {
    private ControladorBens controladorBens;

    {
        controladorBens = new ControladorBens();
    }

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
    @FXML
    private ToggleButton btnLigarDesligarOfertado;
    private boolean isTrue = false;
    private File imagemSelecionada;
    private ControllerListarBens mainController;


    @FXML
    private void initialize() {
        btnLigarDesligarOfertado.setOnAction(this::AcaoMudarOfertado);
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


    public void setBem(Bem bem) {
        if (bem == null) {
            System.err.println("Erro: Bem está null em setBem()!");
            return;
        }
        this.bem = bem;
        nomeTextField.setText(bem.getNome());
        idBemTextField.setText(bem.getId() + "");
        localizacaoTextField.setText(bem.getLocalizacao());
        descricaoTextArea.setText(bem.getDescricao());
        imagemView.setImage(carregarImagem(bem.getCaminhoImagem()));
        btnLigarDesligarOfertado.setText(bem.isOfertado() ? "Desligar Ofertado" : "Ligar Ofertado");

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

        // Validação dos campos
        String nome = nomeTextField.getText();
        String localizacao = localizacaoTextField.getText();
        String descricao = descricaoTextArea.getText();


        // Verifica se os campos estão preenchidos corretamente
        if (nome.isEmpty() || localizacao.isEmpty() || descricao.isEmpty()) {
            exibirAlertaErro("Erro", "Campos obrigatórios não preenchidos", "Por favor, preencha todos os campos.");
            return;
        }

        int capacidade = capacidadeSpinner.getValue();
        String caminhoImagem = salvarImagem(); // Salva a imagem e obtem o caminho relativo
        try {
            controladorBens.alterarNomeBem((int) bem.getId(), nome);
            controladorBens.alterarLocalizacaoBem((int) bem.getId(), localizacao);
            controladorBens.alterarDescricaoBem((int) bem.getId(), descricao);
            controladorBens.alterarCapacidadeBem((int) bem.getId(), capacidade);

            if(isTrue) {
                controladorBens.ofertarBem((int) bem.getId());
            }
            exibirAlertaInformation("Operacao concluída", "Operacao realizada com sucesso!", "Fechando tela de edicao...");
            System.out.println("Bem alterado com sucesso!");

            //fechar a janela após a edicao
            fecharPopup();

            //atualizar a listagem na tela principal, se necessário
            if (mainController != null) {
                mainController.carregarListaDeBens();
            }
        } catch (BemNaoExisteException | NullPointerException | BemJaOfertadoException e) {
            exibirAlertaErro("Erro", "Erro ao alterar nome do bem", e.getMessage());
        }

    }


    public void AcaoMudarOfertado(ActionEvent event) {
        this.isTrue = !bem.isOfertado();
        this.btnLigarDesligarOfertado.setText(isTrue ? "Desligar Ofertado" : "Ligar Ofertado");
    }

    public void btnExcluirBem() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Deseja realmente excluir o Bem?");
        alert.setContentText("Clique em OK para confirmar ou Cancelar para voltar.");
        // Exibindo o alerta e capturando a resposta
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ScreenManager.getInstance().showLoginScreen();
            System.out.println("Usuário confirmou a ação!");
            try {
                controladorBens.remover((int) bem.getId());
                System.out.println("Bem removido com sucesso!");

                //fechar a janela após a exclusão
                fecharPopup();

                //atualizar a listagem na tela principal, se necessário
                if (mainController != null) {
                    mainController.carregarListaDeBens();
                }
            } catch (BemNaoExisteException | IllegalAccessException e) {
                exibirAlertaErro("Erro", "Erro ao remover bem", e.getMessage());
            }
        } else {
            System.out.println("Usuário cancelou a ação!");
        }
    }

    public void handleSelecionarImagem() {
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
}
