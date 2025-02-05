package br.ufrpe.timeshare.gui.application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ScreenManager {
    private Stage stage;

    public ScreenManager(Stage stage) {
        this.stage = stage;
    }

    public void showLoginScreen() {
        loadScreen("login.fxml", "Login");
    }

    public void showCadastroScreen() {
        loadScreen("cadastro.fxml", "Cadastro");
    }

    public void showHomeScreen() {
        loadScreen("home.fxml", "Home");
    }

    private void loadScreen(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}