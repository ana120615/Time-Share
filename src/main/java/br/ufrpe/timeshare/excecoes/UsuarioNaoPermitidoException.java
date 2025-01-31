package br.ufrpe.timeshare.excecoes;

public class UsuarioNaoPermitidoException extends Exception {
    public UsuarioNaoPermitidoException(String message) {
        super(message);
    }
}
