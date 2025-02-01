package br.ufrpe.timeshare.excecoes;

public class CotaNaoExisteException extends Exception {
    public CotaNaoExisteException(String message) {
        super(message);
    }
}
