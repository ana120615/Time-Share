package br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telaPrincipal;

import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.gui.controllers.basico.ControllerBase;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Optional;

public class ControllerAdm implements ControllerBase {

    private Usuario usuario;
    private boolean isExpanded = true;

    @FXML
    private VBox vboxContainer; // Menu lateral (VBox)
    @FXML
    private ImageView imageView; // Logo dentro do menu
    @FXML
    private TabPane tabPaneAdmTelaPrincipal;// Estado inicial
    @FXML
    private Tab tabGerenciamentoBens;
    @FXML
    private Tab tabGerenciamentoReservas;
    @FXML
    private Tab tabRelatorio;
    @FXML
    private Label nomeUsuario;

    @FXML
    public void irParaTelaCadastroBens(ActionEvent event) {
        ScreenManager.getInstance().showCadastroBensScreen();
    }

    @FXML
    public void irParaTelaListaBens(ActionEvent event) {
        ScreenManager.getInstance().showListarBensScreen();
    }

    public void toggleVBox(ActionEvent eventb) {
        if (isExpanded) {
            // Animação para esconder imagem suavemente
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), imageView);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(event -> imageView.setVisible(false));

            // Animação para reduzir largura da VBox
            Timeline shrink = new Timeline(
                    new KeyFrame(Duration.millis(200),
                            new KeyValue(vboxContainer.prefWidthProperty(), 0, Interpolator.EASE_BOTH))
            );
            shrink.setOnFinished(event -> vboxContainer.setVisible(false)); // Oculta completamente

            fadeOut.play();
            shrink.play();
        } else {
            // Primeiro, torna visível novamente
            vboxContainer.setVisible(true);

            // Torna a imagem visível antes de expandir
            imageView.setVisible(true);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), imageView);
            fadeIn.setToValue(1); // Aparece suavemente

            // Animação para expandir largura da VBox
            Timeline expand = new Timeline(
                    new KeyFrame(Duration.millis(200),
                            new KeyValue(vboxContainer.prefWidthProperty(), 222, Interpolator.EASE_BOTH))
            );

            expand.setOnFinished(event -> fadeIn.play()); // Faz a imagem reaparecer depois da expansão
            expand.play();
        }
        isExpanded = !isExpanded; // Alterna estado
    }

    public void mudarAbaBens (ActionEvent event) {
        tabPaneAdmTelaPrincipal.getSelectionModel().select(tabGerenciamentoBens);
    }

    public void mudarAbaReservas (ActionEvent event) {
        tabPaneAdmTelaPrincipal.getSelectionModel().select(tabGerenciamentoReservas);
    }

    public void mudarAbaRelatorio (ActionEvent event) {
        tabPaneAdmTelaPrincipal.getSelectionModel().select(tabRelatorio);
    }

    @FXML
    public void deslogar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Deseja realmente sair?");
        alert.setContentText("Clique em OK para confirmar ou Cancelar para voltar.");

        // Exibindo o alerta e capturando a resposta
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            ScreenManager.getInstance().showLoginScreen();
            System.out.println("Usuário confirmou a ação!");
        } else {
            System.out.println("Usuário cancelou a ação!");
        }

    }

    @Override
    public void receiveData(Object data) {
        if (data instanceof Usuario) {
            this.usuario = (Usuario) data;
            nomeUsuario.setText(usuario.getNome().split(" ")[0]);

        }
    }

    public void irTelaConfiguracoes(ActionEvent event) {
        ScreenManager.getInstance().showConfiguracoesUsuarioScreen();
    }

    public void irParaTelaDeCotas(ActionEvent event) {
        ScreenManager.getInstance().showTelaDeCotasScreen();
    }
}

