package br.ufrpe.time_share;

import br.ufrpe.time_share.dados.IRepositorioUsuario;
import br.ufrpe.time_share.dados.RepositorioUsuarios;
import br.ufrpe.time_share.negocio.beans.*;


public class Programa {
    public static void main(String[] args) {

        Usuario usuario = new Usuario(132644541, "Senha123", TipoUsuario.ADMINISTRADOR);
        Usuario usuario2 = new Usuario(65156155, "Senha9542", TipoUsuario.COMUM);
        Usuario usuario3 = new Usuario(45165206, "S@NHASUF", TipoUsuario.COMUM);
        Usuario usuario4 = new Usuario(511006515, "#afjnja", TipoUsuario.ADMINISTRADOR);
        Usuario usuario5 = new Usuario(471652206, "fafeof", TipoUsuario.ADMINISTRADOR);



        IRepositorioUsuario repo = new RepositorioUsuarios();
        repo.cadastrar(usuario);
        repo.cadastrar(usuario2);
        repo.cadastrar(usuario3);
        repo.cadastrar(usuario4);
        repo.cadastrar(usuario5);

        System.out.println("LISTANDO USUÁRIOS COMUMS");
        System.out.println(repo.listarUsuarioComum());

        System.out.println("\n\nLISTANDO USUÁROS ADMINISTRADORES");
        System.out.println(repo.listarUsuarioAdm());

    }
}
