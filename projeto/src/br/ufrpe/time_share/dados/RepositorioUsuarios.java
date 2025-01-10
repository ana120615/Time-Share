package br.ufrpe.time_share.dados;
import br.ufrpe.time_share.negocio.beans.UsuarioAdm;

public class RepositorioUsuarios extends Repositorio<UsuarioAdm> {
//Construtor
     public RepositorioUsuarios(){
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

