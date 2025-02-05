package br.ufrpe.timeshare.gui.controllers;


import br.ufrpe.timeshare.dados.IRepositorioUsuario;
import br.ufrpe.timeshare.dados.RepositorioUsuarios;
import br.ufrpe.timeshare.excecoes.DadosInsuficientesException;
import br.ufrpe.timeshare.excecoes.UsuarioJaExisteException;
import br.ufrpe.timeshare.excecoes.UsuarioNaoPermitidoException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.negocio.ControladorUsuarioGeral;
import br.ufrpe.timeshare.negocio.beans.TipoUsuario;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDate;

public class ControllerCadastro {
    private static IRepositorioUsuario repositorioUsuarios;
    private ControladorUsuarioGeral controladorUsuarioGeral;

    {
        repositorioUsuarios = RepositorioUsuarios.getInstancia();
        controladorUsuarioGeral = new ControladorUsuarioGeral(repositorioUsuarios);
    }

    @FXML
    private TextField nomeField;
    @FXML
    private TextField cpfField;
    @FXML
    private DatePicker dataNascimentoPicker;
    @FXML
    private PasswordField senhaField;
    @FXML
    private TextField emailField;
    @FXML
    private ComboBox<String> tipoUsuarioCombo;

    @FXML
    private void handleCadastro() {
        String nome = nomeField.getText();
        String cpf = cpfField.getText();
        LocalDate dataNascimento = dataNascimentoPicker.getValue();
        String senha = senhaField.getText();
        String gmail = emailField.getText();
        String tipoUsuario = tipoUsuarioCombo.getValue();

        if (nome.isEmpty() || cpf.isEmpty() || dataNascimento == null || senha.isEmpty() || gmail.isEmpty() || tipoUsuario == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Campos obrigatórios não preenchidos");
            alert.setContentText("Por favor, preencha todos os campos.");
            alert.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro
            alert.showAndWait();
        } else {
            try {
                Usuario usuario = new Usuario(Long.parseLong(cpf), nome, gmail, senha, dataNascimento,
                        tipoUsuario.equals("ADMINISTRADOR") ? TipoUsuario.ADMINISTRADOR : TipoUsuario.COMUM);

                controladorUsuarioGeral.cadastrar(usuario);

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Cadastro concluído");
                alert.setHeaderText("Cadastro realizado com sucesso!");
                alert.setContentText("Usuário " + nome + " cadastrado como " + tipoUsuario + " com nascimento em " + dataNascimento + ".");
                alert.getDialogPane().setStyle("-fx-background-color: #ccffcc;"); // Verde claro
                alert.showAndWait();

                // Limpar os campos do formulário após cadastro
                nomeField.clear();
                cpfField.clear();
                dataNascimentoPicker.setValue(null);
                senhaField.clear();
                emailField.clear();
                tipoUsuarioCombo.getSelectionModel().clearSelection();

            } catch (UsuarioJaExisteException | UsuarioNaoPermitidoException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao cadastrar usuário");
                alert.setContentText(e.getMessage());
                alert.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro
                alert.showAndWait();
            }

            System.out.println(controladorUsuarioGeral.listarUsuarioComum());
        }
    }

    @FXML
    public void irParaTelaLogin () {
        ScreenManager.getInstance().showLoginScreen();
    }

    @FXML
    private void handleEsqueceuSenha() {
        System.out.println("Esqueceu senha");
    }

    @FXML
    private void handleLogin() {
        System.out.println("Login");
    }



}

