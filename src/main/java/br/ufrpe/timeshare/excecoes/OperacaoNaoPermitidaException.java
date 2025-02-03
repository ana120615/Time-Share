package br.ufrpe.timeshare.excecoes;

public class OperacaoNaoPermitidaException extends Exception {
    public OperacaoNaoPermitidaException(String message) {
        super(message);
    }
}
