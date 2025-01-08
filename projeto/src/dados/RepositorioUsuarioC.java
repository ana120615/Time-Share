package dados;
import negocio.beans.UsuarioComum;

public class RepositorioUsuarioC extends Repositorio<UsuarioComum> {
//Construtor
     public RepositorioUsuarioC(){
          super();
     }
    @Override
    void cadastrar(UsuarioComum usuarioC) {
        super.cadastrar(usuarioC);
    }

    @Override
    void listar() {
        super.listar();
    }

    @Override
    void remover(UsuarioComum usuarioC) {
        super.remover(usuarioC);
    }
}
