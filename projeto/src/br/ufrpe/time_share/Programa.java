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
import java.util.Timer;


public class Programa {
    public static void main(String[] args) throws UsuarioNaoPermitidoException, UsuarioJaExisteException, DadosInsuficientesException {
        IRepositorioUsuario repositorioUsuario = RepositorioUsuarios.getInstance();
        ControladorUsuarioGeral controlador = new ControladorUsuarioGeral(repositorioUsuario);

        controlador.cadastrar("11111191111", "Samara", "samar@gmail.com", "ss", LocalDate.of(2005, 02, 01), TipoUsuario.fromValor(2));
        controlador.cadastrar("12345678901", "Monteiro", "monte@gmail.com", "senh@", LocalDate.of(2005, 02, 01), TipoUsuario.fromValor(1));


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
                    "Centro da cidade", 5, "12345678901", LocalDateTime.of(2024, LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getHour(), LocalDateTime.now().getMinute())
                    , 20, 2000);
        } catch (BemNaoExisteException | UsuarioNaoPermitidoException | NullPointerException | BemJaExisteException |
                 UsuarioNaoExisteException e) {
            System.out.println(e.getMessage());
        }

        try {
            controladorBens.ofertarBem(1364);
        } catch (BemNaoExisteException e) {
            System.out.println(e.getMessage());
        }

        // Teste para deslocamento
        for (int i = 0; i < 1; i++) {
            try {
                long delay = 4000;
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            controladorBens.iniciarDeslocamentoAutomatico();


        }


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