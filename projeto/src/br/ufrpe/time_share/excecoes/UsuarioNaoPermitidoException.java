package br.ufrpe.time_share.excecoes;

public class UsuarioNaoPermitidoException extends Exception {
    public UsuarioNaoPermitidoException(String message) {
        super(message);
    }
}
