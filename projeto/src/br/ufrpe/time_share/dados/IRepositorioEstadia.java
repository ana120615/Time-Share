package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Estadia;
import br.ufrpe.time_share.negocio.beans.Reserva;

import java.util.ArrayList;

public interface IRepositorioEstadia {

    void cadastrarEstadia(Estadia estadia);

    void removerEstadia(Estadia estadia);

    Estadia buscarEstadiaPorId(int id);

    ArrayList<Estadia> buscarEstadiaPorUsuario(String cpfUsuario);

    ArrayList<Estadia> buscarEstadiasPorBem(int idBem);

    ArrayList<Estadia> buscarEstadiaPorReserva(int idReserva);

    ArrayList<Estadia> listarEstadias();
}
