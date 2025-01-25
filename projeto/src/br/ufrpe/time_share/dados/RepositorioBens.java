package br.ufrpe.time_share.dados;

import java.util.ArrayList;

import br.ufrpe.time_share.negocio.beans.Bem;

public class RepositorioBens implements IRepositorioBens {

    // INSTANCIA UNICA DO REPOSITORIO
    private static final RepositorioBens INSTANCE = new RepositorioBens();

    private ArrayList<Bem> listaBem;

    {
        listaBem = new ArrayList<>();
    }

    // METODO PARA OBTER A INSTANCIA DO REPOSITORIO
    public static RepositorioBens getInstancia() {
        return INSTANCE;
    }

    @Override
    public void cadastrarBem(Bem bem) {
        this.listaBem.add(bem);
    }

    @Override
    public void removerBem(Bem bem) {
        this.listaBem.remove(bem);
    }

    @Override
    public Bem buscarBemPorId(int id) {
        Bem bemProcurado = null;
        boolean encontrado = false;
        int i;
        for (i = 0; i < listaBem.size() && !encontrado; i++) {
            if (listaBem.get(i).getId() == id) {
                encontrado = true;
            }
        }

        if (encontrado) {
            bemProcurado = listaBem.get(i - 1);
        }
        return bemProcurado;
    }

    @Override
    public Bem buscarBemPorNome(String nome) {
        Bem bemProcurado = null;
        boolean encontrado = false;
        int i;
        for (i = 0; i < this.listaBem.size() && !encontrado; i++) {
            if (this.listaBem.get(i).getNome().equalsIgnoreCase(nome)) {
                encontrado = true;
            }
        }

        if (encontrado) {
            bemProcurado = this.listaBem.get(i - 1);
        }
        return bemProcurado;
    }

    @Override
    public boolean existe(Bem bem) {
        boolean existe = false;
        for (int i = 0; i < this.listaBem.size() && !existe; i++) {
            if (this.listaBem.get(i).equals(bem)) {
                existe = true;
            }
        }
        return existe;
    }

    @Override
    public ArrayList<Bem> listarBensDisponiveis() {
        ArrayList<Bem> listaBensDisponiveis = new ArrayList<>();
        for (Bem bem : this.listaBem) {
            if (bem.isOfertado()) {
                listaBensDisponiveis.add(bem);
            }
        }
        return new ArrayList<>(listaBensDisponiveis);
    }

    @Override
    public ArrayList<Bem> listarBens() {
        return new ArrayList<>(listaBem);
    }

    @Override
    public ArrayList<Bem> listarBensByNome(String nome) {
        ArrayList<Bem> listaBensByNome = new ArrayList<>();
        for (Bem bem : this.listaBem) {
            if (bem.getNome().equalsIgnoreCase(nome) || bem.getNome().contains(nome)) {
                listaBensByNome.add(bem);
            }
        }
        return listaBensByNome;
    }


}


