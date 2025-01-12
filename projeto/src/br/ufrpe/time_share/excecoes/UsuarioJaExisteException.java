package br.ufrpe.time_share.excecoes;

public class UsuarioJaExisteException extends Exception {
    private int cpfUsuario;
    private String emailUsuario;

    public UsuarioJaExisteException(String message, int cpf, String emailUsuario) {
        super(message);
        this.cpfUsuario = cpf;
        this.emailUsuario = emailUsuario;
    }

    public int getCpfUsuario() {
        return cpfUsuario;
    }

    public void setCpfUsuario(int cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
}
