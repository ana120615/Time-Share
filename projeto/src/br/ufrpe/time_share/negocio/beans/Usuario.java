package br.ufrpe.time_share.negocio.beans;

import java.util.ArrayList;
import java.time.LocalDate;

public class Usuario extends Pessoa {

    private ArrayList<Cota> cotasAdquiridas;

    public Usuario(String nome, String cpf, String email, String senha, LocalDate dataNascimento) {
        super(nome, cpf, email, senha, dataNascimento);
        this.cotasAdquiridas = new ArrayList<>();
    }

    public String consultarCotas() {
        String consulta = "";
        consulta += "Suas Cotas: \n";
        for(Cota cota : cotasAdquiridas) {
            consulta += "Descricao: " + cota.getDescricao() + " | Periodo: " + cota.getPeriodo();
            consulta += "\n";
        }

        return consulta;
    }

}