package br.ufrpe.time_share.excecoes;

public class ProprietarioNaoIdentificadoException extends RuntimeException {
    public ProprietarioNaoIdentificadoException(String message) {
        super(message);
    }
}
