package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;
import java.util.ArrayList;


public class UsuarioComum extends Usuario {
    public final String nivelAcesso = "COMUM";
    private ArrayList<Cota> cotasAdquiridas;

    {
        this.cotasAdquiridas = new ArrayList<>(10);
    }

    //CONSTRUTORES
    public UsuarioComum(int cpf, String nome, String email, String senha, LocalDate dataNascimento) {
        super(cpf, nome, email, senha, dataNascimento);
    }

    public UsuarioComum(int cpf, String senha) {
        super(cpf, senha);
    }


    //METODOS GET E SET
    public ArrayList<Cota> getCotasAdquiridas() {
        return this.cotasAdquiridas;
    }
    // adicionei para poder validas as cotas em reserva
    public boolean temCotasAdquiridas(){
        return cotasAdquiridas !=null && !cotasAdquiridas.isEmpty();
    }

    public void setCotasAdquiridas(ArrayList<Cota> cotasAdquiridas) {
        if (cotasAdquiridas != null) {
            this.cotasAdquiridas.addAll(cotasAdquiridas);
        }
    }


    //OUTROS METODOS
    public boolean revenderDireitoUso(Usuario usuario, Cota cota) {
        boolean retorno = false;
        if (usuario != null && !this.equals(usuario)) {
            if (this.cotasAdquiridas.contains(cota)) {
                cota.setProprietario((UsuarioComum) usuario);
                this.cotasAdquiridas.remove(cota);
                retorno = true;
            }
        }
        return retorno;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UsuarioComum) {
            return super.equals(obj);
        }
        return false;
    }

}
