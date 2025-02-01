package br.ufrpe.timeshare.excecoes;

public class ProprietarioNaoIdentificadoException extends RuntimeException {
    public ProprietarioNaoIdentificadoException(String message) {
        super(message);
    }
}
