package br.ufrpe.timeshare.gui.application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ScreenManager.getInstance().setMainStage(primaryStage);
        ScreenManager.getInstance().showListarBensScreen();

        primaryStage.show();
    }
}
