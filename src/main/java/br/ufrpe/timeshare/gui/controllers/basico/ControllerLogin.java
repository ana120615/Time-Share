package br.ufrpe.timeshare.gui.controllers.basico;

import br.ufrpe.timeshare.excecoes.*;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.ControladorLogin;
import br.ufrpe.timeshare.negocio.ControladorUsuarioGeral;
import br.ufrpe.timeshare.negocio.beans.TipoUsuario;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ControllerLogin implements ControllerBase {
    private ControladorLogin controladorLogin;

    private ControladorUsuarioGeral controladorUsuarioGeral;
    private ControladorBens controladorBens;

    private Object data;

    {
        this.controladorLogin = new ControladorLogin();
        this.controladorUsuarioGeral = new ControladorUsuarioGeral();
        this.controladorBens = new ControladorBens();

        Usuario admin = null;
        try {
            admin = new Usuario(12345678901L, "admin", "admin@gmail.com", "senha123", LocalDate.of(2000, 01, 01), TipoUsuario.ADMINISTRADOR);
            controladorUsuarioGeral.cadastrar(admin);
            controladorUsuarioGeral.cadastrar(12345678902L, "comum", "comum@gmail.com", "senha123", LocalDate.of(2000, 01, 01), TipoUsuario.COMUM);
        } catch (UsuarioJaExisteException | UsuarioNaoPermitidoException | DadosInsuficientesException e) {
            System.out.println(e.getMessage());
        }

        try {
            controladorBens.cadastrar(1111, "Bem 1", "Descrição do bem 1", "Recife-PE", 4, admin, LocalDateTime.now(), 15, 5425, "Imagem 1");
            controladorBens.ofertarBem(1111);
        } catch (BemNaoExisteException | UsuarioNaoPermitidoException | QuantidadeDeCotasExcedidasException | BemJaExisteException | UsuarioNaoExisteException | BemJaOfertadoException e) {
            System.out.println(e.getMessage());
        }
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
