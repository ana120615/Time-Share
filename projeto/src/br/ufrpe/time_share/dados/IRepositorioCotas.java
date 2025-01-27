package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Usuario;


import java.util.List;

public interface IRepositorioCotas extends IRepositorio<Cota> {

    void cadastrarCotas(List<Cota> cotas);

    List<Cota> buscarCotasPorProprietario(Usuario proprietario);

    List<Cota> buscarCotasPorBem(Bem bem);

    List<Cota> listarCotasDisponiveisParaVenda();

}
