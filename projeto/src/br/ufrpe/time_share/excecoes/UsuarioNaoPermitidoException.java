package br.ufrpe.time_share.excecoes;

public class UsuarioNaoPermitidoException extends RuntimeException {
    public UsuarioNaoPermitidoException(String message) {
        super(message);
    }
}
