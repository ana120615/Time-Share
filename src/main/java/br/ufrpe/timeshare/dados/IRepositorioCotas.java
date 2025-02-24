package br.ufrpe.timeshare.dados;

import br.ufrpe.timeshare.negocio.beans.Bem;
import br.ufrpe.timeshare.negocio.beans.Cota;
import br.ufrpe.timeshare.negocio.beans.Usuario;

import java.util.List;

public interface IRepositorioCotas extends IRepositorio<Cota> {

    void cadastrarCotas(List<Cota> cotas);

    List<Cota> buscarCotasPorProprietario(Usuario proprietario);

    List<Cota> buscarCotasPorProprietarioPorNomeBem(Usuario proprietario, String nomeBem);

    List<Cota> buscarCotasPorBem(Bem bem);

    List<Cota> listarCotasDisponiveisParaVenda();

}
