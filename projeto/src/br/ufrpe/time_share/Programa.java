package br.ufrpe.time_share;

import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Usuario;
import br.ufrpe.time_share.negocio.beans.UsuarioAdm;

import java.time.LocalDate;


public class Programa {
    public static void main(String[] args) {

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

        System.out.println(((UsuarioAdm) adm1).consultarBens());

        System.out.println(bem.getCotas());

    }
}
