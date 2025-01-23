package br.ufrpe.time_share.negocio;

import java.util.ArrayList;
import java.util.List;

import br.ufrpe.time_share.dados.IRepositorioBens;
import br.ufrpe.time_share.dados.IRepositorioCotas;
import br.ufrpe.time_share.dados.IRepositorioReservas;
import br.ufrpe.time_share.dados.RepositorioBens;
import br.ufrpe.time_share.dados.RepositorioCotas;
import br.ufrpe.time_share.dados.RepositorioReservas;
import br.ufrpe.time_share.excecoes.BemNaoExisteException;
import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Reserva;

public class ControladorRelatorio {

    private IRepositorioBens repositorioBens;
    private IRepositorioReservas repositorioReservas;
    private IRepositorioCotas repositorioCotas;

    public ControladorRelatorio(IRepositorioBens repositorioCotas, IRepositorioReservas repositorioReservas, RepositorioBens repositorioBens) {
        this.repositorioCotas = RepositorioCotas.getInstancia();
        this.repositorioReservas = RepositorioReservas.getInstance();
        this.repositorioBens = RepositorioBens.getInstancia();
    }

    //Consulta de uso de um bem
    public List<String> consultarHistoricoBem(int bemId) throws BemNaoExisteException {
        List<String> historico = new ArrayList<>();
        Bem bem = repositorioBens.buscarBemPorId(bemId);
        if (bem == null) {
            throw new BemNaoExisteException("Bem inexistente");
        }
        String registro;
        for (Cota cota : repositorioCotas.buscarCotasPorBem(bem)) {
            if (!cota.isStatusDeDisponibilidadeParaReserva()) {
                registro = String.format(" %s, CPF: %s utilizou: De %s a %s", cota.getProprietario().getNome(), cota.getProprietario().getCpf(),
                        cota.getDataInicio(), cota.getDataFim());
                historico.add(registro);
                for (Reserva reserva : repositorioReservas.buscarReservasPorBem(bemId))
                    if ((!reserva.getStatus()) &&
                            !(reserva.getDataInicio().isBefore(cota.getDataInicio()) || reserva.getDataFim().isAfter(cota.getDataFim()) ||
                                    reserva.getDataFim().isBefore(cota.getDataInicio()) || reserva.getDataInicio().isAfter(cota.getDataFim()))) {
                        registro = String.format("%s, CPF: %s utilizou: De %s a %s", reserva.getUsuarioComum().getNome(), reserva.getUsuarioComum().getCpf(), reserva.getDataInicio(), reserva.getDataFim());
                        historico.add(registro);
                    }
            }

        }
        return historico;
    }
}

