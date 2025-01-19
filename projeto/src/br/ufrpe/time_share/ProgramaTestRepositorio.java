package br.ufrpe.time_share;

import br.ufrpe.time_share.dados.*;
import br.ufrpe.time_share.negocio.beans.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class ProgramaTestRepositorio {
    public static void main(String[] args) {

        //USUARIOS
        IRepositorioUsuario repoUsuario = RepositorioUsuarios.getInstance();

        Usuario u1 = new UsuarioComum(1111, "5654");
        Usuario u2 = new UsuarioAdm(2222, "5654");
        Usuario u3 = new UsuarioAdm(3333, "5654");

        repoUsuario.cadastrar(u1);
        repoUsuario.cadastrar(u2);
        repoUsuario.cadastrar(u3);

        System.out.println(repoUsuario.listarUsuarioAdm());
        System.out.println("\n");

        IRepositorioUsuario repoUsuario2 = RepositorioUsuarios.getInstance();
        Usuario u4 = new UsuarioComum(444, "Hotel Fazenda");
        Usuario u5 = new UsuarioAdm(555, "Hotel Fazenda");

        repoUsuario2.cadastrar(u4);
        repoUsuario2.cadastrar(u5);
        repoUsuario2.remover(u2);

        System.out.println(repoUsuario2.listarUsuarioAdm());
        System.out.println("\n_________________________________________\n\n");

        //BENS
        IRepositorioBens repoBem = RepositorioBens.getInstancia();

        Bem b1 = new Bem(123, "Hotel Fazenda", "Apartamento com 4 quartos", "Bonito", 8);
        Bem b2 = new Bem(122, "Casa na Praia", "Casa com 3 quartos e piscina privada", "Itamaraca", 6);
        Bem b3 = new Bem(123, "Pousada", "Apartamento com 1 quarto", "Maragogi", 3);

        repoBem.cadastrarBem(b1);
        repoBem.cadastrarBem(b2);
        repoBem.cadastrarBem(b3);

        System.out.println(repoBem.listarBens());
        System.out.println("\n");

        IRepositorioBens repoBens = RepositorioBens.getInstancia();
        repoBens.removerBem(b2);

        System.out.println(repoBens.listarBens());
        System.out.println("\n_________________________________________\n\n");

        //RESERVAS
        IRepositorioReservas repoReservas = RepositorioReservas.getInstance();

        Reserva r1 = new Reserva(122, LocalDateTime.now(), LocalDateTime.of(2025, 2, 19, 14, 0, 0), (UsuarioComum) u4, b1);
        Reserva r2 = new Reserva(133, LocalDateTime.now(), LocalDateTime.of(2025, 1, 29, 14, 0, 0), (UsuarioComum) u1, b3);

        repoReservas.cadastrarReserva(r1);
        repoReservas.cadastrarReserva(r2);

        System.out.println(repoReservas.listarReservas());
        System.out.println("\n");

        IRepositorioReservas repoReservas2 = RepositorioReservas.getInstance();

        repoReservas2.removerReserva(133);
        System.out.println(repoReservas2.listarReservas());
        System.out.println("\n__________________________________________\n\n");


        //COTAS
        IRepositorioCotas repoCotas = RepositorioCotas.getInstance();

        Cota c1 = new Cota(9999, LocalDate.of(2025, 2, 10),
                LocalDate.of(2025, 2, 27), 20000, b1);
        b1.adicionarCota(c1);

        Cota c2 = new Cota(8888, LocalDate.of(2025, 1, 29),
                LocalDate.of(2025, 2, 27), 1500, b1);
        b1.adicionarCota(c2);

        Cota c3 = new Cota(7777, LocalDate.of(2025, 2, 20),
                LocalDate.of(2025, 3, 27), 4000, b3);
        b3.adicionarCota(c3);

        repoCotas.cadastrarCota(c1);
        repoCotas.cadastrarCota(c2);
        repoCotas.cadastrarCota(c3);

        System.out.println(repoCotas.listarCotas());
    }
}
