package br.ufrpe.timeshare.excecoes;

public class CompraNaoFinalizada extends RuntimeException {
    public CompraNaoFinalizada(String message) {
        super(message);
    }
}
