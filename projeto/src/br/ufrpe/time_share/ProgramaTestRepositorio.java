package br.ufrpe.time_share;


import br.ufrpe.time_share.dados.IRepositorioBens;
import br.ufrpe.time_share.dados.RepositorioBens;
import br.ufrpe.time_share.excecoes.BemJaExisteException;
import br.ufrpe.time_share.excecoes.BemNaoExisteException;
import br.ufrpe.time_share.excecoes.UsuarioNaoPermitidoException;
import br.ufrpe.time_share.negocio.ControladorBens;
import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;

public class ProgramaTestRepositorio {
    public static void main(String[] args) {



        ControladorBens controladorBens = new ControladorBens (new RepositorioBens());

        Usuario usuario = new Usuario(555555554, "SENHA", TipoUsuario.ADMINISTRADOR);
        Usuario usuario2 = new Usuario(225512124, "S#NH@", TipoUsuario.COMUM);

        Bem bem = new Bem(1111, "Casa na Praia", "Férias"
                , "Noronha-PE",
                7, usuario);
        Bem bem2 = new Bem(2222, "Apartamento", "Centro comercial"
                , "Recife-PE",
                3, usuario);
        Bem bem3 = new Bem(3333, "Sitio Passeio", "Férias"
                , "Camaragibe-PE",
                7, usuario);
        Bem bem4 = new Bem(4444, "Ap Dois unidos", "Moradia - lar doce lar"
                , "Recife-PE",
                7, usuario);
        Bem bem5 = new Bem(7777, "Casa jaboatão dos Guararapes", "Melhor lugar do mundo"
                , "Recife-PE",
                7, usuario);


        try {
            controladorBens.cadastrar(usuario, bem);
            controladorBens.cadastrar(usuario, bem2);
        } catch (BemJaExisteException | UsuarioNaoPermitidoException | NullPointerException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(controladorBens.listarBens());

        try {
            controladorBens.remover(1111);
        } catch (BemNaoExisteException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(controladorBens.listarBens());
    }
}
