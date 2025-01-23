package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Historico;

import java.util.ArrayList;

public interface IRepositorioHistorico {

    void cadastrarHistorico(Historico historico );

    ArrayList<Historico> listarHistoricoPorBem(int idBem);

    public ArrayList<Historico> listarTodosHistoricos();
}
