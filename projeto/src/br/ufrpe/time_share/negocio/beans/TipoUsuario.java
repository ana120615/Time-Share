package br.ufrpe.time_share.negocio.beans;

public enum TipoUsuario {
    ADMINISTRADOR(1),
    COMUM(2);

    private final int valor;

    TipoUsuario(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public static TipoUsuario fromValor(int valor) {
        for (TipoUsuario tipo : TipoUsuario.values()) {
            if (tipo.getValor() == valor) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido para TipoUsuario: " + valor);
    }
}
