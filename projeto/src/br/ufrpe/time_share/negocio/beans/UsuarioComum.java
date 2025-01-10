package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;
import java.util.ArrayList;


public class UsuarioComum extends Usuario {
    public final String nivelAcesso = "COMUM";
    private ArrayList<Cota> cotasAdquiridas;

    //Construtor
    public UsuarioComum(int cpf, String nome, String email, String senha, LocalDate dataNascimento) {
        super(cpf, nome, email, senha, dataNascimento);
    }

    public UsuarioComum(int cpf, String senha) {
        super(cpf, senha);
    }



    public ArrayList<Cota> getCotasAdquiridas() {
        return cotasAdquiridas;
    }
    // adicionei para poder validas as cotas em reserva
    public boolean temCotasAdquiridas(){
        return cotasAdquiridas !=null && !cotasAdquiridas.isEmpty();
    }

    public void setCotasAdquiridas(ArrayList<Cota> cotasAdquiridas) {
        this.cotasAdquiridas = cotasAdquiridas;
    }

    public String consultarCotas() {
        return "";
    }

    @Override
    public boolean eAdm() {
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + " " +
                "nivelAcesso=" + nivelAcesso;
    }

}
