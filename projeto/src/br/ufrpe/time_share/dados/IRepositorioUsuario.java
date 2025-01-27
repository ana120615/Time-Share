package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Usuario;

import java.util.List;

public interface IRepositorioUsuario extends IRepositorio<Usuario> {

    Usuario buscarUsuarioPorEmail(String email);

    List<Usuario> listarUsuarioComum();

    List<Usuario> listarUsuarioAdm();

}
