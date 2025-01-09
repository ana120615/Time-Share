package dados;
import negocio.beans.Bem;
import negocio.beans.Cota;

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

   public void buscarCotasPorBem(Bem bem){
    bem.buscarCotas();
}

public void buscarCotasPorUsuario(int idUsuario){

}
