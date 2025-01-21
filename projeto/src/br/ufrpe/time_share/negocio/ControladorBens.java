package br.ufrpe.time_share.negocio;

import br.ufrpe.time_share.dados.IRepositorioBens;
import br.ufrpe.time_share.excecoes.BemJaExisteException;
import br.ufrpe.time_share.excecoes.BemNaoExisteException;
import br.ufrpe.time_share.excecoes.UsuarioNaoPermitidoException;
import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;

import java.util.ArrayList;


public class ControladorBens {
    private IRepositorioBens repositorio;

    public ControladorBens (IRepositorioBens instanciaIneterface) {
        this.repositorio = instanciaIneterface;
    }
    public void cadastrar(Usuario usuario, Bem bem) throws BemJaExisteException, UsuarioNaoPermitidoException {
        if (usuario == null || bem == null) {
            throw new NullPointerException();
        } else {
            if (usuario.getTipo().equals(TipoUsuario.ADMINISTRADOR)) {
                boolean jaExiste = repositorio.existe(bem);

                if (jaExiste) {
                    throw new BemJaExisteException("Bem já existe");
                }

                repositorio.cadastrarBem(bem);
            } else {
                throw new UsuarioNaoPermitidoException("Usuário não permitido");
            }

        }

    }

    public Bem remover (int id) throws BemNaoExisteException {
        Bem removido = null;
        int i;
        boolean jaExiste = false;
        for (i = 0; i < repositorio.listarBens().size() && !jaExiste; i++) {
            if (repositorio.listarBens().get(i).getId() == id) {
                jaExiste = true;
            }
        }

        if (jaExiste) {
            removido = repositorio.listarBens().get(i-1);
            repositorio.removerBem(removido);
        } else {
            throw new BemNaoExisteException("Bem não existe");
        }

        return removido;
    }

    public ArrayList<Bem> listarBens () {
        return repositorio.listarBens();
    }

    public ArrayList<Bem> listarBensDisponiveis () {
        return repositorio.listarBensDisponiveis();
    }


}



