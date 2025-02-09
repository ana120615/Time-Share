package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.dados.RepositorioBens;
import br.ufrpe.timeshare.dados.RepositorioCotas;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ControllerCadastroBem implements Initializable {
    private Usuario usuarioLogado;

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }
    private String caminhoImagem;
    // FXML Variables
    @FXML private TextField nomeTextField;
    @FXML private TextArea descricaoTextArea;
    @FXML private TextField localizacaoTextField;
    @FXML private TextField idBemTextField;
    @FXML private Button btnCadastrarBem;
    @FXML private Pane paneBens;
    @FXML private TextField precoTextField;
    @FXML private ImageView imagemView;  
    @FXML private Button adicionarImagemButton;

    // Spinner para Capacidade e Quantidade de Cotas
    @FXML private Spinner<Integer> capacidadeSpinner;
    @FXML private Spinner<Integer> quantidadeCotasSpinner;
    
    // DatePicker para a Data Inicial
    @FXML private DatePicker dataInicialPicker;

    // Controlador de Negócio
    private ControladorBens controladorBens;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Inicializa o controlador
            controladorBens = new ControladorBens(new RepositorioBens(), new RepositorioCotas());
            usuarioLogado = (Usuario) ScreenManager.getInstance().getData(); // Recupera o usuario do ScreenManager
    
            if (usuarioLogado == null) {
                System.out.println("Usuário não encontrado!"); 
                showAlert(Alert.AlertType.ERROR, "Erro", "Você precisa estar logado para cadastrar um bem.");
            }
    
            // Inicializar Spinners
            SpinnerValueFactory<Integer> capacidadeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000);
            capacidadeSpinner.setValueFactory(capacidadeValueFactory);
            
            SpinnerValueFactory<Integer> quantidadeCotasValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 52);
            quantidadeCotasSpinner.setValueFactory(quantidadeCotasValueFactory);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao inicializar: " + e.getMessage());
        }
    }
    

//imagem do bem
@FXML
private void handleSelecionarImagem(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif"));

    File file = fileChooser.showOpenDialog(paneBens.getScene().getWindow());

    if (file != null) {
        // Verifica se o arquivo é muito grande (maior que 10 MB)
        long tamanhoMaximo = 10 * 1024 * 1024; // 10 MB
        if (file.length() > tamanhoMaximo) {
            showAlert(Alert.AlertType.WARNING, "Imagem muito grande", "A imagem excede o limite de tamanho permitido.");
            return;
        }

        // Carregamento assíncrono da imagem
        new Thread(() -> {
            try {
                // Redimensiona a imagem para 500x500, mantendo as proporções
                Image image = new Image(file.toURI().toString(), 500, 500, true, true); 

                // Atualização do ImageView na thread da UI
                Platform.runLater(() -> {
                    imagemView.setImage(image);
                    imagemView.setCache(true); // Ativa o cache para melhorar o desempenho
                    imagemView.setCacheHint(CacheHint.QUALITY); // Melhor qualidade para a imagem
                });

                caminhoImagem = file.getAbsolutePath(); // Salva o caminho da imagem
            } catch (Exception e) {
                e.printStackTrace(); // Imprime o erro (para depuração)
                Platform.runLater(() -> { // Exibe um alerta na thread da UI
                    showAlert(Alert.AlertType.ERROR, "Erro ao carregar imagem", "Erro: " + e.getMessage());
                });
            }
        }).start();
    }
}



    // Método para cadastrar um Bem
    @FXML
    private void handleCadastrarBem(ActionEvent event) {
        try {
            int id = Integer.parseInt(idBemTextField.getText());
            String nome = nomeTextField.getText();
            String descricao = descricaoTextArea.getText();
            String localizacao = localizacaoTextField.getText();
            int capacidade = capacidadeSpinner.getValue();
            int quantidadeCotas = quantidadeCotasSpinner.getValue();
            LocalDate dataInicial = dataInicialPicker.getValue();
             double precoCota = Double.parseDouble(precoTextField.getText());

            // Convertendo a data para LocalDateTime para usar no cadastro
            LocalDateTime dataHoraInicial = dataInicial.atStartOfDay();

            // Criação do Bem e cadastro
            controladorBens.cadastrar(id, nome, descricao, localizacao, capacidade, usuarioLogado, dataHoraInicial, quantidadeCotas, precoCota, caminhoImagem); 

            showAlert(Alert.AlertType.INFORMATION, "Cadastro de Bem", "Bem cadastrado com sucesso!");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro ao cadastrar", "Ocorreu um erro ao cadastrar o bem: " + e.getMessage());
        }
    }


    // Método para exibir um alerta
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
