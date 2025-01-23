package br.ufrpe.time_share.negocio;
import br.ufrpe.time_share.dados.RepositorioHistorico;
import br.ufrpe.time_share.negocio.beans.Historico;

public class ControladorHistorico {
    private RepositorioHistorico repositorioHistorico;
    public ControladorHistorico() {
        this.repositorioHistorico = new RepositorioHistorico();
    }
    public void registrarHistorico(Historico historico) {
       // ver a logica amanha
    }
 
}
