package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Estadia;
import java.util.List;

public interface IRepositorioEstadia extends IRepositorio<Estadia> {


    List<Estadia> buscarEstadiaPorUsuario(long cpfUsuario);

    List<Estadia> buscarEstadiasPorBem(int idBem);

    List<Estadia> buscarEstadiaPorReserva(int idReserva);

}
