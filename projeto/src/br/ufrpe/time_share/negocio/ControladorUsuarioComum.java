package br.ufrpe.time_share.negocio;

import br.ufrpe.time_share.dados.IRepositorioUsuario;
import br.ufrpe.time_share.excecoes.UsuarioDeTipoIlegivel;
import br.ufrpe.time_share.excecoes.UsuarioJaExisteException;
import br.ufrpe.time_share.excecoes.UsuarioNaoExisteException;
import br.ufrpe.time_share.negocio.beans.Usuario;
import br.ufrpe.time_share.negocio.beans.UsuarioComum;

import java.time.LocalDate;

public class ControladorUsuarioComum {
    private IRepositorioUsuario repositorio;

    public ControladorUsuarioComum(IRepositorioUsuario instanciaInterface) {
        this.repositorio = instanciaInterface;
    }

    public void cadastrar(int cpf, String nome, String email, String senha, LocalDate dataNascimento) throws UsuarioJaExisteException {
        UsuarioComum usuarioComum = new UsuarioComum(cpf, nome, email, senha, dataNascimento);
        if (!this.repositorio.existe(usuarioComum)) {
            this.repositorio.cadastrar(usuarioComum);
        } else {
            throw new UsuarioJaExisteException("Usuario ja cadastrado", cpf, email);
        }
    }

    public void cadastrar(UsuarioComum usuarioComum) throws UsuarioJaExisteException {
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
            if (usuario instanceof UsuarioComum) {
                this.repositorio.remover(usuario);
            } else {
                throw new UsuarioDeTipoIlegivel("O usuario em questao e de do tipo Administrador.");
            }
        } else {
            throw new UsuarioNaoExisteException("Usuario nao encontrado.");
        }
    }



}
