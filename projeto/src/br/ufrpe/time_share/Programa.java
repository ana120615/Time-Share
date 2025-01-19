package br.ufrpe.time_share;

import br.ufrpe.time_share.negocio.beans.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class Programa {
    public static void main(String[] args) {

        // USUÁRIO ADMINISTRADOR

        Usuario administrador = new UsuarioAdm(126654, "Senha123");

        Bem b1 = new Bem(1111, "Casa Da Praia", "Noronha",
                "Recife-pe" , 7);

        Bem b2 = new Bem(2222, "Apartamento cidade", "Centro",
                "Recife-pe" , 3);

        // ADMINISTRADOR CADASTRANDO BEM

        ((UsuarioAdm) administrador).cadastrarBem(b1);
        ((UsuarioAdm) administrador).cadastrarBem(b2);

        System.out.println(((UsuarioAdm) administrador).consultarBens());

        // OFERTANDO BEM
        b1.setOfertado(true);

        // CRIANDO E ADICIONANDO COTAS AO BEM

        b1.adicionarCota(336541, LocalDate.of(2025, 2, 10),
                LocalDate.of(2025, 2, 27), 20000);

        b1.adicionarCota(53641, LocalDate.of(2025, 3, 12),
                LocalDate.of(2025, 3, 30), 15000);

        b1.adicionarCota(9451213, LocalDate.of(2025, 4, 7),
                LocalDate.of(2025, 5, 4), 30000);

        System.out.println("\nCOTAS OFERTADAS BEM1\n" + b1.getCotas());


        // USUARIO COMUM

        Usuario usuarioComum = new UsuarioComum (11233215, "SenhaComum");
        usuarioComum.setNome("Samara");
        usuarioComum.setDataNascimento(LocalDate.now());


        // RELACIONANDO USUARIOCOMUM, COTA E VENDA

        Venda v1 = new Venda(12354, (UsuarioComum) usuarioComum);

        ArrayList<Cota> cotasBem1 = b1.getCotas();

        v1.adicionarCotaCarrinho(cotasBem1.get(0));
        v1.adicionarCotaCarrinho(cotasBem1.get(1));
        v1.adicionarCotaCarrinho(cotasBem1.get(2));

        System.out.println("\nCarrinho de Compras: \n" +v1.getCarrinhoDeComprasCotas());

        v1.removerCotaCarrinho(cotasBem1.get(1));

        System.out.println("\nCarrinho de Compras após Remoção: \n" +v1.getCarrinhoDeComprasCotas());

        System.out.println(" ");
        System.out.println(v1.gerarNotaFiscal());

        // PROMOÇÃO
        Promocao p1 = new Promocao();

        double x = p1.calcularTaxaPromocao(LocalDate.now(), usuarioComum);
        System.out.println("PROMOÇÃO: " + x*100 + "% de desconto");

        double valorDescontado = 0;
        if(x != 0) {
            valorDescontado = v1.calcularValorTotal() * x;
        }
        System.out.println("VALOR TOTAL R$" + v1.calcularValorTotal());
        System.out.println("VALOR DESCONTADO R$" + valorDescontado);
        System.out.println("VALOR FINAL R$" + (v1.calcularValorTotal() - valorDescontado));
        System.out.println(" ");
        v1.finalizarCompra();

        // VERIFICANDO A DISPONIBILIADE DE COMPRA DAS COTAS NO BEM1
        // NOTE QUE A DISPONIBILIDADE DA COMPRA DA COTA
        System.out.println("\nCOTAS DO BEM APÓS A COMPRA SER FINALIZADA: \n" + b1.getCotas());

        // VERIFICANDO SE O USUÀRIO RECEBEU AS COTAS COMPRADAS
        System.out.println("\nCOTAS USUÀRIO: \n" + ((UsuarioComum) usuarioComum).getCotasAdquiridas());


        // ESTADIA E RESERVA
        Reserva r1 = new Reserva(6314,
                LocalDateTime.of(2025, 2, 15, 12, 00, 00),
                LocalDateTime.of(2025, 2, 28, 12, 00, 00),
                (UsuarioComum) usuarioComum, b1);

        Estadia estadia = new Estadia(63141, r1);

        // CHECK-IN
        estadia.setDataInicio(LocalDateTime.now());

        // CHECK-OUT NO FIM DA RESERVA
        // LEMBRANDO QUE NÂO É OBRIGATÓRIO SER NO FIM DA RESERVA PARA TERMINAR SUA ESTADIA
        // VALE O MESMO PARA O CHECK-IN
        estadia.setDataFim(r1.getDataFim());

        System.out.println("\n\nIMPRIMINDO AS INFORMAÇÕES DA ESTADIA REALIZADA:" + estadia);




    }
}
