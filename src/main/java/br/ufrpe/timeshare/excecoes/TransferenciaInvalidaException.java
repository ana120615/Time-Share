package br.ufrpe.timeshare.excecoes;

public class TransferenciaInvalidaException extends RuntimeException {
    public TransferenciaInvalidaException(String message) {
        super(message);
    }
}
