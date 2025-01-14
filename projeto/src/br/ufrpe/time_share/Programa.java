package br.ufrpe.time_share;

import br.ufrpe.time_share.negocio.beans.*;

import java.time.LocalDate;


public class Programa {
    public static void main(String[] args) {
        // USUÀRIO ADM
        Usuario adm = new UsuarioAdm(111222336, "$$767$$");
        // BEM
        Bem bem = new Bem(1111, "Apartamento beira-mar", "Centro do Recife",
                "Recife-Pe", 6);
        // COTAS
        Cota c1 = new Cota(6241, LocalDate.now(), LocalDate.of(2025, 1, 20),
                100000);

        Cota c2 = new Cota(9542, LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 10), 500000);

        // CADASTRANDO UMA COTA
        ((UsuarioAdm) adm).cadastrarBem(bem);

        // ADICIONANDO UMA COTA NO BEM
        bem.adicionarCota(c1);
        bem.adicionarCota(c2);

        // USUARIO COMUM
        Usuario userComum = new UsuarioComum(8845412, "LALA%$");

        Venda v1 = new Venda(6666, (UsuarioComum) userComum);

        v1.adicionarCotaCarrinho(c1);
        v1.adicionarCotaCarrinho(c2);

        System.out.println(v1);

        v1.finalizarCompra();

        // VERIFICANDO SE USUARIO COMUM POSSUI AS COTAS DEPOIS DE FINALIZAR A COMPRA
        System.out.println(((UsuarioComum) userComum).getCotasAdquiridas());




    }
}
