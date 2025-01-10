package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Usuario;
import java.util.ArrayList;

public interface IRepositorioUsuario {

    void cadastrar(Usuario usuario);

    void cadastrar(int cpf, String email);

    void alterar(Usuario usuario);

    void remover(Usuario usuario);

    Usuario buscarUsuarioPorCpf(int cpf);

    Usuario buscarUsuarioPorEmail(String email);

    ArrayList<Usuario> listarUsuarioComum();

    ArrayList<Usuario> listarUsuarioAdm();

}
