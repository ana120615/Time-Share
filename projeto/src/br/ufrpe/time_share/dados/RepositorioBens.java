package br.ufrpe.time_share.dados;

import java.util.ArrayList;
import br.ufrpe.time_share.negocio.beans.Bem;

public class RepositorioBens implements IRepositorioBens {
    private ArrayList<Bem> listaBem;

    {
        listaBem = new ArrayList<>(50);
    }

    @Override
    public void cadastrarBem(Bem bem) {
        this.listaBem.add(bem);
    }

    @Override
    public void editarBem(Bem bem) {

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
            bemProcurado = listaBem.get(i-1);
        }
        return bemProcurado;
    }

    @Override
    public Bem buscarBemPorNome(String nome) {
        Bem bemProcurado = null;
        boolean encontrado = false;
        int i;
        for (i = 0; i < this.listaBem.size() && !encontrado; i++) {
            if (this.listaBem.get(i).getNome().equals(nome)) {
                encontrado = true;
            }
        }

        if (encontrado) {
            bemProcurado = this.listaBem.get(i-1);
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
        return listaBensDisponiveis;
    }

    @Override
    public ArrayList<Bem> listarBens() {
        return this.listaBem;
    }
}


