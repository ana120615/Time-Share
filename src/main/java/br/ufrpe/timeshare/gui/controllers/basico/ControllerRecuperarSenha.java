package br.ufrpe.timeshare.gui.controllers.basico;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.Properties;
import java.util.UUID;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import br.ufrpe.timeshare.excecoes.UsuarioNaoExisteException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.negocio.ControladorUsuarioGeral;
import br.ufrpe.timeshare.negocio.beans.Usuario;

public class ControllerRecuperarSenha {

    @FXML
    private TextField emailField;

    @FXML
    private TextField tokenField;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnValidar;

    @FXML
    private Button btnVoltar;

    @FXML
    private Label mensagemLabel;

    private String tokenGerado;
    private ControladorUsuarioGeral controladorUsuarioGeral;
    private String email;
    private Usuario usuario;

    @FXML
    public void initialize() {
        this.controladorUsuarioGeral = new ControladorUsuarioGeral();
        limparCampos(); //limpa a tela ao inicializar
    }

    private void limparCampos() {
        emailField.clear();
        tokenField.clear();
        mensagemLabel.setText("");
    }

    // Para mensagem de erro
    private void exibirAlertaErro(String titulo, String header, String contentText) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(header);
        alerta.setContentText(contentText);
        alerta.getDialogPane().setStyle("-fx-background-color:  #ffcccc;"); // Vermelho claro
        alerta.showAndWait();
    }


    @FXML
    public void onBuscar() {
        this.email = emailField.getText();

        try {
            this.usuario = controladorUsuarioGeral.procurarUsuarioPorEmail(email);
            if (isValidEmail(email)) {
                if (this.usuario != null) {
                    // Gerar um token aleatório
                    tokenGerado = UUID.randomUUID().toString();
                    // Enviar o token para o e-mail
                    sendEmail(email, tokenGerado);

                    mensagemLabel.setText("Token enviado para o e-mail.");
                } else {
                    mensagemLabel.setText("E-mail não encontrado.");
                }
            } else {
                mensagemLabel.setText("E-mail inválido.");
            }
        } catch (UsuarioNaoExisteException e) {
            exibirAlertaErro("Erro", "Problema com a procura do usuário por e-mail", e.getMessage());
        } catch (Exception e) {
            exibirAlertaErro("Erro", "Erro inesperado", e.getMessage());
        }
    }

    @FXML
    public void onValidar() {
        String tokenInserido = tokenField.getText();
        String senha;
        if (usuario != null) {
            senha = usuario.getSenha();

            if (this.usuario != null && tokenGerado != null && tokenGerado.equals(tokenInserido)) {
                mensagemLabel.setText("Token válido. Senha: " + senha);
            } else {
                mensagemLabel.setText("Token inválido.");
            }
        } else {
            exibirAlertaErro("Erro", "Problema com cadastro", "Usuário com esse e-mail não cadastrado.");
        }
    }

    @FXML
    public void onVoltar() {
        ScreenManager.getInstance().showLoginScreen();
        limparCampos();
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    private void sendEmail(String toEmail, String token) {
        String host = "smtp.gmail.com"; // Servidor SMTP do Gmail
        final String username = "lailasamara67@gmail.com"; // Meu email (remetente)
        final String password = "zqzi mcdc ssjc trhe"; // Minha senha para envio

        // Propriedades de configuração para SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587"); // Porta SMTP para TLS
        props.put("mail.smtp.auth", "true"); // Habilitar autenticação SMTP
        props.put("mail.smtp.starttls.enable", "true"); // Habilitar TLS para segurança

        // Criando a sessão de e-mail com a autenticação SMTP
        Session session = Session.getDefaultInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password); // Usando as credenciais
            }
        });

        try {
            // Criando a mensagem de e-mail
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // Definindo o remetente
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail)); // Destinatário
            message.setSubject("Token de Recuperação de Senha"); // Assunto do e-mail
            message.setText("Seu token para recuperação de senha é: " + token); // Corpo do e-mail

            // Enviando o e-mail
            Transport.send(message);
            System.out.println("E-mail enviado com sucesso!");
        } catch (MessagingException e) {
            e.printStackTrace();
            mensagemLabel.setText("Erro ao enviar o e-mail.");
            exibirAlertaErro("Erro", "Falha no envio do e-mail", e.getMessage());
        }
    }
}
