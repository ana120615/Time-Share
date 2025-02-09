package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.dados.IRepositorioUsuario;
import br.ufrpe.timeshare.dados.RepositorioUsuarios;
import br.ufrpe.timeshare.excecoes.UsuarioNaoExisteException;
import br.ufrpe.timeshare.excecoes.UsuarioNaoPermitidoException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.negocio.ControladorUsuarioGeral;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.Optional;


public class ControllerConfiguracoes implements ControllerBase {
    private IRepositorioUsuario repositorioUsuarios;
    private ControladorUsuarioGeral controladorUsuarioGeral;

    {
        repositorioUsuarios = RepositorioUsuarios.getInstancia();
        controladorUsuarioGeral = new ControladorUsuarioGeral(repositorioUsuarios);
    }

    private Usuario usuario;

    @FXML
    private Label nome;

    @FXML
    private Label email;

    @FXML
    private Label senha;

    @FXML
    private Label dataNascimento;

    @FXML
    private Label cpf;

    // Recebe dados, nesse caso, o objeto Usuario
    @Override
    public void receiveData(Object data) {
        if (data instanceof Usuario) {
            this.usuario = (Usuario) data;
            nome.setText(usuario.getNome());
            email.setText(usuario.getEmail());
            senha.setText(usuario.getSenha());
            dataNascimento.setText(usuario.getDataNascimento().toString());
            cpf.setText(Long.toString(usuario.getId()));
        }
    }

    @FXML
    public void editarNome(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Editar nome");
        dialog.setHeaderText("Editar nome");
        dialog.setContentText("Novo nome: ");
        Optional<String> result = dialog.showAndWait();

        if (result.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Campo obrigatório não preenchido");
            alert.setContentText("Operacao nao realizada");
            alert.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro
            alert.showAndWait();
        } else {
            try {
                String novoNome = result.orElse("");
                usuario.setNome(novoNome);
                controladorUsuarioGeral.alterarNomeUsuario(usuario.getId(), novoNome, usuario.getTipo());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Edicao concluída.");
                alert.setHeaderText("Cadastro realizado com sucesso!");
                alert.setContentText("Usuário " + usuario.getNome() + " cadastrado como " + usuario.getTipo() + " com nascimento em " + dataNascimento + ".");
                alert.getDialogPane().setStyle("-fx-background-color: #ccffcc;"); // Verde claro
                alert.showAndWait();
                ScreenManager.getInstance().showScreen("ConfiguracoesUsuario");
            } catch (UsuarioNaoExisteException | UsuarioNaoPermitidoException | NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao cadastrar usuário");
                alert.setContentText(e.getMessage());
                alert.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao alterar nome");
                alert.setContentText(e.getMessage());
                alert.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro
                alert.showAndWait();
            }
        }

    }

    @FXML
    public void editarEmail(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Editar email");
        dialog.setHeaderText("Editar email");
        dialog.setContentText("Novo email: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Campo obrigatório não preenchido");
            alert.setContentText("Operacao nao realizada");
            alert.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro
            alert.showAndWait();
        } else {
            try {
                String novoEmail = result.orElse("");
                usuario.setEmail(novoEmail);
                controladorUsuarioGeral.alterarEmailUsuario(usuario.getId(), novoEmail, usuario.getTipo());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Edicao concluída.");
                alert.setHeaderText("Operacao realizada com sucesso!");
                alert.setContentText("Usuário " + usuario.getNome() + " cadastrado como " + usuario.getTipo() + " com nascimento em " + dataNascimento + ".");
                alert.getDialogPane().setStyle("-fx-background-color: #ccffcc;"); // Verde claro
                alert.showAndWait();
                ScreenManager.getInstance().showScreen("ConfiguracoesUsuario");
            } catch (UsuarioNaoExisteException | UsuarioNaoPermitidoException | NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao editar usuário");
                alert.setContentText(e.getMessage());
                alert.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao alterar email");
                alert.setContentText(e.getMessage());
                alert.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void editarSenha(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Editar senha");
        dialog.setHeaderText("Editar senha");
        dialog.setContentText("Nova senha: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Campo obrigatório não preenchido");
            alert.setContentText("Operacao nao realizada");
            alert.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro
            alert.showAndWait();
        } else {
            try {
                String novaSenha = result.orElse("");
                usuario.setEmail(novaSenha);
                controladorUsuarioGeral.alterarSenhaUsuario(usuario.getEmail(), novaSenha, usuario.getTipo());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Edicao concluída.");
                alert.setHeaderText("Operacao realizada com sucesso!");
                alert.setContentText("Usuário " + usuario.getNome() + " cadastrado como " + usuario.getTipo() + " com nascimento em " + dataNascimento + ".");
                alert.getDialogPane().setStyle("-fx-background-color: #ccffcc;"); // Verde claro
                alert.showAndWait();
                ScreenManager.getInstance().showScreen("ConfiguracoesUsuario");
            } catch (UsuarioNaoExisteException | UsuarioNaoPermitidoException | NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao editar usuário");
                alert.setContentText(e.getMessage());
                alert.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao alterar senha");
                alert.setContentText(e.getMessage());
                alert.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void editarDataNascimento(ActionEvent event) {
        Dialog<LocalDate> dialog = new Dialog<>();
        dialog.setTitle("Editar data de nascimento");
        dialog.setHeaderText("Editar data de nascimento");
        dialog.setContentText("Data de nascimento: ");

        DatePicker dataNascimento = new DatePicker();

        //adicionar botões de OK e Cancelar
        ButtonType confirmarBotao = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmarBotao, ButtonType.CANCEL);

        //layout do diálogo
        VBox vbox = new VBox(10, new Label("Nova data de nascimento:"), dataNascimento);
        vbox.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(vbox);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmarBotao) {
                return dataNascimento.getValue();
            }
            return null;
        });


        Optional<LocalDate> resultado = dialog.showAndWait();

        if (resultado.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Campo obrigatório não preenchido");
            alert.setContentText("Operacao nao realizada");
            alert.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro
            alert.showAndWait();
        } else {
            try {
                LocalDate data = resultado.get();
                usuario.setDataNascimento(data);
                controladorUsuarioGeral.alterarDataAniversario(usuario.getId(), data, usuario.getTipo());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Edicao concluída.");
                alert.setHeaderText("Operacao realizada com sucesso!");
                alert.setContentText("Usuário " + usuario.getNome() + " cadastrado como " + usuario.getTipo() + " com nascimento em " + dataNascimento + ".");
                alert.getDialogPane().setStyle("-fx-background-color: #ccffcc;"); // Verde claro
                alert.showAndWait();
                ScreenManager.getInstance().showScreen("ConfiguracoesUsuario");
            } catch (UsuarioNaoExisteException | UsuarioNaoPermitidoException | NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao editar usuário");
                alert.setContentText(e.getMessage());
                alert.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao alterar data de nascimento");
                alert.setContentText(e.getMessage());
                alert.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro
                alert.showAndWait();
            }
        }
    }

}
