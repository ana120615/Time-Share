package br.ufrpe.timeshare.negocio;

import br.ufrpe.timeshare.dados.IRepositorioUsuario;
import br.ufrpe.timeshare.dados.RepositorioUsuarios;
import br.ufrpe.timeshare.excecoes.SenhaInvalidaException;
import br.ufrpe.timeshare.excecoes.UsuarioNaoExisteException;
import br.ufrpe.timeshare.negocio.beans.Usuario;

public class ControladorLogin {
    private IRepositorioUsuario repositorio;

    public ControladorLogin() {
        this.repositorio = RepositorioUsuarios.getInstancia();
    }

    public Usuario efetuarLogin(String email, String senha) throws UsuarioNaoExisteException, SenhaInvalidaException {
        Usuario procurado = this.repositorio.buscarUsuarioPorEmail(email);
        if (procurado == null) {
            throw new UsuarioNaoExisteException("Usuario nao encontrado.");
        } else {
            if (procurado.getSenha().equals(senha)) {
                return procurado;
            } else {
                throw new SenhaInvalidaException("Senha incorreta.");
            }
        }
    }

}
