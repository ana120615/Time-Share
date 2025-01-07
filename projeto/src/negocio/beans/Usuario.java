package negocio.beans;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Usuario {

    private String cpf;
    private String nome;
    private String email;
    private String senha;
    private LocalDate dataNascimento;

    //Construtor
    public Usuario(String cpf, String nome, String email, String senha, LocalDate dataNascimento){
    this.setCpf(cpf);
    this.setNome(nome);
    this.setEmail(email);
    this.setSenha(senha);
    this.setDataNascimento(dataNascimento);
    }

    //METODOS GET E SET
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    //METODO PARA CALCULAR IDADE
        public int calcularIdade() {
        return (int) dataNascimento.until(LocalDate.now(), ChronoUnit.YEARS);
    }

    //METODO ABSTRATO
public abstract boolean eAdm();
}
