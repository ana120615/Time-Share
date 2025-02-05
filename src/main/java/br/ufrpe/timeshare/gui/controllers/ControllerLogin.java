package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.dados.IRepositorioUsuario;
import br.ufrpe.timeshare.dados.RepositorioUsuarios;
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

public class ControllerLogin {
    private IRepositorioUsuario repositorioUsuario;
    private ControladorLogin controladorLogin;

    {
        this.repositorioUsuario = RepositorioUsuarios.getInstancia();
        this.controladorLogin = new ControladorLogin(repositorioUsuario);
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

//    @FXML
//    private void irParaCadastro() {
//        ScreenManager.getInstance().showCadastroScreen();
//    }

    @FXML
    public void irTelaCadastro(ActionEvent event) {
        ScreenManager.getInstance().showCadastroScreen();
        txtUser.clear();
        txtPassword.clear();
    }

    @FXML
    public void logar(ActionEvent event) {
        String email = txtUser.getText();
        String senha = txtPassword.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Campos obrigatórios não preenchidos");
            alert.setContentText("Por favor, preencha todos os campos.");
            alert.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro
            alert.showAndWait();
        } else {

            try {
                Usuario usuario = controladorLogin.efetuarLogin(email, senha);
                if (usuario.getTipo().equals(TipoUsuario.ADMINISTRADOR)) {

                    txtUser.clear();
                    txtPassword.clear();

                } else {
                    ScreenManager.getInstance().showUsuarioComumPrincipalScreen();
                    txtUser.clear();
                    txtPassword.clear();
                }
            } catch (UsuarioNaoExisteException | SenhaInvalidaException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao cadastrar usuário");
                alert.setContentText(e.getMessage());
                alert.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro
                alert.showAndWait();
            }
        }

    }
}
