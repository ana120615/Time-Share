package br.ufrpe.time_share.negocio;

import br.ufrpe.time_share.dados.IRepositorioUsuario;
import br.ufrpe.time_share.dados.RepositorioBens;
import br.ufrpe.time_share.dados.IRepositorioBens;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;
import br.ufrpe.time_share.excecoes.UsuarioNaoExisteException;
import br.ufrpe.time_share.excecoes.UsuarioJaExisteException;
import br.ufrpe.time_share.excecoes.UsuarioNaoPermitidoException;


public class ControladorAdm {
    private IRepositorioUsuario repositorioUsuarios;
    private RepositorioBens repositorioBens;

    public ControladorAdm(IRepositorioUsuario repositorioUsuario) {
        this.repositorioUsuarios = repositorioUsuario;
        this.repositorioBens = repositorioBens.getInstancia();
    }

    public void cadastrarAministrador(Usuario adm) throws UsuarioJaExisteException {
        validarCadastroAdm(adm);
        repositorioUsuarios.cadastrar(adm);

    }

    public void validarCadastroAdm(Usuario adm) throws UsuarioJaExisteException {
        if (adm == null) {
            throw new UsuarioNaoPermitidoException("O adminitsrador não pode ser nulo");

        }
        if (adm.getCpf() <= 0) {
            throw new UsuarioNaoPermitidoException("O CPF não é válido");
        }
        if (repositorioUsuarios.buscarUsuarioPorCpf(adm.getCpf()) != null) {
            throw new UsuarioJaExisteException("Usuário Já existe", adm.getCpf(), adm.getEmail());
        }
        if (adm.getTipo() != TipoUsuario.ADMINISTRADOR) {
            throw new UsuarioNaoPermitidoException("Apenas administradores podem ser cadastrados nessa categoria!");
        }

    }

    public void removerAdm(Usuario adm) throws UsuarioNaoExisteException {
        if (adm == null) {
            throw new UsuarioNaoExisteException("Usuário com o CPF digitado não foi encontrado! ");
        }
        if (adm.getTipo() != TipoUsuario.ADMINISTRADOR) {
            throw new UsuarioNaoPermitidoException("O usuário não tem permissão para ser removido de administradores! ");
        }
        repositorioUsuarios.remover(adm);

    }


}

