package br.ufrpe.timeshare.dados;

import br.ufrpe.timeshare.negocio.beans.Bem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepositorioBens extends RepositorioGenerico<Bem> implements IRepositorioBens {

    // INSTANCIA UNICA DO REPOSITORIO
    private static RepositorioBens instancia;

    public RepositorioBens() {
        super();
    }

    // METODO PARA OBTER A INSTANCIA DO REPOSITORIO
    public static RepositorioBens getInstancia() {
        if (instancia == null) {
            synchronized (RepositorioBens.class) { // Thread-safe
                if (instancia == null) {
                    instancia = new RepositorioBens();
                }
            }
        }
        return instancia;
    }



    @Override
    public List<Bem> listarBensDisponiveis() {
        List<Bem> listaBensDisponiveis = new ArrayList<>();
        for (Bem bem : lista) {
            if (bem.isOfertado()) {
                listaBensDisponiveis.add(bem);
            }
        }
        Collections.sort(listaBensDisponiveis);
        return listaBensDisponiveis;
    }

    @Override
    public List<Bem> listarBensPorNome(String nome) {
        List<Bem> listaBensByNome = new ArrayList<>();
        for (Bem bem : lista) {
            if (bem.getNome().equalsIgnoreCase(nome) || bem.getNome().contains(nome)) {
                listaBensByNome.add(bem);
            }
        }
        Collections.sort(listaBensByNome);
        return listaBensByNome;
    }

    @Override
    public List<Bem> listarBensUsuarioPorNome(String nome, int idUsuario) {
        List<Bem> listaBensByNome = new ArrayList<>();
        for (Bem bem : lista) {
            if ((bem.getCadastradoPor().getId() == idUsuario) && (bem.getNome().equalsIgnoreCase(nome) || bem.getNome().contains(nome))) {
                listaBensByNome.add(bem);
            }
        }
        Collections.sort(listaBensByNome);
        return listaBensByNome;
    }

}


