package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Usuario;
import br.ufrpe.time_share.negocio.beans.UsuarioAdm;
import br.ufrpe.time_share.negocio.beans.UsuarioComum;

import java.util.ArrayList;

public interface IRepositorioUsuario {

    void cadastrar(Usuario usuario);

    void alterar(Usuario usuario);

    void remover(Usuario usuario);

    Usuario buscarUsuarioPorCpf(int cpf);

    Usuario buscarUsuarioPorEmail(String email);

    ArrayList<UsuarioComum> listarUsuarioComum();

    ArrayList<UsuarioAdm> listarUsuarioAdm();

}
