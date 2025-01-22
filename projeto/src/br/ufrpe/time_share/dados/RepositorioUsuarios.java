package br.ufrpe.time_share.dados;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;

import java.util.ArrayList;

public class RepositorioUsuarios implements IRepositorioUsuario {

    // INSTANCIA UNICA DO REPOSITORIO
    public static final RepositorioUsuarios INSTANCE = new RepositorioUsuarios();

    private ArrayList<Usuario> usuarios;

    {
        this.usuarios = new ArrayList<>();
    }

    // METODO PARA OBTER A INSTANCIA DO REPOSITORIO
    public static RepositorioUsuarios getInstance() {
        return INSTANCE;
    }

    @Override
    public void cadastrar(Usuario usuario) {
        usuarios.add(usuario);
    }

    @Override
    public void remover(Usuario usuario) {
        usuarios.remove(usuario);
    }

    public Usuario buscarUsuarioPorCpf(String cpf){
        Usuario resultado = null;

        boolean usuarioComEsseCpfExiste = false;
        int i;
        for (i = 0; i < usuarios.size() && !usuarioComEsseCpfExiste; i++) {
            if (usuarios.get(i).getCpf().equals(cpf)) {
                usuarioComEsseCpfExiste = true;
            }
        }

        if (usuarioComEsseCpfExiste) {
            resultado = usuarios.get(i-1);
        }

        return resultado; 
    }

    @Override
    public Usuario buscarUsuarioPorEmail(String email) {
        Usuario resultado = null;

        boolean usuarioComEsseEmailExiste = false;
        int i;
        for (i = 0; i < usuarios.size() && !usuarioComEsseEmailExiste; i++) {
            if (usuarios.get(i).getEmail().equals(email)) {
                usuarioComEsseEmailExiste = true;
            }
        }

        if (usuarioComEsseEmailExiste) {
            resultado = usuarios.get(i-1);
        }

        return resultado;
    }

    @Override
    public boolean existe(Usuario usuario) {
        boolean existe = false;
        for (int i = 0; i < usuarios.size() && !existe; i++) {
            if (usuarios.get(i).equals(usuario)) {
                existe = true;
            }
        }
        return existe;
    }

    @Override
    public ArrayList<Usuario> listarUsuarioComum() {
        ArrayList<Usuario> resultado = new ArrayList<>();
        for (Usuario comum : usuarios) {
            if (comum.getTipo().equals(TipoUsuario.COMUM)) {
                resultado.add(comum);
            }
        }
        return new ArrayList<>(resultado);
    }

    @Override
    public ArrayList<Usuario> listarUsuarioAdm() {
        ArrayList<Usuario> resultado = new ArrayList<>();
        for (Usuario adm : usuarios) {
            if (adm.getTipo().equals(TipoUsuario.ADMINISTRADOR)) {
                resultado.add(adm);
            }
        }
        return new ArrayList<>(resultado);
    }
}

