package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Historico;
import java.util.ArrayList;

public class RepositorioHistorico implements IRepositorioHistorico{

    // INSTANCIA UNICA DO REPOSITORIO
    private static final RepositorioHistorico INSTANCE = new RepositorioHistorico();

    private ArrayList<Historico> historicos;
    private RepositorioBens repositorioBens;

    public RepositorioHistorico() {
        this.historicos = new ArrayList<>();
        this.repositorioBens = RepositorioBens.getInstancia();

    }

    
    public static RepositorioHistorico getInstancia() {
        return INSTANCE;
    }

    public void cadastrarHistorico(Historico historico ){
        this.historicos.add(historico);
    }

    public ArrayList<Historico> listarHistoricoPorBem(String idBem) {
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

    @Override
    public ArrayList<Historico> listarHistoricoPorBem(int idBem) {

        throw new UnsupportedOperationException("Unimplemented method 'listarHistoricoPorBem'");
    }

}