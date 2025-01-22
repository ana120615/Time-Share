package br.ufrpe.time_share.negocio;

import br.ufrpe.time_share.dados.IRepositorioUsuario;
import br.ufrpe.time_share.excecoes.DadosInsuficientesException;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;
import br.ufrpe.time_share.excecoes.UsuarioNaoExisteException;
import br.ufrpe.time_share.excecoes.UsuarioJaExisteException;
import br.ufrpe.time_share.excecoes.UsuarioNaoPermitidoException;

import java.time.LocalDate;


public class ControladorAdm {
    private IRepositorioUsuario repositorio;

    public ControladorAdm(IRepositorioUsuario repositorioUsuario) {
        this.repositorio = repositorioUsuario;
    }

    public void cadastrar(String cpf, String nome, String email, String senha, LocalDate dataNascimento) throws UsuarioJaExisteException, DadosInsuficientesException, UsuarioNaoPermitidoException {
        if (cpf == null || nome == null || email == null || senha == null || dataNascimento == null) {
            throw new DadosInsuficientesException("Informacao insuficiente.");
        }
        Usuario usuarioAdm = new Usuario(cpf, nome, email, senha, dataNascimento, TipoUsuario.ADMINISTRADOR);
        validarCadastro(usuarioAdm);
        this.repositorio.cadastrar(usuarioAdm);
    }

    public void cadastrarAministrador(Usuario adm) throws UsuarioJaExisteException, UsuarioNaoPermitidoException {
        validarCadastro(adm);
        repositorio.cadastrar(adm);

    }

    public void validarCadastro(Usuario adm) throws UsuarioJaExisteException, UsuarioNaoPermitidoException {
        if (adm == null) {
            throw new UsuarioNaoPermitidoException("O adminitsrador não pode ser nulo.");
        }
        else if (adm.getCpf() != null) {
            char[] verificador = adm.getCpf().toCharArray();
            if (verificador.length != 11) {
                throw new UsuarioNaoPermitidoException("CPF invalido.");
            }

            for (int i = 0; i < verificador.length; i++) {
                if (!Character.isDigit(verificador[i])) {
                    throw new UsuarioNaoPermitidoException("CPF invalido.");
                }
            }
        }
        else if (this.repositorio.existe(adm)) {
            throw new UsuarioJaExisteException("Usuário já cadastrado.", adm.getCpf(), adm.getEmail());
        }
        else if (adm.getTipo() != TipoUsuario.ADMINISTRADOR) {
            throw new UsuarioNaoPermitidoException("Apenas administradores podem ser cadastrados nessa categoria!");
        }
    }

    public void removerAdm(Usuario adm) throws UsuarioNaoExisteException, UsuarioNaoPermitidoException {
        if (adm == null) {
            throw new UsuarioNaoExisteException("Usuário com o CPF digitado não foi encontrado! ");
        }
        if (adm.getTipo() != TipoUsuario.ADMINISTRADOR) {
            throw new UsuarioNaoPermitidoException("O usuário não tem permissão para ser removido de administradores! ");
        }
        repositorio.remover(adm);

    }


}

