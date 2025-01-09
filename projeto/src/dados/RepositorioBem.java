package dados;
import java.util.ArrayList;
import java.util.List;

import negocio.beans.Bem;

public class RepositorioBem extends Repositorio<Bem> {
    //Construtor
     public RepositorioBem(){
          super();
     }
public Bem buscarBemPorId(int id){
    Bem resultado=null;
for (Bem bem : getEntidades()) {
            if (bem.getId()==id) {
                resultado= bem;
            }
        }
        return resultado; // Retorna null se não encontrar
    }
    public void atualizarCapacidade(int bemId, int novaCapacidade){
        for (Bem bem : getEntidades()) {
            if (bem.getId()==bemId) {
                bem.setCapacidade(novaCapacidade);
            }
        }
    }

    public List<Bem> buscarBensDisponiveis(){
        List<Bem> resultado= new ArrayList<>();
    for(Bem bem:getEntidades()){
        if(bem.verificarDisponibilidade()){
            resultado.add(bem);
        }
    }
     return resultado;
    }
}


