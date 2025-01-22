package br.ufrpe.time_share.excecoes;

public class UsuarioJaExisteException extends Exception {
    private String cpfUsuario;
    private String emailUsuario;

    public UsuarioJaExisteException(String message, String cpf, String emailUsuario) {
        super(message);
        this.cpfUsuario = cpf;
        this.emailUsuario = emailUsuario;
    }

    public String getCpfUsuario() {
        return cpfUsuario;
    }

    public void setCpfUsuario(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
}
