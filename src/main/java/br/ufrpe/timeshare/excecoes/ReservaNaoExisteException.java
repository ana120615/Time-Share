package br.ufrpe.timeshare.excecoes;

public class ReservaNaoExisteException extends Exception {
    public ReservaNaoExisteException(String message){
        super(message);
    }
}
