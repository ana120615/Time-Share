package br.ufrpe.timeshare.dados;

import br.ufrpe.timeshare.negocio.beans.Estadia;

import java.util.List;

public interface IRepositorioEstadia extends IRepositorio<Estadia> {


    List<Estadia> buscarEstadiaPorUsuario(long cpfUsuario);

    List<Estadia> buscarEstadiasPorBem(int idBem);

    Estadia buscarEstadiaPorReserva(int idReserva);

}
