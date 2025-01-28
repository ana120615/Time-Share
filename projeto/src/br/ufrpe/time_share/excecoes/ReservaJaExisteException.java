package br.ufrpe.time_share.excecoes;

public class ReservaJaExisteException extends Exception {
    public ReservaJaExisteException(String message){
    super(message);
    }
}