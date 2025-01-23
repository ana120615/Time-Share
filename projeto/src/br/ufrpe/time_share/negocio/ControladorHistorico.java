package br.ufrpe.time_share.negocio;
import br.ufrpe.time_share.dados.IRepositorioHistorico;
import br.ufrpe.time_share.negocio.beans.Historico;
import br.ufrpe.time_share.negocio.beans.Usuario;
import java.util.ArrayList;

public class ControladorHistorico {
    private IRepositorioHistorico repositorioHistorico;
    public ControladorHistorico() {
        this.repositorioHistorico = new IRepositorioHistorico(); 
    }
    public void registrarHistorico(Historico historico) {
       // ver a logica amanha
    }
 
}
