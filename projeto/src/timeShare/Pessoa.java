package timeShare;

public class Pessoa {
    

private String cpf;
private String nome;
private String email;
private String senha;

public Pessoa(String nome, String cpf, String email, String senha) {
		this.nome=nome;
		this.cpf=cpf;
		this.email=email;
		this.senha=senha;
	}
public void setCpf(String cpf) {
	this.cpf=cpf;
}
public String getCpf() {
	return this.cpf;
}
public void setNome(String nome) {
	this.nome=nome;
}
public String getNome() {
	return this.nome;
}
public void setEmail(String email) {
	this.email=email;
}
public String getEmail() {
	return this.email;
}
public void setSenha(String senha) {
	this.senha=senha;
}
public String getSenha() {
	return this.senha;
}

    
}
