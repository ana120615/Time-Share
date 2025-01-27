package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Usuario;

import java.util.ArrayList;

public interface IRepositorioUsuario extends IRepositorio<Usuario> {

    void cadastrar(Usuario usuario);

    void remover(Usuario usuario);

    Usuario buscarUsuarioPorCpf(String cpf);

    Usuario buscarUsuarioPorEmail(String email);

    boolean existe(Usuario usuario);

    ArrayList<Usuario> listarUsuarioComum();

    ArrayList<Usuario> listarUsuarioAdm();

}
