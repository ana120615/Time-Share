package br.ufrpe.time_share.negocio;

import br.ufrpe.time_share.dados.IRepositorioUsuario;
import br.ufrpe.time_share.excecoes.UsuarioDeTipoIlegivel;
import br.ufrpe.time_share.excecoes.UsuarioJaExisteException;
import br.ufrpe.time_share.excecoes.UsuarioNaoExisteException;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;

import java.time.LocalDate;

public class ControladorUsuarioComum {
    private IRepositorioUsuario repositorio;

    public ControladorUsuarioComum(IRepositorioUsuario instanciaInterface) {
        this.repositorio = instanciaInterface;
    }

    public void cadastrar(int cpf, String nome, String email, String senha, LocalDate dataNascimento) throws UsuarioJaExisteException {
        Usuario usuarioComum = new Usuario(cpf, nome, email, senha, dataNascimento, TipoUsuario.COMUM);
        if (!this.repositorio.existe(usuarioComum)) {
            this.repositorio.cadastrar(usuarioComum);
        } else {
            throw new UsuarioJaExisteException("Usuario ja cadastrado", cpf, email);
        }
    }

    public void cadastrar(Usuario usuarioComum) throws UsuarioJaExisteException {
        if (!this.repositorio.existe(usuarioComum)) {
            this.repositorio.cadastrar(usuarioComum);
        } else {
            throw new UsuarioJaExisteException("Usuario ja cadastrado", usuarioComum.getCpf(), usuarioComum.getEmail());
        }
    }

    public Usuario procurarUsuarioPorCpf(int cpf) {
        return this.repositorio.buscarUsuarioPorCpf(cpf);
    }

    public Usuario procurarUsuarioPorEmail(String email) {
        return this.repositorio.buscarUsuarioPorEmail(email);
    }

    public void remover(int cpf) throws UsuarioNaoExisteException, UsuarioDeTipoIlegivel {
        Usuario usuario = procurarUsuarioPorCpf(cpf);
        if (usuario != null) {
            if (usuario.getTipo().equals(TipoUsuario.COMUM)) {
                this.repositorio.remover(usuario);
            } else {
                throw new UsuarioDeTipoIlegivel("O usuario em questao e de do tipo Administrador.");
            }
        } else {
            throw new UsuarioNaoExisteException("Usuario nao encontrado.");
        }
    }

}


