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

        // Criando usuários
        Usuario admin = new Usuario("123.456.789-00", "Admin", "admin@gmail.com", "Senha123", LocalDate.of(1980, 1, 1), TipoUsuario.ADMINISTRADOR);
        Usuario usuario1 = new Usuario("987.654.321-00", "Usuário 1", "usuario1@gmail.com", "Senha456", LocalDate.of(1995, 5, 15), TipoUsuario.COMUM);

        // Criando um bem e cotas
        Bem bem = new Bem(1, "Casa na praia", "Uma bela casa na praia", "Praia de Boa Viagem", 5, admin);
        Cota cota1 = new Cota(1, LocalDateTime.of(2025, 1, 1, 0, 0), LocalDateTime.of(2025, 1, 7, 23, 59), 1000, bem);
        cota1.setProprietario(usuario1);
        ArrayList<Cota> cotas = new ArrayList<>();
        cotas.add(cota1);
        bem.setCotas(cotas);

        

        // Teste: Criar reserva
        try {
            Reserva reserva = controladorReservas.criarReserva(LocalDateTime.of(2025, 1, 1, 10, 0), usuario1, bem);
            System.out.println("Reserva criada com sucesso: " + reserva);

            // Teste: Calcular taxa extra
            double taxa = controladorReservas.calcularTaxaExtra(reserva);
            System.out.println("Taxa extra calculada: " + taxa);

            // Teste: Consultar disponibilidade
            System.out.println("Disponibilidade antes do cancelamento:");
            System.out.println(controladorReservas.consultarDisponibilidade(bem.getId(), LocalDateTime.of(2025, 1, 1, 0, 0), LocalDateTime.of(2025, 1, 31, 23, 59)));

            // Teste: Cancelar reserva
            controladorReservas.cancelarReserva(reserva.getId());
            System.out.println("Reserva cancelada: " + reserva);

            // Teste: Reembolso
            double reembolso = controladorReservas.reembolsar(reserva);
            System.out.println("Valor do reembolso: " + reembolso);

            // Teste: Consultar disponibilidade após cancelamento
            System.out.println("Disponibilidade após o cancelamento:");
            System.out.println(controladorReservas.consultarDisponibilidade(bem.getId(), LocalDateTime.of(2025, 1, 1, 0, 0), LocalDateTime.of(2025, 1, 31, 23, 59)));

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Teste: Cenário de conflito de reservas
        try {
            Reserva reserva1 = controladorReservas.criarReserva(LocalDateTime.of(2025, 1, 10, 10, 0), usuario1, bem);
            System.out.println("Reserva criada: " + reserva1);

            Reserva reserva2 = controladorReservas.criarReserva(LocalDateTime.of(2025, 1, 10, 10, 0), usuario1, bem); // Deve lançar exceção
            System.out.println("Reserva criada: " + reserva2);
        } catch (Exception e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        // Teste: Check-in e check-out
        try {
            Reserva reserva = controladorReservas.criarReserva(LocalDateTime.of(2025, 1, 15, 10, 0), usuario1, bem);
            Estadia estadia = controladorReservas.checkin(reserva.getId(), LocalDateTime.of(2025, 1, 15, 10, 0));
            System.out.println("Check-in realizado: " + estadia);

            controladorReservas.prolongarEstadia(estadia);
            int duracao = controladorReservas.checkout(estadia);
            System.out.println("Check-out realizado: duração = " + duracao + " dias");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
