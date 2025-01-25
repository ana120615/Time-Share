package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Usuario;

import java.util.ArrayList;

public interface IRepositorioCotas {

    void cadastrarCota(Cota cota);

    void cadastrarCotas(ArrayList<Cota> cotas);

    void excluirCota(Cota cota);

    boolean existeCota(Cota cota);

    Cota buscarCotaPorId(int id);

    ArrayList<Cota> buscarCotasPorProprietario(Usuario proprietario);

    ArrayList<Cota> buscarCotasPorBem(Bem bem);

    ArrayList<Cota> listarCotas();
    
    ArrayList<Cota>listarCotasDisponiveisParaVenda();

}
