package br.ufrpe.time_share;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import br.ufrpe.time_share.negocio.ControladorReservas;
import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Reserva;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;

public class TesteReservas {
    public static void main(String[] args) {
        IRepositorioReservas repositorioReservas = RepositorioReservas.getInstance();
        ControladorReservas controladorReservas = new ControladorReservas(repositorioReservas);

        //criando um administrador
        Usuario administrador = new Usuario(
            "789.741.366-99", "Mario", "mario123@gmail.com", "M@rio887",
            LocalDate.of(1981, 1, 17), TipoUsuario.ADMINISTRADOR
        );


        //ajustando datas iniciais para teste
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataFim = agora.plusDays(6).withHour(23).withMinute(59).withSecond(59);

        //criando um bem
        Bem bem = new Bem(
            5789, "Casa", "Casa com 5 quartos, 6 banheiros, 1 piscina, sala de estar, sala de jantar, cozinha e área gourmet",
            "Praia de Serrambi", 10, administrador
        );

        //criando uma cota
        Cota cota = new Cota(89848, agora, dataFim, 1000, bem);

        //criando um usuario comum
        Usuario usuario = new Usuario(
            "798.747.222-80", "Maria", "maria123@gmail.com", "M@ria777",
            LocalDate.of(1997, 01, 22), TipoUsuario.COMUM
        );

        //adicionando proprietario a cota
        cota.setProprietario(usuario);

        // Reservar
        try {
            Reserva reserva = controladorReservas.criarReserva(LocalDateTime.now(), usuario, bem);
            double taxa = controladorReservas.calcularTaxaExtra(reserva);
            System.out.println(controladorReservas.gerarComprovanteReserva(reserva, taxa));
            try {
                controladorReservas.cancelarReserva(reserva.getId());
                System.out.println(reserva);
            } catch (ReservaJaCanceladaException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            try {
                System.out.println(controladorReservas.consultarDisponibilidade(bem.getId(), LocalDateTime.now(), LocalDateTime.now().plusMonths(1)));

            } catch (BemNaoExisteException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            //testando outra reserva
            Reserva reservaB = controladorReservas.criarReserva(LocalDateTime.now(), usuario, bem);
        } catch (ReservaNaoExisteException | CotaJaReservadaException e) {
            System.out.println( e.getMessage());
            e.printStackTrace();
        } catch (ReservaJaExisteException | PeriodoJaReservadoException | DadosInsuficientesException
                | ForaPeriodoException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        
    }
}
