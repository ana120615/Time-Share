package br.ufrpe.timeshare.excecoes;


public class UsuarioNaoExisteException extends Exception {
    public UsuarioNaoExisteException(String message) {
        super(message);
    }
}
