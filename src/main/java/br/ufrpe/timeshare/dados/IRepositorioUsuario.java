package br.ufrpe.timeshare.dados;

import br.ufrpe.timeshare.negocio.beans.Usuario;

import java.util.List;

public interface IRepositorioUsuario extends IRepositorio<Usuario> {

    Usuario buscarUsuarioPorEmail(String email);

    List<Usuario> listarUsuarioComum();

    List<Usuario> listarUsuarioAdm();

}
