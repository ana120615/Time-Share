package br.ufrpe.time_share.excecoes;

public class UsuarioJaExisteException extends Exception {
    private long cpfUsuario;
    private String emailUsuario;

    public UsuarioJaExisteException(String message, long cpf, String emailUsuario) {
        super(message);
        this.cpfUsuario = cpf;
        this.emailUsuario = emailUsuario;
    }

    public long getCpfUsuario() {
        return cpfUsuario;
    }

    public void setCpfUsuario(long cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
}
