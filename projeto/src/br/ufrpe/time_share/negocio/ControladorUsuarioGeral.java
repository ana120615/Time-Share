package br.ufrpe.time_share.negocio;

import br.ufrpe.time_share.dados.IRepositorioUsuario;
import br.ufrpe.time_share.excecoes.*;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;

public class ControladorUsuarioGeral {
    private IRepositorioUsuario repositorio;

    public ControladorUsuarioGeral(IRepositorioUsuario instanciaInterface) {
        this.repositorio = instanciaInterface;
    }

    public void cadastrar(String cpf, String nome, String email, String senha, LocalDate dataNascimento, TipoUsuario tipo) throws UsuarioJaExisteException, UsuarioNaoPermitidoException, DadosInsuficientesException {
        if (cpf == null || nome == null || email == null || senha == null || dataNascimento == null) {
            throw new DadosInsuficientesException("Informacao insuficiente.");
        }
        Usuario usuario = new Usuario(cpf, nome, email, senha, dataNascimento, tipo);
        validarCadastro(usuario);
        this.repositorio.cadastrar(usuario);
    }

    public void cadastrar(Usuario usuario) throws UsuarioJaExisteException, UsuarioNaoPermitidoException {
        validarCadastro(usuario);
        this.repositorio.cadastrar(usuario);
    }

    public void validarCadastro(Usuario usuario) throws UsuarioJaExisteException, UsuarioNaoPermitidoException {
        if (usuario == null) {
            throw new UsuarioNaoPermitidoException("O usuario não pode ser nulo.");
        }
        if (usuario.getCpf() != null) {
            char[] verificador = usuario.getCpf().toCharArray();
            if (verificador.length != 11) {
                throw new UsuarioNaoPermitidoException("CPF invalido.");
            }

            for (char c : verificador) {
                if (!Character.isDigit(c)) {
                    throw new UsuarioNaoPermitidoException("CPF invalido.");
                }
            }
        }

        if (this.repositorio.existe(usuario)) {
            throw new UsuarioJaExisteException("Usuário já cadastrado.", usuario.getCpf(), usuario.getEmail());
        }

        if (LocalDate.now().getYear() - usuario.getDataNascimento().getYear() < 18) {
            throw new UsuarioNaoPermitidoException("Idade insuficiente.");
        }
    }

    public Usuario procurarUsuarioPorCpf(String cpf) throws UsuarioNaoExisteException {
        Usuario usuario = this.repositorio.buscarUsuarioPorCpf(cpf);
        if (usuario == null) {
            throw new UsuarioNaoExisteException("Usuario nao encontrado.");
        }
        return usuario;
    }

    public Usuario procurarUsuarioPorEmail(String email) throws UsuarioNaoExisteException {
        Usuario usuario = this.repositorio.buscarUsuarioPorEmail(email);
        if (usuario == null) {
            throw new UsuarioNaoExisteException("Usuario nao encontrado.");
        }
        return usuario;
    }

    public void remover(String cpf, TipoUsuario tipo) throws UsuarioNaoExisteException, UsuarioNaoPermitidoException {
        Usuario usuario = procurarUsuarioPorCpf(cpf);
        if (usuario != null) {
            if (usuario.getTipo().equals(tipo)) {
                this.repositorio.remover(usuario);
            } else {
                throw new UsuarioNaoPermitidoException("O usuario em questao pertence a uma categoria diferente.");
            }
        } else {
            throw new UsuarioNaoExisteException("Usuario nao encontrado.");
        }
    }

    public void alterarNomeUsuario(String cpf, String novoNome, TipoUsuario tipo) throws UsuarioNaoExisteException, UsuarioNaoPermitidoException {
        Usuario usuario = procurarUsuarioPorCpf(cpf);
        if (usuario != null && usuario.getTipo().equals(tipo)) {
            usuario.setNome(novoNome);
        } else if (usuario == null) {
            throw new UsuarioNaoExisteException("Usuario nao encontrado.");
        } else {
            throw new UsuarioNaoPermitidoException("O usuario em questao pertence a uma categoria diferente.");
        }
    }

    public void alterarSenhaUsuario(String email, String senha, TipoUsuario tipo) throws UsuarioNaoExisteException, UsuarioNaoPermitidoException {
        Usuario usuario = procurarUsuarioPorEmail(email);
        if (usuario != null && usuario.getTipo().equals(tipo)) {
            usuario.setSenha(senha);
        } else if (usuario == null) {
            throw new UsuarioNaoExisteException("Usuario nao encontrado.");
        } else {
            throw new UsuarioNaoPermitidoException("O usuario em questao pertence a uma categoria diferente.");
        }
    }

    public void alterarDataAniversario(String cpf, LocalDate dataAniversario, TipoUsuario tipo) throws UsuarioNaoExisteException, UsuarioNaoPermitidoException {
        Usuario usuario = procurarUsuarioPorCpf(cpf);
        if (usuario != null && usuario.getTipo().equals(tipo)) {
            usuario.setDataNascimento(dataAniversario);
        } else if (usuario == null) {
            throw new UsuarioNaoExisteException("Usuario nao encontrado.");
        } else {
            throw new UsuarioNaoPermitidoException("O usuario em questao pertence a uma categoria diferente.");
        }
    }

    public ArrayList<Usuario> listarAdm() {
        return this.repositorio.listarUsuarioAdm();
    }

    public ArrayList<Usuario> listarUsuarioComum() {
        return this.repositorio.listarUsuarioComum();
    }

}
