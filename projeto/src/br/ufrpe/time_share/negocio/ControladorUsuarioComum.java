package br.ufrpe.time_share.negocio;

import br.ufrpe.time_share.dados.IRepositorioUsuario;
import br.ufrpe.time_share.excecoes.*;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;

import java.time.LocalDate;

public class ControladorUsuarioComum {
    private IRepositorioUsuario repositorio;

    public ControladorUsuarioComum(IRepositorioUsuario instanciaInterface) {
        this.repositorio = instanciaInterface;
    }

    public void cadastrar(String cpf, String nome, String email, String senha, LocalDate dataNascimento) throws UsuarioJaExisteException, UsuarioNaoPermitidoException, DadosInsuficientesException {
        if (cpf == null || nome == null || email == null || senha == null || dataNascimento == null) {
            throw new DadosInsuficientesException("Informacao insuficiente.");
        }
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
        if (usuarioComum.getCpf() != null) {
            char[] verificador = usuarioComum.getCpf().toCharArray();
            if (verificador.length != 11) {
                throw new UsuarioNaoPermitidoException("CPF invalido.");
            }

            for (char c : verificador) {
                if (!Character.isDigit(c)) {
                    throw new UsuarioNaoPermitidoException("CPF invalido.");
                }
            }
        }

        if (this.repositorio.existe(usuarioComum)) {
            throw new UsuarioJaExisteException("Usuário já cadastrado.", usuarioComum.getCpf(), usuarioComum.getEmail());
        }
        if (usuarioComum.getTipo() != TipoUsuario.COMUM) {
            throw new UsuarioNaoPermitidoException("Apenas administradores podem ser cadastrados nessa categoria!");
        }
    }

    private Usuario procurarUsuarioPorCpf(String cpf) {
        return this.repositorio.buscarUsuarioPorCpf(cpf);
    }

    private Usuario procurarUsuarioPorEmail(String email) {
        return this.repositorio.buscarUsuarioPorEmail(email);
    }

    public void remover(String cpf) throws UsuarioNaoExisteException, UsuarioNaoPermitidoException {
        Usuario usuario = procurarUsuarioPorCpf(cpf);
        if (usuario != null) {
            if (usuario.getTipo().equals(TipoUsuario.COMUM)) {
                this.repositorio.remover(usuario);
            } else {
                throw new UsuarioNaoPermitidoException("O usuario em questao pertence ao tipo Administrador.");
            }
        } else {
            throw new UsuarioNaoExisteException("Usuario nao encontrado.");
        }
    }

    public void alterarNomeUsuario(String cpf, String novoNome) throws UsuarioNaoExisteException, UsuarioNaoPermitidoException {
        Usuario usuario = procurarUsuarioPorCpf(cpf);
        if (usuario != null && usuario.getTipo().equals(TipoUsuario.COMUM)) {
            usuario.setNome(novoNome);
        } else if (usuario == null) {
            throw new UsuarioNaoExisteException("Usuario nao encontrado.");
        } else {
            throw new UsuarioNaoPermitidoException("O usuario em questao pertence ao tipo Administrador.");
        }
    }

    public void alterarSenhaUsuario(String email, String senha) throws UsuarioNaoExisteException, UsuarioNaoPermitidoException {
        Usuario usuario = procurarUsuarioPorEmail(email);
        if (usuario != null && usuario.getTipo().equals(TipoUsuario.COMUM)) {
            usuario.setSenha(senha);
        } else if (usuario == null) {
            throw new UsuarioNaoExisteException("Usuario nao encontrado.");
        } else {
            throw new UsuarioNaoPermitidoException("O usuario em questao pertence ao tipo Administrador.");
        }
    }

    public void alterarDataAniversario(String cpf, LocalDate dataAniversario) throws UsuarioNaoExisteException, UsuarioNaoPermitidoException {
        Usuario usuarioAdm = procurarUsuarioPorCpf(cpf);
        if (usuarioAdm != null && usuarioAdm.getTipo().equals(TipoUsuario.COMUM)) {
            usuarioAdm.setDataNascimento(dataAniversario);
        } else if (usuarioAdm == null) {
            throw new UsuarioNaoExisteException("Usuario nao encontrado.");
        } else {
            throw new UsuarioNaoPermitidoException("O usuario em questao pertence ao tipo Administrador.");
        }
    }

}


