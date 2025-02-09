package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.dados.IRepositorioUsuario;
import br.ufrpe.timeshare.dados.RepositorioUsuarios;
import br.ufrpe.timeshare.negocio.ControladorUsuarioGeral;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


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
    public void editarNome(String novoNome) {

    }

    @FXML
    public void editarEmail(String novoEmail) {

    }

    @FXML
    public void editarSenha(String novaSenha) {

    }

    @FXML
    public void editarDataNascimento(String novaDataNascimento) {

    }

}
