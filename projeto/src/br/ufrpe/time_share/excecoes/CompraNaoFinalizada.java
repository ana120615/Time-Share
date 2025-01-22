package br.ufrpe.time_share.excecoes;

public class CompraNaoFinalizada extends RuntimeException {
    public CompraNaoFinalizada(String message) {
        super(message);
    }
}
