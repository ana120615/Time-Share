package br.ufrpe.time_share;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import br.ufrpe.time_share.dados.IRepositorioReservas;
import br.ufrpe.time_share.dados.RepositorioReservas;
import br.ufrpe.time_share.excecoes.BemNaoExisteException;
import br.ufrpe.time_share.excecoes.CotaJaReservadaException;
import br.ufrpe.time_share.excecoes.DadosInsuficientesException;
import br.ufrpe.time_share.excecoes.ForaPeriodoException;
import br.ufrpe.time_share.excecoes.PeriodoJaReservadoException;
import br.ufrpe.time_share.excecoes.ReservaJaCanceladaException;
import br.ufrpe.time_share.excecoes.ReservaJaExisteException;
import br.ufrpe.time_share.excecoes.ReservaNaoExisteException;
import br.ufrpe.time_share.excecoes.ReservaNaoReembolsavelException;
import br.ufrpe.time_share.negocio.ControladorReservas;
import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Estadia;
import br.ufrpe.time_share.negocio.beans.Reserva;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;

public class TesteReservas {
    public static void main(String[] args) {
        IRepositorioReservas repositorioReservas = RepositorioReservas.getInstance();
        ControladorReservas controladorReservas = new ControladorReservas(repositorioReservas);

        ArrayList<Cota> cotas = new ArrayList<>();

        // criando um administrador
        Usuario administrador = new Usuario(
            "789.741.366-99", "Mario", "mario123@gmail.com", "M@rio887",
            LocalDate.of(1981, 1, 17), TipoUsuario.ADMINISTRADOR
        );

        Usuario administradorB = new Usuario("071.847.794-09", "Carolina", "carol344@gmail.com", "c@r0l123", LocalDate.of(1993, 1, 25), TipoUsuario.ADMINISTRADOR);

        // ajustando datas iniciais para teste
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataFim = agora.plusDays(6).withHour(23).withMinute(59).withSecond(59);

        // criando um bem
        Bem bem = new Bem(
            5789, "Casa", "Casa com 5 quartos, 6 banheiros, 1 piscina, sala de estar, sala de jantar, cozinha e área gourmet",
            "Praia de Serrambi", 10, administrador
        );
        Bem bemB = new Bem(1974, "Chacara", "Chacara com piscina, 7 quartos e uma otima area externa", "Primavera", 12, administradorB);

        // criando uma cota
        Cota cota = new Cota(89848, agora, dataFim, 1000, bem);
        Cota cotaB = new Cota(56874, LocalDateTime.of(2025, 01, 26, 20, 24, 55), LocalDateTime.of(2025, 01, 26, 20, 24, 55).plusWeeks(1), 900, bemB);
        Cota cotaC = new Cota(5479, LocalDateTime.of(2025, 01, 20, 00, 00, 00), LocalDateTime.of(2025, 1, 30, 0, 0, 0), 900, bemB);
       
        cotas.add(cotaB);
        cotas.add(cotaC);
        bemB.setCotas(cotas);
        // criando um usuario comum
        Usuario usuario = new Usuario(
            "798.747.222-80", "Maria", "maria123@gmail.com", "M@ria777",
            LocalDate.of(1997, 01, 22), TipoUsuario.COMUM
        );
        Usuario usuarioB = new Usuario("555.474.632-77", "Jose", "joseS876@gmail.com", "jose1409", LocalDate.of(2000, 9, 14), TipoUsuario.COMUM);

        // adicionando proprietario a cota
        cota.setProprietario(usuario);
        cotaB.setProprietario(usuarioB);
        cotaC.setProprietario(usuarioB);

        // Reservar
        try {
            Reserva reserva = controladorReservas.criarReserva(LocalDateTime.now(), usuario, bem);
            double taxa = controladorReservas.calcularTaxaExtra(reserva);
            System.out.println(controladorReservas.gerarComprovanteReserva(reserva, taxa));

            try {
                controladorReservas.cancelarReserva(reserva.getId());
                System.out.println(reserva);

                try {
                    System.out.println("Reembolso: " + controladorReservas.reembolsar(reserva));
                } catch (ReservaNaoReembolsavelException e) {
                    e.printStackTrace();
                }
            } catch (ReservaJaCanceladaException e) {
                e.printStackTrace();
            }

            try {
                System.out.println(controladorReservas.consultarDisponibilidade(bem.getId(), LocalDateTime.now(), LocalDateTime.now().plusMonths(1)));
            } catch (BemNaoExisteException e) {
                e.printStackTrace();
            }

            // testando outra reserva
            Reserva reservaB = controladorReservas.criarReserva(LocalDateTime.of(2025, 1, 7, 23, 59, 59), usuarioB, bemB);
            System.out.println(reservaB);

            try {
                controladorReservas.alterarPeriodoReserva(reservaB.getId(), LocalDateTime.of(2025, 1, 15, 0, 00, 0), LocalDateTime.of(2025, 1, 21, 23, 59, 59));
                System.out.println(reservaB);

                Estadia estadia = controladorReservas.checkin(reservaB.getId(), LocalDateTime.of(2025, 1, 16, 0, 00, 00));
                System.out.println(estadia);

                controladorReservas.gerarComprovanteEstadia(estadia, estadia.calcularDuracao());
                controladorReservas.prolongarEstadia(estadia);
                System.out.println("Reserva relacionada a estadia: " + estadia.getReserva());
                double taxaEstadia = controladorReservas.calcularTaxaExtra(estadia.getReserva());
                System.out.println("Taxa para prolongar: " + taxaEstadia);
                int duracao = controladorReservas.checkout(estadia);
                System.out.println(controladorReservas.gerarComprovanteEstadia(estadia, duracao));
            } catch (ReservaJaCanceladaException | ReservaNaoExisteException | ForaPeriodoException e) {
                e.printStackTrace();
            }
        } catch (ReservaNaoExisteException | CotaJaReservadaException e) {
            e.printStackTrace();
        } catch (ReservaJaExisteException | PeriodoJaReservadoException | DadosInsuficientesException | ForaPeriodoException e) {
            e.printStackTrace();
        }
    }
}
