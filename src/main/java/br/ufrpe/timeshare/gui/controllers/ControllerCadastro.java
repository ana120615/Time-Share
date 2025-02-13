package br.ufrpe.timeshare.gui.controllers;


import br.ufrpe.timeshare.excecoes.UsuarioJaExisteException;
import br.ufrpe.timeshare.excecoes.UsuarioNaoPermitidoException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.negocio.ControladorUsuarioGeral;
import br.ufrpe.timeshare.negocio.beans.TipoUsuario;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class ControllerCadastro {
    private ControladorUsuarioGeral controladorUsuarioGeral;

    {
        controladorUsuarioGeral = new ControladorUsuarioGeral();
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

    private void exibirAlertaErro(String titulo, String header, String contentText) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(header);
        alerta.setContentText(contentText);
        alerta.getDialogPane().setStyle("-fx-background-color:  #ffcccc;"); // Vermelho claro
        alerta.showAndWait();
    }

    private void exibirAlertaInformation(String titulo, String header, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contentText);
        alert.getDialogPane().setStyle("-fx-background-color: #ccffcc;"); // Verde claro
        alert.showAndWait();
    }

    @FXML
    private void handleCadastro() {
        String nome = nomeField.getText();
        String cpf = cpfField.getText();
        LocalDate dataNascimento = dataNascimentoPicker.getValue();
        String senha = senhaField.getText();
        String gmail = emailField.getText();
        String tipoUsuario = tipoUsuarioCombo.getValue();

        boolean valorInvalido = false;
        for (char c : cpf.toCharArray()) {
            if (!Character.isDigit(c)) {
                valorInvalido = true;
            }
        }

        if (nome.isEmpty() || cpf.isEmpty() || dataNascimento == null || senha.isEmpty() || gmail.isEmpty() || tipoUsuario == null) {
            exibirAlertaErro("Erro", "Campos obrigatórios não preenchidos", "Por favor, preencha todos os campos");
        } else if (valorInvalido) {
            exibirAlertaErro("Erro", "CPF invalido", "Por favor, insira apenas numeros");
        } else {
            try {

                Usuario usuario = new Usuario(Long.parseLong(cpf), nome, gmail, senha, dataNascimento,
                        tipoUsuario.equals("Administrador") ? TipoUsuario.ADMINISTRADOR : TipoUsuario.COMUM);

                controladorUsuarioGeral.cadastrar(usuario);

                exibirAlertaInformation("Cadastro concluído", "Cadastro realizado com sucesso!", ("Usuário " + nome + " cadastrado como " + tipoUsuario + " com nascimento em " + dataNascimento + "."));

                // Limpar os campos do formulário após cadastro
                nomeField.clear();
                cpfField.clear();
                dataNascimentoPicker.setValue(null);
                senhaField.clear();
                emailField.clear();
                tipoUsuarioCombo.getSelectionModel().clearSelection();

            } catch (UsuarioJaExisteException | UsuarioNaoPermitidoException e) {
                exibirAlertaErro("Erro", "Erro ao cadastrar usuário", e.getMessage());
            }

            System.out.println(controladorUsuarioGeral.listarUsuarioComum());
        }
    }

    @FXML
    public void irParaTelaLogin() {
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

