package br.ufrpe.time_share.dados;
import br.ufrpe.time_share.negocio.beans.Usuario;
import br.ufrpe.time_share.negocio.beans.UsuarioAdm;

import java.util.ArrayList;

public class RepositorioUsuarios implements IRepositorioUsuario {


    @Override
    public void cadastrar(Usuario usuario) {

    }

    @Override
    public void cadastrar(int cpf, String email) {

    }

    @Override
    public void alterar(Usuario usuario) {

    }

    @Override
    public void remover(Usuario usuario) {

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

    @Override
    public Usuario buscarUsuarioPorEmail(String email) {
        return null;
    }

    @Override
    public ArrayList<Usuario> listarUsuarioComum() {
        return null;
    }

    @Override
    public ArrayList<Usuario> listarUsuarioAdm() {
        return null;
    }
}

