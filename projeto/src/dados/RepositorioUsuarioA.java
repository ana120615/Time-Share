package dados;
import negocio.beans.UsuarioAdm;

public class RepositorioUsuarioA extends Repositorio<UsuarioAdm> {
//Construtor
     public RepositorioUsuarioA(){
          super();
     }
    @Override
    void cadastrar(UsuarioAdm usuarioAdm) {
        super.cadastrar(usuarioAdm);
    }

    @Override
    void listar() {
        super.listar();
    }

    @Override
    void remover(UsuarioAdm usuarioAdm) {
        super.remover(usuarioAdm);
    }
}
