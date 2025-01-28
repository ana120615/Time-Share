package br.ufrpe.time_share.negocio;

import br.ufrpe.time_share.dados.IRepositorioUsuario;
import br.ufrpe.time_share.excecoes.SenhaInvalidaException;
import br.ufrpe.time_share.excecoes.UsuarioNaoExisteException;
import br.ufrpe.time_share.negocio.beans.Usuario;

public class ControladorLogin {
    private IRepositorioUsuario repositorio;

    public ControladorLogin(IRepositorioUsuario instanciaInterface) {
        this.repositorio = instanciaInterface;
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
