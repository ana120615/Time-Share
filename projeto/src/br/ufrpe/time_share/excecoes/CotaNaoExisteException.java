package br.ufrpe.time_share.excecoes;

public class CotaNaoExisteException extends Exception {
    public CotaNaoExisteException(String message) {
        super(message);
    }
}
