package dados;
import java.util.ArrayList;
import java.util.List;

public class Repositorio<T> {
     private List<T> entidades;
     public Repositorio(){
          this.entidades=new ArrayList<>();
     }
     public void cadastrar(T obj){
          entidades.add(obj);
     }
     public void remover(T obj){
          entidades.remove(obj);
     }
     public RepositorioCotas listar(){
          for (T entidade : entidades) {
               System.out.println(entidade);
           }
   
     }
     protected List<T> getEntidades() {
          return entidades;
      }
}
