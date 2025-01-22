package br.ufrpe.time_share;

import br.ufrpe.time_share.dados.RepositorioBens;
import br.ufrpe.time_share.excecoes.BemNaoExisteException;
import br.ufrpe.time_share.excecoes.UsuarioNaoPermitidoException;
import br.ufrpe.time_share.negocio.ControladorBens;
import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;


import java.time.LocalDateTime;

public class ProgramaTestRepositorio {
    public static void main(String[] args) {

        ControladorBens controladorBens = new ControladorBens(new RepositorioBens());

        Usuario usuario = new Usuario("555555554", "SENHA", TipoUsuario.ADMINISTRADOR);
        Usuario usuario2 = new Usuario("225512124", "S#NH@", TipoUsuario.COMUM);

        Bem bem = new Bem(1111, "Casa na Praia", "Férias"
                , "Noronha-PE",
                7, usuario);


        try {
            controladorBens.cadastrar(1364, "Apartamento", "Linda Casa",
                    "Centro da cidade", 5, usuario, LocalDateTime.now()
                    , 20,2000);
        } catch (BemNaoExisteException | UsuarioNaoPermitidoException | NullPointerException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\nLISTA DE BENS");
        System.out.println(controladorBens.listarBens());

        System.out.println("\nDESLOCAMENTO DA COTA");
        System.out.println(controladorBens.calcularDeslocamentoDasCotas(1364, 2026));

        System.out.println("\nLISTA DE BENS");
        System.out.println(controladorBens.listarBens());

        // System.out.println(controladorBens.listarBens());

//        try {
//            controladorBens.remover(1111);
//        } catch (BemNaoExisteException e) {
//            System.out.println(e.getMessage());
//        }
//
//        try {
//            controladorBens.ofertarBem(2222);
//            controladorBens.ofertarBem(1111);
//        } catch (BemNaoExisteException e) {
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println("\nBEM OFERTADOS");
//        System.out.println(controladorBens.listarBensDisponiveis());
//
//        System.out.println("\nLISTA DE BENS");
//        System.out.println(controladorBens.listarBens());
//    }
    }
}
