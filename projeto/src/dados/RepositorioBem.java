package dados;
import negocio.beans.Bem;

public class RepositorioBem extends Repositorio<Bem> {
    //Construtor
     public RepositorioBem(){
          super();
     }
    @Override
    void cadastrar(Bem bem) {
        super.cadastrar(bem);
    }

    @Override
    void listar() {
        super.listar();
    }

    @Override
    void remover(Bem bem) {
        super.remover(bem);
    }


}
