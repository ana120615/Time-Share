package br.ufrpe.time_share.negocio;

import br.ufrpe.time_share.dados.IRepositorioUsuario;
import br.ufrpe.time_share.excecoes.DadosInsuficientesException;
import br.ufrpe.time_share.excecoes.UsuarioJaExisteException;
import br.ufrpe.time_share.excecoes.UsuarioNaoExisteException;
import br.ufrpe.time_share.excecoes.UsuarioNaoPermitidoException;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;

import java.time.LocalDate;

//VAI SER MANTIDA?
public abstract class ControladorUsuarioGeral {
    private IRepositorioUsuario repositorio;

    public ControladorUsuarioGeral(IRepositorioUsuario instanciaInterface) {
        this.repositorio = instanciaInterface;
    }

    // METODOS ABSTRATOS
    public abstract void validarCadastro(Usuario usuario) throws UsuarioJaExisteException, UsuarioNaoPermitidoException;
    public abstract void cadastrar(String cpf, String nome, String email, String senha, LocalDate dataNascimento) throws UsuarioJaExisteException, DadosInsuficientesException, UsuarioNaoPermitidoException;
    public abstract void removerAdm(Usuario adm) throws UsuarioNaoExisteException, UsuarioNaoPermitidoException;

    // OUTROS METODOS
    public void cadastrar(Usuario usuario) throws UsuarioJaExisteException, UsuarioNaoPermitidoException {
        validarCadastro(usuario);
        repositorio.cadastrar(usuario);
    }

    private Usuario procurarUsuarioPorCpf(String cpf) {
        return this.repositorio.buscarUsuarioPorCpf(cpf);
    }

    private Usuario procurarUsuarioPorEmail(String email) {
        return this.repositorio.buscarUsuarioPorEmail(email);
    }

}
