package br.ufrpe.time_share;


import br.ufrpe.time_share.dados.IRepositorioBens;
import br.ufrpe.time_share.dados.RepositorioBens;
import br.ufrpe.time_share.excecoes.BemJaExisteException;
import br.ufrpe.time_share.excecoes.UsuarioNaoPermitidoException;
import br.ufrpe.time_share.negocio.ControladorBens;
import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;

public class ProgramaTestRepositorio {
    public static void main(String[] args) {


        IRepositorioBens repositorioBens = new RepositorioBens();

        ControladorBens controladorBens = new ControladorBens();

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
            controladorBens.cadastrar(usuario, bem, repositorioBens);
            controladorBens.cadastrar(usuario2, bem2, repositorioBens);
        } catch (UsuarioNaoPermitidoException e) {
            System.out.println(e.getMessage());
        } catch (BemJaExisteException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        try {
            controladorBens.cadastrar(usuario, bem3, repositorioBens);
            controladorBens.cadastrar(usuario, bem4, repositorioBens);
            controladorBens.cadastrar(usuario, bem5, repositorioBens);
        } catch (UsuarioNaoPermitidoException e) {
            System.out.println(e.getMessage());
        } catch (BemJaExisteException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\nIMPRIMINDO BEM CADASTRADOS PELO USUARIO");
        for (Bem b : repositorioBens.listarBens()) {
            System.out.println(b);
        }

    }
}
