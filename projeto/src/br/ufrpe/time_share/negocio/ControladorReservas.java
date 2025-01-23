package br.ufrpe.time_share.negocio;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import br.ufrpe.time_share.dados.IRepositorioReservas;
import br.ufrpe.time_share.excecoes.BemNaoExisteException;
import br.ufrpe.time_share.excecoes.ForaPeriodoException;
import br.ufrpe.time_share.excecoes.CotaJaReservadaException;
import br.ufrpe.time_share.excecoes.DadosInsuficientesException;
import br.ufrpe.time_share.excecoes.PeriodoJaReservadoException;
import br.ufrpe.time_share.excecoes.ReservaJaCanceladaException;
import br.ufrpe.time_share.excecoes.ReservaJaExisteException;
import br.ufrpe.time_share.excecoes.ReservaNaoExisteException;
import br.ufrpe.time_share.excecoes.ReservaNaoReembolsavelException;
import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Estadia;
import br.ufrpe.time_share.negocio.beans.Promocao;
import br.ufrpe.time_share.negocio.beans.Reserva;
import br.ufrpe.time_share.negocio.beans.Usuario;

public class ControladorReservas {
    private IRepositorioReservas repositorio;

    public ControladorReservas(IRepositorioReservas instanciaInterface) {
        this.repositorio = instanciaInterface;
    }

    public Estadia checkin(int idReserva, LocalDateTime dataInicio) throws ReservaNaoExisteException, ReservaJaCanceladaException, ForaPeriodoException {
        Reserva reservaRelacionada = repositorio.buscarReservasPorId(idReserva);
        Estadia estadia = null;
        if (reservaRelacionada == null) {
            throw new ReservaNaoExisteException("Reserva inexistente");
        } else {
            if (reservaRelacionada.isCancelada()||!reservaRelacionada.getStatus()) {
                throw new ReservaJaCanceladaException("Reserva ja cancelada");
            } else {
                int idAleatorio = 1001 + ThreadLocalRandom.current().nextInt(10000);
                estadia = new Estadia(idAleatorio, reservaRelacionada);
                if (dataInicio.isBefore(reservaRelacionada.getDataInicio()) || dataInicio.isAfter(reservaRelacionada.getDataFim())) {
                    throw new ForaPeriodoException("Data de inicio fora do periodo da reserva");
                } else {
                    estadia.setDataInicio(dataInicio);
                    estadia.setDataFim(reservaRelacionada.getDataFim());
                }
            }
        }
        return estadia;
    }

    //ao passar o tempo reservado, tenho a opcao de prolongar
//a estadia ou fazer check out
    public Estadia prolongarEstadia(Estadia estadia) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime novaDataFim = estadia.getDataFim().plusDays(6).withHour(23).withMinute(59).withSecond(59);

