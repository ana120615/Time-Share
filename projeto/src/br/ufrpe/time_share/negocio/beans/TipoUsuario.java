package br.ufrpe.time_share.negocio.beans;

public enum TipoUsuario {
    ADMINISTRADOR (1),
    COMUM (2);

    public final int VALOR;

    TipoUsuario(int valor) {
        this.VALOR = valor;
    }


}
