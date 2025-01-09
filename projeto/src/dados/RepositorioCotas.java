package dados;
import java.util.ArrayList;
import java.util.List;

import negocio.beans.Bem;
import negocio.beans.Cota;
import negocio.beans.Usuario;

public class RepositorioCotas extends Repositorio<Cota> {
    //Construtor
    public RepositorioCotas(){
        super();
   }
   public void atualizarStatusCota(int cotaId, boolean status){
for (Cota cota : getEntidades()) {
            if (cota.getId()==cotaId) {
                cota.setStatus(status);
            }
        }
   }
   public void buscarCotasPorBem(Bem bem) {
       bem.buscarCotas();
}

public List<Cota> buscarPorUsuario(Usuario usuario) {
        List<Cota> resultado = new ArrayList<>();
        for (Cota cota : getEntidades()) {
            if (cota.getProprietario().equals(usuario)) {
                resultado.add(cota);
            }
        }
        return resultado;
    }
public List<Cota> buscarCotasDisponiveis(){
    List<Cota> resultado= new ArrayList<>();
    for(Cota cota:getEntidades()){
        if(cota.verificarDisponibilidade()){
            resultado.add(cota);
        }
    }
    return resultado;
}
}
