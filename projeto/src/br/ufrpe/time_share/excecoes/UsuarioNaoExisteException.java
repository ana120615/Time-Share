package br.ufrpe.time_share.excecoes;


public class UsuarioNaoExisteException extends Exception {
    public UsuarioNaoExisteException(String message) {
        super(message);
    }
}
