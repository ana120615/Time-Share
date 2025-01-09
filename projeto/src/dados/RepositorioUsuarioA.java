package dados;
import negocio.beans.UsuarioAdm;

public class RepositorioUsuarioA extends Repositorio<UsuarioAdm> {
//Construtor
     public RepositorioUsuarioA(){
          super();
     }

    public UsuarioAdm buscarUsuarioPorCpf(int cpf){
        UsuarioAdm resultado=null;
        for (UsuarioAdm usuarioAdm : getEntidades()) {
            if (usuarioAdm.getCpf()==cpf) {
                resultado= usuarioAdm;
            }
        }
        return resultado; 
    }
    }

