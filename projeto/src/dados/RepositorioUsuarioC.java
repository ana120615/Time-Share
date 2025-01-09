package dados;
import negocio.beans.UsuarioComum;

public class RepositorioUsuarioC extends Repositorio<UsuarioComum> {
//Construtor
     public RepositorioUsuarioC(){
          super();
     }
 public UsuarioComum buscarUsuarioPorCpf(int cpf){
    UsuarioComum resultado=null;
        for (UsuarioComum usuarioComum : getEntidades()) {
            if (usuarioComum.getCpf()==cpf) {
                resultado= usuarioComum;
            }
        }
        return resultado; // Retorna null se não encontrar
    }
}
