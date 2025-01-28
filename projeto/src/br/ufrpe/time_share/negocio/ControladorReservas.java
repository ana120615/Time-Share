package br.ufrpe.time_share.negocio;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    //Talvez retirar a parte do status, caso eu remova a reserva no checkout
    public Estadia checkin(int idReserva, LocalDateTime dataInicio) throws ReservaNaoExisteException, ReservaJaCanceladaException, ForaPeriodoException {
        Reserva reservaRelacionada = repositorio.buscarReservasPorId(idReserva);
        Estadia estadia = null;
        if (reservaRelacionada == null) {
            throw new ReservaNaoExisteException("Reserva inexistente");
        } else {
            if (reservaRelacionada.isCancelada()) {
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

    //remover reserva aqui
    public int checkout(Estadia estadia) throws ReservaNaoExisteException, ReservaJaCanceladaException {
        Reserva reserva = null;
        LocalDateTime agora = LocalDateTime.now();
        if (estadia.getDataFim().equals(agora) || estadia.getDataFim().isBefore(agora)) {
            reserva = repositorio.buscarReserva(estadia.getReserva());
            if (reserva == null) {
                throw new ReservaNaoExisteException("Reserva inexistente");
            } else {
                if (reserva.isCancelada()) {
                    throw new ReservaJaCanceladaException("Reserva ja cancelada");
                } else {
                    repositorio.removerReserva(estadia.getReserva());
                }
            }
        }
        return estadia.calcularDuracao();
    }

    //metodo para criar reserva/ reservar
    //tbm deve verificar se o periodo da cota naquele ano ja expirou
    public Reserva criarReserva(LocalDateTime dataInicio, Usuario usuarioComum, Bem bem)
            throws ReservaJaExisteException, PeriodoJaReservadoException, DadosInsuficientesException, ForaPeriodoException {
        if (bem == null || dataInicio == null || usuarioComum == null) {
            throw new DadosInsuficientesException("Bem, data de inicio ou usuario nao podem ser nulos.");
        }

        int id = 0;
        // Garante ID diferente
        for (int i = 0; i < 1; ) {
             id = 1001 + ThreadLocalRandom.current().nextInt(10000);
            if (repositorio.buscarReservasPorId( id) == null) {
                i++;
            }
        }

        //A data final da reserva eh 1 semana apos a data de inicio
        LocalDateTime dataFim = dataInicio.plusDays(6).withHour(23).withMinute(59).withSecond(59);
        Reserva reserva = new Reserva(id, dataInicio, dataFim, usuarioComum, bem);

        Reserva reservaExistente = repositorio.buscarReserva(reserva);
        if (reservaExistente != null) {
            throw new ReservaJaExisteException("Reserva ja existe.");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new ForaPeriodoException("Data de inicio apos data final");
        }

        //verificando se existe alguma reserva para o mesmo bem
        //no periodo que se quer reservar
        for (Reserva buscar : repositorio.listarReservas()) {
            if (buscar.getBem().equals(reserva.getBem())  && !buscar.isCancelada() &&
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
        } 
            if (reservaCancelada.isCancelada()) {
                throw new ReservaJaCanceladaException("Reserva ja cancelada");
            } 
                reservaCancelada.cancelarReserva();
                repositorio.atualizarReserva(reservaCancelada);
            
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

        if (reserva.isCancelada()) {
            throw new ReservaJaCanceladaException("Reserva ja cancelada");
        }

// Verificar sobreposição de periodos com outras reservas
        for (Reserva buscar : repositorio.listarReservas()) {
            if (buscar.getBem().equals(reserva.getBem()) && buscar.getId() != reserva.getId() && !buscar.isCancelada()) {
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


    
    public List<String> consultarDisponibilidade(int bemId, LocalDateTime inicioPeriodo, LocalDateTime fimPeriodo) throws BemNaoExisteException, ForaPeriodoException {
        List<String> periodosDisponiveis = new ArrayList<>();
    
        // Valida se o período é válido
        if (fimPeriodo.isBefore(inicioPeriodo)|| inicioPeriodo.isBefore(LocalDateTime.now())) {
            throw new ForaPeriodoException("Periodo invalido");
        }
    
        // Busca todas as reservas para o bem
        List<Reserva> reservas = repositorio.buscarReservasPorBem(bemId);
        if (reservas == null) {
            throw new BemNaoExisteException("Bem não existe");
        }
    
        // Itera sobre semanas dentro do período solicitado
        LocalDateTime inicioAtual = inicioPeriodo;
        LocalDateTime fimAtual;
    
        while (!inicioAtual.isAfter(fimPeriodo)) {
            // Define o fim da semana atual
            fimAtual = inicioAtual.plusDays(6).withHour(23).withMinute(59).withSecond(59);
    
            if (fimAtual.isAfter(fimPeriodo)) {
                fimAtual = fimPeriodo; // Ajusta o fim da semana para não ultrapassar o período informado
            }
    
            boolean disponivel = true;
    
            // Verifica se há conflitos com as reservas existentes
            for (Reserva reserva : reservas) {
                if (!reserva.isCancelada()) {
                    LocalDateTime reservaInicio = reserva.getDataInicio();
                    LocalDateTime reservaFim = reserva.getDataFim();
    
                    // Verifica sobreposição de períodos
                    if ((inicioAtual.isBefore(reservaFim) && fimAtual.isAfter(reservaInicio))) {
                        disponivel = false;
                        break; // Encontra conflito, não precisa verificar mais
                    }
                }
            }
    
            // Adiciona o período como disponível, se não houver conflito
            if (disponivel) {
                String periodoFormatado = String.format(
                    "%s - %s",
                    inicioAtual.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    fimAtual.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                );
                periodosDisponiveis.add(periodoFormatado);
            }
    
            // Avança para a próxima semana
            inicioAtual = fimAtual.plusSeconds(1);
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
