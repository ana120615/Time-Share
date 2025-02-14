package br.ufrpe.timeshare.gui.controllers.basico;

import br.ufrpe.timeshare.excecoes.SenhaInvalidaException;
import br.ufrpe.timeshare.excecoes.UsuarioNaoExisteException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.negocio.ControladorLogin;
import br.ufrpe.timeshare.negocio.beans.TipoUsuario;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ControllerLogin implements ControllerBase {
    private ControladorLogin controladorLogin;
    private Object data;

    {
        this.controladorLogin = new ControladorLogin();
    }

    @FXML
    private TextField txtUser;
    @FXML
    private TextField txtPassword;

    @FXML
    private void handleLogin() {
        System.out.println("Usuário: " + txtUser.getText());
        System.out.println("Senha: " + txtPassword.getText());
    }

    @FXML
    public void irTelaCadastro(ActionEvent event) {
        ScreenManager.getInstance().showCadastroScreen();
        limparCampos();
    }

    @FXML
    public void logar(ActionEvent event) {
        String email = txtUser.getText();
        String senha = txtPassword.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            exibirAlerta("Erro", "Campos obrigatórios não preenchidos", "Por favor, preencha todos os campos.");
        } else {
            try {
                Usuario usuario = controladorLogin.efetuarLogin(email, senha);

                // Armazena os dados no ScreenManager antes de mudar a tela
                ScreenManager.getInstance().setData(usuario);

                if (usuario.getTipo().equals(TipoUsuario.ADMINISTRADOR)) {
                    ScreenManager.getInstance().showAdmPrincipalScreen();
                } else {
                    ScreenManager.getInstance().showUsuarioComumPrincipalScreen();
                }

                limparCampos();

            } catch (UsuarioNaoExisteException | SenhaInvalidaException e) {
                exibirAlerta("Erro", "Erro ao realizar login", e.getMessage());
            }
        }
    }

    @Override
    public void receiveData(Object data) {
        this.data = data;

        if (data instanceof Usuario usuario) {
            System.out.println("Usuário recebido: " + usuario.getNome());
        } else {
            System.out.println("Dado recebido: " + (data != null ? data.toString() : "null"));
        }
    }

    private void limparCampos() {
        txtUser.clear();
        txtPassword.clear();
    }

    private void exibirAlerta(String titulo, String cabecalho, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.getDialogPane().setStyle("-fx-background-color: #ffcccc;");
        alert.showAndWait();
    }
}
