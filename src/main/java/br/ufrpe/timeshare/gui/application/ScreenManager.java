package br.ufrpe.timeshare.gui.application;

import br.ufrpe.timeshare.gui.controllers.ControllerBase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScreenManager {

    private static ScreenManager instance;
    private Object data;
    private Stage mainStage;

    private final Map<String, Scene> telas = new HashMap<>();
    private final Map<String, FXMLLoader> loaders = new HashMap<>();

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    private ScreenManager() {
        carregarTelas();
    }

    private void carregarTelas() {
        carregarTela("Cadastro", "/br/ufrpe/timeshare/gui/application/cadastro.fxml");
        carregarTela("Login", "/br/ufrpe/timeshare/gui/application/login.fxml");
        carregarTela("UsuarioComumPrincipal", "/br/ufrpe/timeshare/gui/application/usuariocomumtelaprincipal.fxml");
        carregarTela("AdmPrincipal", "/br/ufrpe/timeshare/gui/application/usuarioadmtelaprincipal.fxml");
        carregarTela("ConfiguracoesUsuario", "/br/ufrpe/timeshare/gui/application/configuracoesusuario.fxml");
        carregarTela("CadastrarBem","/br/ufrpe/timeshare/gui/application/telaCadastroBens.fxml");
        carregarTela("ListarBens", "/br/ufrpe/timeshare/gui/application/listarBens.fxml");  
    }

    private void carregarTela(String nome, String caminhoFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent root = loader.load();
            telas.put(nome, new Scene(root));
            loaders.put(nome, loader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void showScreen(String nomeTela) {
        if (mainStage != null && telas.containsKey(nomeTela)) {
            mainStage.setScene(telas.get(nomeTela));
            mainStage.show();

            FXMLLoader loader = loaders.get(nomeTela);
            if (loader != null) {
                Object controller = loader.getController();
                if (controller instanceof ControllerBase) {
                    ((ControllerBase) controller).receiveData(data);
                }
            }
        }
    }

    public void showCadastroScreen() {
        showScreen("Cadastro");
    }

    public void showLoginScreen() {
        showScreen("Login");
    }

    public void showUsuarioComumPrincipalScreen() {
        showScreen("UsuarioComumPrincipal");
    }

    public void showConfiguracoesUsuarioScreen() {
        showScreen("ConfiguracoesUsuarioComum");
    }

    public void showAdmPrincipalScreen() {
        showScreen("AdmPrincipal");
    }
    public void showCadastrarBemScreen(){
        showScreen("CadastrarBem");
    }
    public void showListarBensScreen() {
        showScreen("ListarBens");
    }
}
