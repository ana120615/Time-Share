package br.ufrpe.timeshare.dados;

import java.util.ArrayList;
import java.util.List;

import br.ufrpe.timeshare.negocio.beans.Bem;

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
    public Bem buscarBemPorNome(String nome) {
        Bem bemProcurado = null;
        boolean encontrado = false;
        int i;
        for (i = 0; i < lista.size() && !encontrado; i++) {
            if (lista.get(i).getNome().equalsIgnoreCase(nome)) {
                encontrado = true;
            }
        }

        if (encontrado) {
            bemProcurado = lista.get(i - 1);
        }
        return bemProcurado;
    }


    @Override
    public List<Bem> listarBensDisponiveis() {
        List<Bem> listaBensDisponiveis = new ArrayList<>();
        for (Bem bem : lista) {
            if (bem.isOfertado()) {
                listaBensDisponiveis.add(bem);
            }
        }
        return listaBensDisponiveis;
    }

    @Override
    public List<Bem> listarBensByNome(String nome) {
        List<Bem> listaBensByNome = new ArrayList<>();
        for (Bem bem : lista) {
            if (bem.getNome().equalsIgnoreCase(nome) || bem.getNome().contains(nome)) {
                listaBensByNome.add(bem);
            }
        }
        return listaBensByNome;
    }

}