        if (estadia.getDataFim().equals(agora) || estadia.getDataFim().isBefore(agora)) {
            try {
                alterarPeriodoReserva(estadia.getReserva().getId(), agora, novaDataFim);
                estadia.setDataFim(novaDataFim);
            } catch (ReservaNaoExisteException e) {
                System.out.println(e.getMessage());

            } catch (ReservaJaCanceladaException e) {
                System.out.println(e.getMessage());
            } catch (PeriodoJaReservadoException e) {
                System.out.println(e.getMessage());
            }
        }
        return estadia;
    }

    public int checkout(Estadia estadia) throws ReservaNaoExisteException, ReservaJaCanceladaException {
        Reserva reserva = null;
        LocalDateTime agora = LocalDateTime.now();
        if (estadia.getDataFim().equals(agora) || estadia.getDataFim().isBefore(agora)) {
            reserva = repositorio.buscarReserva(estadia.getReserva());
            if (reserva == null) {
                throw new ReservaNaoExisteException("Reserva inexistente");
            } else {
                if (reserva.isCancelada()||!reserva.getStatus()) {
                    throw new ReservaJaCanceladaException("Reserva ja cancelada");
                } else {
                    estadia.getReserva().setStatus(false);;
                }
            }
        }
        return estadia.calcularDuracao();
    }

    //metodo para criar reserva/ reservar
    public Reserva criarReserva(LocalDateTime dataInicio, Usuario usuarioComum, Bem bem)
            throws ReservaJaExisteException, PeriodoJaReservadoException, DadosInsuficientesException, ForaPeriodoException {
        if (bem == null || dataInicio == null || usuarioComum == null) {
            throw new DadosInsuficientesException("Bem, data de início ou usuário não podem ser nulos.");
        }

        int id = 1001 + ThreadLocalRandom.current().nextInt(10000);
        LocalDateTime dataFim = dataInicio.plusDays(6).withHour(23).withMinute(59).withSecond(59);
        Reserva reserva = new Reserva(id, dataInicio, dataFim, usuarioComum, bem);

        Reserva reservaExistente = repositorio.buscarReserva(reserva);
        if (reservaExistente != null) {
            throw new ReservaJaExisteException("Reserva ja existe.");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new ForaPeriodoException("Data de inicio apos data final");
        }

        for (Reserva buscar : repositorio.listarReservas()) {
            if (buscar.getStatus() &&
                    !(reserva.getDataFim().isBefore(buscar.getDataInicio()) || reserva.getDataInicio().isAfter(buscar.getDataFim()))) {
                throw new PeriodoJaReservadoException("Periodo ja reservado.");
            }
        }
        repositorio.cadastrarReserva(reserva);
        return reserva;
    }


    //metodo para cancelar reserva
    public Reserva cancelarReserva(int idReserva) throws ReservaNaoExisteException, ReservaJaCanceladaException {
        Reserva reservaCancelada;
        reservaCancelada = repositorio.buscarReservasPorId(idReserva);
        if (reservaCancelada == null) {
            throw new ReservaNaoExisteException("Reserva inexistente");
        } else {
            if (reservaCancelada.isCancelada()||!reservaCancelada.getStatus()) {
                throw new ReservaJaCanceladaException("Reserva ja cancelada");
            } else {
                reservaCancelada.cancelarReserva();
            }
        }
        return reservaCancelada;
    }

    //metodo para reembolso apos cancelamento
    public double reembolsar(Reserva reserva) throws ReservaNaoReembolsavelException {
        double reembolso = 0.00;
        if (reserva.isCancelada()) {
            try {
                if (calcularTaxaExtra(reserva) != 0) {
                    reembolso += (calcularTaxaExtra(reserva)) * 0.30;
                } else {
                    throw new ReservaNaoReembolsavelException("Reserva nao reembolsavel");
                }
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            } catch (ReservaNaoExisteException e) {
                System.out.println(e.getMessage());

            } catch (CotaJaReservadaException e) {
                System.out.println(e.getMessage());
            }
        }
        return reembolso;
    }

    //metodo para alterar periodo da reserva
    public Reserva alterarPeriodoReserva(int idReserva, LocalDateTime novaDataInicio, LocalDateTime novaDataFim)
            throws ReservaNaoExisteException, ReservaJaCanceladaException, PeriodoJaReservadoException {

        Reserva reserva = repositorio.buscarReservasPorId(idReserva);

        if (reserva == null) {
            throw new ReservaNaoExisteException("Reserva inexistente");
        }

        if (!reserva.getStatus()||reserva.isCancelada()) {
            throw new ReservaJaCanceladaException("Reserva ja cancelada");
        }

// Verificar sobreposição de períodos com outras reservas
        for (Reserva buscar : repositorio.listarReservas()) {
            if (buscar.getId() != reserva.getId() && buscar.getStatus()) {
                if (!(novaDataFim.isBefore(buscar.getDataInicio()) || novaDataInicio.isAfter(buscar.getDataFim()))) {
                    throw new PeriodoJaReservadoException("Periodo ja reservado");
                }
            }
        }

// Atualizar dados da reserva
        reserva.setDataInicio(novaDataInicio);
        reserva.setDataFim(novaDataFim);
        repositorio.atualizarReserva(reserva);

        return reserva;
    }


    public List<String> consultarDisponibilidade(int bemId, LocalDateTime inicioPeriodo, LocalDateTime fimPeriodo) throws BemNaoExisteException {
        List<String> periodosDisponiveis = new ArrayList<>();
        // Buscar todas as reservas para o bem
        ArrayList<Reserva> reservas = repositorio.buscarReservasPorBem(bemId);

        if (reservas == null) {
            throw new BemNaoExisteException("Bem não existe");
        } else {
            LocalDateTime inicioAtual = inicioPeriodo;
            LocalDateTime fimAtual;
            while (!inicioAtual.isAfter(fimPeriodo)) {
                fimAtual = inicioAtual.plusDays(6).withHour(23).withMinute(59).withSecond(59);//1 semana
                if (fimAtual.isAfter(fimPeriodo)) {
                    fimAtual = fimPeriodo;
                }


                boolean disponivel = true;

                //verificando reservas
                for (Reserva reservaBusca : reservas) {
                    if ((inicioAtual.isBefore(reservaBusca.getDataFim()) || inicioAtual.equals(reservaBusca.getDataFim())) &&
                            (fimAtual.isAfter(reservaBusca.getDataInicio()) || fimAtual.equals(reservaBusca.getDataInicio())) || !(reservaBusca.getStatus())) {
                        disponivel = false;
                        break;
                    }

                    //verificando cotas
                    for (Cota cota : reservaBusca.getBem().getCotas()) {
                        if (!(cota.isStatusDeDisponibilidadeParaReserva()) && (inicioAtual.isBefore(cota.getDataFim()) || inicioAtual.equals(cota.getDataFim())) &&
                                (fimAtual.isAfter(cota.getDataInicio()) || fimAtual.equals(cota.getDataInicio()))) {
                            disponivel = false;
                            break;
                        }
                    }
                }

                if (disponivel) {
                    String periodoFormatado = inicioAtual.toLocalDate() + " " + inicioAtual.toLocalTime() + " - " + fimAtual.toLocalDate() + " " + fimAtual.toLocalTime();
                    periodosDisponiveis.add(periodoFormatado);
                }

                inicioAtual = fimAtual.plusSeconds(1);
            }
        }
        return periodosDisponiveis;
    }


    //metodo para calcular taxa extra
    //tambem e usado quando prolongo uma estadia
    //verifica se cota pertence ao usuario e corresponde ao periodo que deseja reservar
    public double calcularTaxaExtra(Reserva reserva) throws ReservaNaoExisteException, CotaJaReservadaException {
        double taxa = 0.00;
        boolean reservaTaxada = true;
        Promocao promocao = new Promocao();

        if (reserva == null) {
            throw new NullPointerException("Reserva nao pode ser nula");
        }

        if (repositorio.buscarReserva(reserva) == null) {
            throw new ReservaNaoExisteException("Reserva inexistente");
        }

        // Verifica se a cota cobre a reserva
        for (Cota cota : reserva.getBem().getCotas()) {
            if (cota.getProprietario().equals(reserva.getUsuarioComum())) {
                boolean datasIguais = cota.getDataInicio().isEqual(reserva.getDataInicio()) &&
                        cota.getDataFim().isEqual(reserva.getDataFim());

                boolean cotaCobreReserva = (!cota.getDataInicio().isAfter(reserva.getDataInicio()) && !cota.getDataFim().isBefore(reserva.getDataFim())) ||
                        (!cota.getDataInicio().isBefore(reserva.getDataInicio()) && cota.getDataFim().isAfter(reserva.getDataFim())) ||
                        (cota.getDataInicio().isBefore(reserva.getDataInicio()) && !cota.getDataFim().isAfter(reserva.getDataFim())) ||
                        (cota.getDataInicio().isEqual(reserva.getDataInicio()) && !cota.getDataFim().isAfter(reserva.getDataFim())) ||
                        (!cota.getDataInicio().isBefore(reserva.getDataInicio()) && cota.getDataFim().isEqual(reserva.getDataFim())) ||
                        (cota.getDataInicio().isEqual(reserva.getDataInicio()) && cota.getDataFim().isEqual(reserva.getDataFim()));

                if (datasIguais || cotaCobreReserva) {
                    if (!cota.isStatusDeDisponibilidadeParaReserva()) {
                        throw new CotaJaReservadaException("A cota já foi utilizada em uma reserva");

                    } else {
                        cota.setStatusDeDisponibilidadeParaReserva(false);
                        reservaTaxada = false;
                        break;
                    }
                }
            }
        }


        if (reservaTaxada) {
            taxa = 150.00;
            double taxaPromocional = promocao.calcularTaxaPromocao(reserva.getDataInicio(), reserva.getUsuarioComum());
            double desconto = taxa * taxaPromocional;
            taxa -= desconto;
        }


        return Math.max(taxa, 0.00);
    }


    //metodo para gerar comprovante da reserva
    public String gerarComprovanteReserva(Reserva reserva, double taxa) {
        String reservaToString = reserva.toString();
        return reservaToString + ", taxa extra: R$" + String.format("%.2f", taxa);
    }

    //metodo para gerar comprovante da estadia
    public String gerarComprovanteEstadia(Estadia estadia, int duracao) {
        String estadiaToString = estadia.toString();
        return estadiaToString + ", duracao da estadia: " + String.format("%d", duracao) + " dias";
    }
}
