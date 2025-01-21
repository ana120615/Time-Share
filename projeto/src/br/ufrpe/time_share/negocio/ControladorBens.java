package br.ufrpe.time_share.negocio;

import br.ufrpe.time_share.dados.IRepositorioBens;
import br.ufrpe.time_share.excecoes.BemJaExisteException;
import br.ufrpe.time_share.excecoes.UsuarioNaoPermitidoException;
import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;


public class ControladorBens {

    public void cadastrar(Usuario usuario, Bem bem, IRepositorioBens repositorio) throws BemJaExisteException, UsuarioNaoPermitidoException {
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
}



