package br.ufrpe.timeshare.dados;
import br.ufrpe.timeshare.negocio.beans.TipoUsuario;
import br.ufrpe.timeshare.negocio.beans.Usuario;

import java.util.ArrayList;
import java.util.List;

public class RepositorioUsuarios extends RepositorioGenerico<Usuario> implements IRepositorioUsuario {

    // INSTANCIA UNICA DO REPOSITORIO
    private static RepositorioUsuarios instancia;

    public RepositorioUsuarios() {
        super();
    }

    // METODO PARA OBTER A INSTANCIA DO REPOSITORIO
    public static RepositorioUsuarios getInstancia() {
        if (instancia == null) {
            synchronized (RepositorioUsuarios.class) { // Thread-safe
                if (instancia == null) {
                    instancia = new RepositorioUsuarios();
                }
            }
        }
        return instancia;
    }



    @Override
    public Usuario buscarUsuarioPorEmail(String email) {
        Usuario resultado = null;

        boolean usuarioComEsseEmailExiste = false;
        int i;
        for (i = 0; i < lista.size() && !usuarioComEsseEmailExiste; i++) {
            if (lista.get(i).getEmail().equals(email)) {
                usuarioComEsseEmailExiste = true;
            }
        }

        if (usuarioComEsseEmailExiste) {
            resultado = lista.get(i-1);
        }

        return resultado;
    }

    @Override
    public List<Usuario> listarUsuarioComum() {
        List<Usuario> resultado = new ArrayList<>();
        for (Usuario comum : lista) {
            if (comum.getTipo().equals(TipoUsuario.COMUM)) {
                resultado.add(comum);
            }
        }
        return new ArrayList<>(resultado);
    }

    @Override
    public List<Usuario> listarUsuarioAdm() {
        List<Usuario> resultado = new ArrayList<>();
        for (Usuario adm : lista) {
            if (adm.getTipo().equals(TipoUsuario.ADMINISTRADOR)) {
                resultado.add(adm);
            }
        }
        return resultado;
    }
}

