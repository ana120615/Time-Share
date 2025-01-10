package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;
import java.util.ArrayList;

public interface IRepositorioCotas {

    void cadastrarCota(Cota cota);

    void alterarCota(Cota cota);

    void excluirCota(Cota cota);

    void atualizarStatusCota(Cota cota, boolean statusAtualizado);

    Cota buscarCota(Cota cota);

    Cota buscarCotaPorId(int id);

    ArrayList<Cota> buscarCotasPorBem(Bem id);

    ArrayList<Cota> listarCota();

    ArrayList<Cota> listarCotaDisponivelParaVenda(Cota cota);

}
