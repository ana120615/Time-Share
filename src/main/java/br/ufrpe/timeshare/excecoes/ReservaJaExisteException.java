package br.ufrpe.timeshare.excecoes;

public class ReservaJaExisteException extends Exception {
    public ReservaJaExisteException(String message){
    super(message);
    }
}