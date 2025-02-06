package br.ufrpe.timeshare.gui.controllers;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ControllerAdm {

    @FXML
    private VBox vboxContainer; // Menu lateral (VBox)

    @FXML
    private ImageView imageView; // Logo dentro do menu

    private boolean isExpanded = true; // Estado inicial

    @FXML
    private void toggleVBox() {
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
}

