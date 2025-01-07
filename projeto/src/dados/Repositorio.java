package dados;
import java.util.ArrayList;
import java.util.List;

public class Repositorio<T> {
     private List<T> entidades;
     public Repositorio(){
          this.entidades=new ArrayList<>();
     }
     void cadastrar(T obj){
          entidades.add(obj);
     }
     void remover(T obj){
          entidades.remove(obj);
     }
     void modificar(T obj){

     }
     void listar(){
          for (T entidade : entidades) {
               System.out.println(entidade);
           }
   
     }
}
