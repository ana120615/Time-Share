package timeShare;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Pessoa {

    private String cpf;
    private String nome;
    private String email;
    private String senha;
    private LocalDate dataNascimento;

    public Pessoa(String nome, String cpf, String email, String senha, LocalDate dataNascimento) {
        setCpf(cpf);
        setNome(nome);
        setEmail(email);
        setSenha(senha);
        setDataNascimento(dataNascimento);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null) {
            throw new IllegalArgumentException();
        }
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null) {
            throw new IllegalArgumentException();
        }
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException();
        }
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        if (senha == null) {
            throw new IllegalArgumentException();
        }
        this.senha = senha;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public int calcularIdade() {
        return (int) dataNascimento.until(LocalDate.now(), ChronoUnit.YEARS);
    }
}
