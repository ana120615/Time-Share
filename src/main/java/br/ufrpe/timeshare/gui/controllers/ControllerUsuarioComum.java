package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.gui.application.ScreenManager;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Optional;

public class
ControllerUsuarioComum {

    @FXML
    private VBox vboxContainer; // Menu lateral (VBox)

    @FXML
    private ImageView imageView; // Logo dentro do menu

    private boolean isExpanded = true;

    @FXML
    private TabPane tabPaneUsuarioComumTelaPrincipal; // Estado inicial
    @FXML
    private Tab tabGerenciamentoReservas;
    @FXML
    private Tab tabBensCotas;


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

    @FXML
    public void irTelaConfiguracoesUsuarioComum(ActionEvent event) {
        ScreenManager.getInstance().showConfiguracoesUsuarioComumScreen();
    }

    @FXML
    public void irTelaPrincipalUsuarioComum(ActionEvent event) {
        ScreenManager.getInstance().showUsuarioComumPrincipalScreen();
    }

    public void mudarAbaBensCotas (ActionEvent event) {
        tabPaneUsuarioComumTelaPrincipal.getSelectionModel().select(tabBensCotas);
    }

    public void mudarAbaReservas (ActionEvent event) {
        tabPaneUsuarioComumTelaPrincipal.getSelectionModel().select(tabGerenciamentoReservas);
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

    public void irParaTelaDeConfiguracoes(ActionEvent event) {
        ScreenManager.getInstance().showConfiguracoesUsuarioComumScreen();
    }

}
