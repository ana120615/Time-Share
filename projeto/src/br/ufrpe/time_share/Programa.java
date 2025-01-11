package br.ufrpe.time_share;

import br.ufrpe.time_share.dados.IRepositorioBens;
import br.ufrpe.time_share.dados.RepositorioBens;
import br.ufrpe.time_share.negocio.beans.*;

import java.time.LocalDate;
import java.util.ArrayList;


public class Programa {
    public static void main(String[] args) {

        RepositorioBens repositorioBens = new RepositorioBens();

        Usuario adm1 = new UsuarioAdm(1223548, "senhaAdm1");

        Bem bem = new Bem(1111, "Casa na Praia", "Belas vistas",
                "Recife-Pe", 4);
        Bem bem2 = new Bem(2222, "Apartamento", "Centro",
                "Recife-Pe", 2);
        ((UsuarioAdm) adm1).cadastrarBem(bem);
        ((UsuarioAdm) adm1).cadastrarBem(bem2);

        Cota cota1 = new Cota(1234, LocalDate.now(), 950.50);
        Cota cota2 = new Cota(2617, LocalDate.now(), 600);


        bem.adicionarCota(cota1);
        bem.adicionarCota(cota2);

        repositorioBens.cadastrarBem(bem);
        repositorioBens.cadastrarBem(bem2);

        System.out.println(repositorioBens.buscarBemPorId(111));

        System.out.println("\n\n");
        ArrayList<Bem> bensNoRepo = repositorioBens.listarBensDisponiveis();
        for (Bem b : bensNoRepo) {
            System.out.println(b);
        }
        System.out.println("\n\n");


        //System.out.println(((UsuarioAdm) adm1).consultarBens());

        //System.out.println(bem.getCotas());

        UsuarioComum comum = new UsuarioComum(153284, "545415@@Senha");

        Venda v1 = new Venda(1512, comum);

        v1.adicionarCotaCarrinho(cota1);
        v1.adicionarCotaCarrinho(cota2);

        //System.out.println(v1.gerarNotaFiscal());

        // Em tese não finalizou a compra
        System.out.println(comum.getCotasAdquiridas());

        v1.finalizarCompra();
        Cota cota3 = new Cota(5555, LocalDate.now(), 400.50);
        Cota cota4 = new Cota(6666, LocalDate.now(), 600);
        Venda v2 = new Venda(151, comum);
        v2.adicionarCotaCarrinho(cota3);
        v2.adicionarCotaCarrinho(cota4);
        System.out.println(comum.getCotasAdquiridas());

        v2.finalizarCompra();

        // Agora posso acessar as cotas compradas no Usuário Comum
        System.out.println(comum.getCotasAdquiridas());


    }
}
