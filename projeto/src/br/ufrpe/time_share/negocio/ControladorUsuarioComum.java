package br.ufrpe.time_share.negocio;

import br.ufrpe.time_share.dados.IRepositorioUsuario;
import br.ufrpe.time_share.excecoes.UsuarioDeTipoIlegivel;
import br.ufrpe.time_share.excecoes.UsuarioJaExisteException;
import br.ufrpe.time_share.excecoes.UsuarioNaoExisteException;
import br.ufrpe.time_share.excecoes.UsuarioNaoPermitidoException;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;

import java.time.LocalDate;

public class ControladorUsuarioComum {
    private IRepositorioUsuario repositorio;

    public ControladorUsuarioComum(IRepositorioUsuario instanciaInterface) {

        this.repositorio = instanciaInterface;
    }

    public void cadastrar(int cpf, String nome, String email, String senha, LocalDate dataNascimento) throws UsuarioJaExisteException, UsuarioNaoPermitidoException {
        Usuario usuarioComum = new Usuario(cpf, nome, email, senha, dataNascimento, TipoUsuario.COMUM);
        validarCadastro(usuarioComum);
        this.repositorio.cadastrar(usuarioComum);
    }

    public void cadastrar(Usuario usuarioComum) throws UsuarioJaExisteException, UsuarioNaoPermitidoException {
        validarCadastro(usuarioComum);
        this.repositorio.cadastrar(usuarioComum);

    }

    public void validarCadastro(Usuario usuarioComum) throws UsuarioJaExisteException, UsuarioNaoPermitidoException {
        if (usuarioComum == null) {
            throw new UsuarioNaoPermitidoException("O adminitsrador não pode ser nulo.");
        }
        else if (usuarioComum.getCpf() <= 0) {
            throw new UsuarioNaoPermitidoException("O CPF não é válido.");
        }
        else if (this.repositorio.existe(usuarioComum)) {
            throw new UsuarioJaExisteException("Usuário já cadastrado.", usuarioComum.getCpf(), usuarioComum.getEmail());
        }
        else if (usuarioComum.getTipo() != TipoUsuario.COMUM) {
            throw new UsuarioNaoPermitidoException("Apenas administradores podem ser cadastrados nessa categoria!");
        }
    }

    public Usuario procurarUsuarioPorCpf(int cpf) {
        return this.repositorio.buscarUsuarioPorCpf(cpf);
    }

    public Usuario procurarUsuarioPorEmail(String email) {
        return this.repositorio.buscarUsuarioPorEmail(email);
    }

    public void remover(int cpf) throws UsuarioNaoExisteException, UsuarioNaoPermitidoException {
        Usuario usuario = procurarUsuarioPorCpf(cpf);
        if (usuario != null) {
            if (usuario.getTipo().equals(TipoUsuario.COMUM)) {
                this.repositorio.remover(usuario);
            } else {
                throw new UsuarioNaoPermitidoException("O usuario em questao e de do tipo Administrador.");
            }
        } else {
            throw new UsuarioNaoExisteException("Usuario nao encontrado.");
        }
    }


}


