package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Usuario extends Entidade {

    private long id;
    private String nome;
    private String email;
    private String senha;
    private LocalDate dataNascimento;
    private TipoUsuario tipo;

    //CONSTRUTORES
    public Usuario(long cpf, String nome, String email, String senha, LocalDate dataNascimento, TipoUsuario tipo) {
        this.setId(cpf);
        this.setNome(nome);
        this.setEmail(email);
        this.setSenha(senha);
        this.setDataNascimento(dataNascimento);
        this.setTipo(tipo);
    }

    public Usuario(long cpf, String senha, TipoUsuario tipo) {
        this.setId(cpf);
        this.setSenha(senha);
        this.setTipo(tipo);
    }

    //METODOS GET E SET
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }


    //METODO PARA CALCULAR IDADE
    public int calcularIdade() {
        return (int) dataNascimento.until(LocalDate.now(), ChronoUnit.YEARS);
    }

    //METODO PARA VERIFICAR ANIVERSARIO
    public boolean verificarAniversario() {
        boolean ehNiver = false;
        LocalDate dataAtual = LocalDate.now();

        if (dataAtual.getDayOfMonth() == dataNascimento.getDayOfMonth() &&
                dataAtual.getMonth() == dataNascimento.getMonth()) {
            ehNiver = true;
        }
        return ehNiver;
    }


    @Override
    public String toString() {
        return "\n" +
                "cpf=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", tipo='" + tipo + '\'' +
                ", dataNascimento=" + dataNascimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Override
    public boolean equals(Object obj) {
        boolean ret = false;
        if (obj instanceof Usuario) {
            Usuario usuario = (Usuario) obj;
            if (this.id == usuario.getId() || this.email.equals(usuario.getEmail())) {
                ret = true;
            }
        }
        return ret;
    }
}
