package br.ufrpe.timeshare.gui.application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ScreenManager {

    private static ScreenManager instance;
    private Stage mainStage;

    private Scene telaCadastro;
    private Scene telaLogin;

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }

        return instance;
    }

    private ScreenManager() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/cadastro.fxml"));
            Parent root = loader.load();
            telaCadastro = new Scene(root);

            loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/login.fxml"));
            root = loader.load();
            telaLogin = new Scene(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void showCadastroScreen() {
        mainStage.setScene(telaCadastro);
        mainStage.show();
    }

    public void showLoginScreen() {
        mainStage.setScene(telaLogin);
        mainStage.show();
    }
}