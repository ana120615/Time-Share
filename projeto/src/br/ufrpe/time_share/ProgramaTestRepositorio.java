package br.ufrpe.time_share;

import br.ufrpe.time_share.dados.IRepositorioUsuario;
import br.ufrpe.time_share.dados.RepositorioUsuarios;
import br.ufrpe.time_share.negocio.beans.Usuario;
import br.ufrpe.time_share.negocio.beans.UsuarioAdm;
import br.ufrpe.time_share.negocio.beans.UsuarioComum;

public class ProgramaTestRepositorio {
    public static void main(String[] args) {

        IRepositorioUsuario repo = new RepositorioUsuarios();

        Usuario u1 = new UsuarioComum(1111, "5654");
        Usuario u2 = new UsuarioAdm(2222, "5654");
        Usuario u3 = new UsuarioAdm(3333, "5654");

        repo.cadastrar(u1);
        repo.cadastrar(u2);
        repo.cadastrar(u3);

        System.out.println(repo.listarUsuarioAdm());
       

    }
}
