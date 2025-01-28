//package br.ufrpe.time_share;
//
//import br.ufrpe.time_share.dados.RepositorioBens;
//import br.ufrpe.time_share.dados.RepositorioCotas;
//import br.ufrpe.time_share.excecoes.BemJaExisteException;
//import br.ufrpe.time_share.excecoes.BemNaoExisteException;
//import br.ufrpe.time_share.excecoes.UsuarioNaoExisteException;
//import br.ufrpe.time_share.excecoes.UsuarioNaoPermitidoException;
//import br.ufrpe.time_share.negocio.ControladorBens;
//import br.ufrpe.time_share.negocio.beans.Bem;
//import br.ufrpe.time_share.negocio.beans.TipoUsuario;
//import br.ufrpe.time_share.negocio.beans.Usuario;
//import java.time.LocalDateTime;
//
//public class ProgramaTestRepositorio {
//    public static void main(String[] args) {
//
//        ControladorBens controladorBens = new ControladorBens(RepositorioBens.getInstancia(), RepositorioCotas.getInstancia());
//
//        Usuario usuario = new Usuario("555555554", "SENHA", TipoUsuario.ADMINISTRADOR);
//        Usuario usuario2 = new Usuario("225512124", "S#NH@", TipoUsuario.COMUM);
//
//        Bem bem = new Bem(1111, "Casa na Praia", "Férias"
//                , "Noronha-PE",
//                7, usuario);
//
//
//        try {
//            controladorBens.cadastrar(1364, "Apartamento", "Linda Casa",
//                    "Centro da cidade", 5, "555555554", LocalDateTime.now()
//                    , 20,2000);
//        } catch (BemNaoExisteException | UsuarioNaoPermitidoException | NullPointerException | BemJaExisteException |
//                 UsuarioNaoExisteException e) {
//            System.out.println(e.getMessage());
//        }
//
//
//
//    }
//}
