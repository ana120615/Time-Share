package br.ufrpe.time_share.excecoes;

public class ReservaNaoExisteException extends Exception {
    public ReservaNaoExisteException(String message){
        super(message);
    }
}
