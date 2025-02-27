package br.ufrpe.timeshare.gui.application;

import br.ufrpe.timeshare.gui.controllers.basico.ControllerAjuda;
import br.ufrpe.timeshare.gui.controllers.basico.ControllerBase;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScreenManager {

    private static ScreenManager instance;
    private final Map<String, Scene> telas = new HashMap<>();
    private final Map<String, FXMLLoader> loaders = new HashMap<>();
    private Object data;
    private Stage mainStage;

    private ScreenManager() {
        carregarTelas();
    }

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    private void carregarTelas() {
        carregarTela("Cadastro", "/br/ufrpe/timeshare/gui/application/CadastroUsuario.fxml");
        carregarTela("Login", "/br/ufrpe/timeshare/gui/application/LoginUsuario.fxml");
        carregarTela("UsuarioComumPrincipal", "/br/ufrpe/timeshare/gui/application/UsuarioComumTelaPrincipal.fxml");
        carregarTela("AdmPrincipal", "/br/ufrpe/timeshare/gui/application/UsuarioAdmTelaPrincipal.fxml");
        carregarTela("ConfiguracoesUsuario", "/br/ufrpe/timeshare/gui/application/ConfiguracoesUsuario.fxml");
        carregarTela("CadastroBens", "/br/ufrpe/timeshare/gui/application/TelaCadastroBens.fxml");
        carregarTela("TelaDeVendas", "/br/ufrpe/timeshare/gui/application/TelaDeVenda.fxml");
        carregarTela("TelaDeBensECotas", "/br/ufrpe/timeshare/gui/application/TelaDeBensECotas.fxml");
        carregarTela("TelaMinhasCotas", "/br/ufrpe/timeshare/gui/application/TelaMinhasCotas.fxml");
        carregarTela("TelaRealizarReserva", "/br/ufrpe/timeshare/gui/application/RealizarReserva.fxml");
        carregarTela("TelaMinhasReservas", "/br/ufrpe/timeshare/gui/application/MinhasReservasComum.fxml");
        carregarTela("TelaEstadia", "/br/ufrpe/timeshare/gui/application/Estadia.fxml");
        carregarTela("TelaBensMaisVendidos", "/br/ufrpe/timeshare/gui/application/CharTelaBensMaisVendidos.fxml");
        carregarTela("TelaRecuperarSenha", "/br/ufrpe/timeshare/gui/application/RecuperarSenha.fxml");
        carregarTela("TelaHistoricoUsoBens", "/br/ufrpe/timeshare/gui/application/CharTelaBensMaisUsados.fxml");
        carregarTela("TelaListarReservasAdmin", "/br/ufrpe/timeshare/gui/application/TelaListarReservaAdmin.fxml");
        carregarTela("TelaListarEstadiasAdmin", "/br/ufrpe/timeshare/gui/application/TelaListarEstadiasAdmin.fxml");
        carregarTela("TelaAjuda", "/br/ufrpe/timeshare/gui/application/Ajuda.fxml");
    }

    private void carregarTela(String nome, String caminhoFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            telas.put(nome, scene);
            loaders.put(nome, loader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
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

    public void showAjudaScreen(Stage telaAnterior) {
        if (telaAnterior != null) {
            FXMLLoader loader = loaders.get("TelaAjuda");
            ControllerAjuda controller = loader.getController();
            controller.receiveData(telaAnterior);
            showScreen("TelaAjuda");
        }
    }

    public void showRecuperarSenhaScreen() {
        showScreen("TelaRecuperarSenha");
    }

    public void showEstadiaScreen() {
        showScreen("TelaEstadia");
    }

    public void showMinhasReservasScreen() {
        showScreen("TelaMinhasReservas");
    }

    public void showRealizarReservaScreen() {
        showScreen("TelaRealizarReserva");
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
        showScreen("ConfiguracoesUsuario");
    }

    public void showAdmPrincipalScreen() {
        showScreen("AdmPrincipal");
    }

    public void showCadastroBensScreen() {
        showScreen("CadastroBens");
    }

    public void showListarBensScreen() {
        showScreen("ListarBens");
    }

    public void showTelaDeVendasScreen() {
        showScreen("TelaDeVendas");
    }

    public void showTelaDeBensECotasScreen() {
        showScreen("TelaDeBensECotas");
    }

    public void showTelaMinhasCotasScreen() {
        showScreen("TelaMinhasCotas");
    }

    public void showTelaBensMaisVendidos() {
        showScreen("TelaBensMaisVendidos");
    }

    public void showTelaBensMaisUsados() {
        showScreen("TelaHistoricoUsoBens");
    }

    public void showTelaListarReservasAdminScreen() {
        showScreen("TelaListarReservasAdmin");
    }

    public void showTelaListarEstadiasAdminScreen() { showScreen("TelaListarEstadiasAdmin");}
}
