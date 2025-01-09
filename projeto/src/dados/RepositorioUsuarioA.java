package dados;
import negocio.beans.UsuarioAdm;

public class RepositorioUsuarioA extends Repositorio<UsuarioAdm> {
//Construtor
     public RepositorioUsuarioA(){
          super();
     }

    public UsuarioAdm buscarUsuarioPorCpf(int cpf){
        for (UsuarioAdm usuarioAdm : getEntidades()) {
            if (usuarioAdm.getCpf()==cpf) {
                return usuarioAdm;
            }
        }
        return null; // Retorna null se não encontrar
    }
    }

