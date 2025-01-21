package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Usuario;

import java.util.ArrayList;

public interface IRepositorioUsuario {

    void cadastrar(Usuario usuario);

    void alterar(Usuario usuarioAtualizado);

    void remover(Usuario usuario);

    Usuario buscarUsuarioPorCpf(int cpf);

    Usuario buscarUsuarioPorEmail(String email);

    boolean existe(Usuario usuario);

    ArrayList<Usuario> listarUsuarioComum();

    ArrayList<Usuario> listarUsuarioAdm();

}
