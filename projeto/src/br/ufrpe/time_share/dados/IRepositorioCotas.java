package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Usuario;

import java.util.ArrayList;

public interface IRepositorioCotas extends IRepositorio<Cota> {

    void cadastrarCotas(ArrayList<Cota> cotas);

    ArrayList<Cota> buscarCotasPorProprietario(Usuario proprietario);

    ArrayList<Cota> buscarCotasPorBem(Bem bem);

    ArrayList<Cota> listarCotasDisponiveisParaVenda();

}
