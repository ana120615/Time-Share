package br.ufrpe.timeshare.negocio;

import br.ufrpe.timeshare.dados.IRepositorioUsuario;
import br.ufrpe.timeshare.dados.RepositorioUsuarios;
import br.ufrpe.timeshare.excecoes.*;
import br.ufrpe.timeshare.negocio.beans.TipoUsuario;
import br.ufrpe.timeshare.negocio.beans.Usuario;

import java.time.LocalDate;
import java.util.List;

public class ControladorUsuarioGeral {
    private IRepositorioUsuario repositorio;

    public ControladorUsuarioGeral() {
        this.repositorio = RepositorioUsuarios.getInstancia();
    }

    public void cadastrar(long cpf, String nome, String email, String senha, LocalDate dataNascimento, TipoUsuario tipo) throws UsuarioJaExisteException, UsuarioNaoPermitidoException, DadosInsuficientesException {
        if (cpf == 0 || nome == null || email == null || senha == null || dataNascimento == null) {
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
        if (usuario.getId() != 0) {
            String num = Long.toString(usuario.getId());
            char[] verificador = num.toCharArray();
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
            throw new UsuarioJaExisteException("Usuário já cadastrado.", usuario.getId(), usuario.getEmail());
        }

        if (LocalDate.now().getYear() - usuario.getDataNascimento().getYear() < 18 || LocalDate.now().getYear() - usuario.getDataNascimento().getYear() > 100) {
            throw new UsuarioNaoPermitidoException("Idade invalida.");
        }
    }

    public Usuario procurarUsuarioPorCpf(long cpf) throws UsuarioNaoExisteException {
        Usuario usuario = this.repositorio.buscar(cpf);
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

    public void remover(long cpf, TipoUsuario tipo) throws UsuarioNaoExisteException, UsuarioNaoPermitidoException {
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

    public void alterarNomeUsuario(long cpf, String novoNome, TipoUsuario tipo) throws UsuarioNaoExisteException, UsuarioNaoPermitidoException, NullPointerException {
        if(novoNome == null) {
            throw new NullPointerException("Nome nulo");
        }
        Usuario usuario = procurarUsuarioPorCpf(cpf);
        if (usuario != null && usuario.getTipo().equals(tipo)) {
            usuario.setNome(novoNome);
        }  else {
            throw new UsuarioNaoPermitidoException("O usuario em questao pertence a uma categoria diferente.");
        }
    }

    public void alterarEmailUsuario(long cpf, String novoEmail, TipoUsuario tipo) throws UsuarioNaoExisteException, UsuarioNaoPermitidoException, NullPointerException {
        if(novoEmail == null) {
            throw new NullPointerException("Email nulo");
        }
        Usuario usuario = procurarUsuarioPorCpf(cpf);
        if (usuario != null && usuario.getTipo().equals(tipo)) {
            usuario.setEmail(novoEmail);
        }  else {
            throw new UsuarioNaoPermitidoException("O usuario em questao pertence a uma categoria diferente.");
        }
    }

    public void alterarSenhaUsuario(String email, String senha, TipoUsuario tipo) throws UsuarioNaoExisteException, UsuarioNaoPermitidoException, NullPointerException {
        if(senha == null) {
            throw new NullPointerException("Senha nula");
        }
        Usuario usuario = procurarUsuarioPorEmail(email);
        if (usuario != null && usuario.getTipo().equals(tipo)) {
            usuario.setSenha(senha);
        } else {
            throw new UsuarioNaoPermitidoException("O usuario em questao pertence a uma categoria diferente.");
        }
    }

    public void alterarDataAniversario(long cpf, LocalDate dataAniversario, TipoUsuario tipo) throws UsuarioNaoExisteException, UsuarioNaoPermitidoException, NullPointerException {
        if(dataAniversario == null) {
            throw new NullPointerException("Data nula");
        }
        if (LocalDate.now().getYear() - dataAniversario.getYear() < 18 || LocalDate.now().getYear() - dataAniversario.getYear() > 100) {
            throw new UsuarioNaoPermitidoException("Idade invalida.");
        }
        Usuario usuario = procurarUsuarioPorCpf(cpf);
        if (usuario != null && usuario.getTipo().equals(tipo)) {
            usuario.setDataNascimento(dataAniversario);
        } else if (usuario == null) {
            throw new UsuarioNaoExisteException("Usuario nao encontrado.");
        } else {
            throw new UsuarioNaoPermitidoException("O usuario em questao pertence a uma categoria diferente.");
        }
    }

    public List<Usuario> listarAdm() {
        return this.repositorio.listarUsuarioAdm();
    }

    public List<Usuario> listarUsuarioComum() {
        return this.repositorio.listarUsuarioComum();
    }

}
