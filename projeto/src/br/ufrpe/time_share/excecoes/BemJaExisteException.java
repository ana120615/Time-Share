package br.ufrpe.time_share.excecoes;

public class BemJaExisteException extends RuntimeException {

  public BemJaExisteException(String message) {
      super(message);
    }
}
