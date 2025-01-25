package br.ufrpe.time_share.excecoes;

public class TransferenciaInvalidaException extends RuntimeException {
    public TransferenciaInvalidaException(String message) {
        super(message);
    }
}
