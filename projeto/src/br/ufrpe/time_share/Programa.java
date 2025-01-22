package br.ufrpe.time_share;

import br.ufrpe.time_share.dados.IRepositorioUsuario;
import br.ufrpe.time_share.dados.RepositorioBens;
import br.ufrpe.time_share.dados.RepositorioUsuarios;
import br.ufrpe.time_share.excecoes.*;
import br.ufrpe.time_share.negocio.ControladorBens;
import br.ufrpe.time_share.negocio.ControladorUsuarioGeral;
import br.ufrpe.time_share.negocio.ControladorVendas;
import br.ufrpe.time_share.negocio.beans.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;


public class Programa {
    public static void main(String[] args) throws UsuarioNaoPermitidoException, UsuarioJaExisteException, DadosInsuficientesException {
        IRepositorioUsuario repositorioUsuario = RepositorioUsuarios.getInstance();
        ControladorUsuarioGeral controlador = new ControladorUsuarioGeral(repositorioUsuario);

        //controlador.cadastrar("11111111111", "Samara", "samara@gmail.com", "ss", LocalDate.of(2005,02,01), TipoUsuario.fromValor(1));
        controlador.cadastrar("11111191111", "Samara", "samar@gmail.com", "ss", LocalDate.of(2005, 02, 01), TipoUsuario.fromValor(2));
//
//        Usuario usuario = new Usuario("132644541", "Senha1", TipoUsuario.COMUM);
//        Usuario usuario2 = new Usuario("65156155", "Senha9542", TipoUsuario.COMUM);
//        Usuario usuario3 = new Usuario("45165206", "S@NHASUF", TipoUsuario.COMUM);
//        Usuario usuario4 = new Usuario("511006515", "#afjnja", TipoUsuario.ADMINISTRADOR);
        Usuario usuario5 = new Usuario("471652206", "fafeof", TipoUsuario.ADMINISTRADOR);


        ControladorVendas controladorVendas = new ControladorVendas();
        ControladorBens controladorBens = new ControladorBens(RepositorioBens.getInstancia());

        Venda v1 = null;
        try {
            v1 = controladorVendas.iniciarVenda("11111191111");
        } catch (UsuarioNaoExisteException e) {
            System.out.println(e.getMessage());
        }

        try {
            controladorBens.cadastrar(1364, "Apartamento", "Linda Casa",
                    "Centro da cidade", 5, usuario5, LocalDateTime.now()
                    , 20, 2000);
        } catch (BemNaoExisteException | UsuarioNaoPermitidoException | NullPointerException | BemJaExisteException e) {
            System.out.println(e.getMessage());
        }

        try {
            controladorBens.ofertarBem(1364);
        } catch (BemNaoExisteException e) {
            System.out.println(e.getMessage());
        }


        // controladorVendas.adicionarCotaCarrinho();

        Scanner teclado = new Scanner(System.in);

        boolean vendaFinalizada = false;
        while (!vendaFinalizada) {
            System.out.println(controladorBens.listarBens());
            System.out.println("1 - adiconar; 2 - remover; 3 finalizar");
            int escolha = teclado.nextInt();

            if (escolha == 1) {
                System.out.print("ID: ");
                int id = teclado.nextInt();
                try {
                    controladorVendas.adicionarCotaCarrinho(id, v1);
                } catch (CotaNaoExisteException | CotaNaoOfertadaException e) {
                    System.out.println(e.getMessage());
                }

            } else if (escolha == 2) {
                System.out.print("ID: ");
                int id = teclado.nextInt();
                try {
                    controladorVendas.removeCotaCarrinho(id, v1);
                } catch (CotaNaoExisteException e) {
                    System.out.println(e.getMessage());
                }
            } else if (escolha == 3) {
                System.out.println(controladorVendas.finalizarCompra(v1));
                vendaFinalizada = true;
            }
        }

    }
}