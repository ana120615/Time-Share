package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Historico;
import java.util.ArrayList;

public class IRepositorioHistorico {
    private ArrayList<Historico> historicos;

    public IRepositorioHistorico() {
        this.historicos = new ArrayList<>();

    }
    public void cadastrarHistorico(Historico historico ){
        this.historicos.add(historico);
    }
    public ArrayList<Historico> listarHistoricoPorBem(int idBem) {
        ArrayList<Historico> resultado = new ArrayList<>();
        for (Historico historico : historicos) {
            if (historico.getIdBem() == idBem) {
                resultado.add(historico);
            }
        }
        return resultado;
    }
    public ArrayList<Historico> listarTodosHistoricos() {
        return new ArrayList<>(historicos);
    }

}