package br.ufrpe.time_share.dados;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;

import java.util.ArrayList;

public class RepositorioUsuarios implements IRepositorioUsuario {
    private ArrayList<Usuario> usuarios;

    {
        this.usuarios = new ArrayList<>();
    }


    @Override
    public void cadastrar(Usuario usuario) {
        usuarios.add(usuario);
    }

    @Override
    public void alterar(Usuario usuarioAtualizado) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getCpf() == usuarioAtualizado.getCpf()) {
                usuarios.set(i, usuarioAtualizado);
            }
        }
    }

    @Override
    public void remover(Usuario usuario) {
        usuarios.remove(usuario);
    }

    public Usuario buscarUsuarioPorCpf(int cpf){
        Usuario resultado = null;

        boolean usuarioComEsseCpfExiste = false;
        int i;
        for (i = 0; i < usuarios.size() && !usuarioComEsseCpfExiste; i++) {
            if (usuarios.get(i).getCpf() == cpf) {
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

