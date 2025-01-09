package dados;
import negocio.beans.Bem;

public class RepositorioBem extends Repositorio<Bem> {
    //Construtor
     public RepositorioBem(){
          super();
     }
public Bem buscarBemPorId(int id){
for (Bem bem : getEntidades()) {
            if (bem.getId()==id) {
                return bem;
            }
        }
        return null; // Retorna null se não encontrar
    }
    public void atualizarCapacidade(int bemId, int novaCapacidade){
        for (Bem bem : getEntidades()) {
            if (bem.getId()==bemId) {
                bem.setCapacidade(novaCapacidade);
            }
        }
    }
}


