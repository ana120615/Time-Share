package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Entidade;

import java.util.ArrayList;
import java.util.List;

public abstract class RepositorioGenerico<T extends Entidade> implements IRepositorio<T> {

    protected List<T> lista;

    public RepositorioGenerico() {
        this.lista = new ArrayList<>();
    }

    @Override
    public void cadastrar(T obj) {
        lista.add(obj);
    }

    @Override
    public T buscar(long id) {
        for (T obj : lista) {
            if (obj.getId() == id) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public void remover(T obj) {
       lista.remove(obj);
    }

    @Override
    public boolean existe(T obj) {
        boolean existe = false;
        for (int i = 0; i < lista.size() && !existe; i++) {
            if (lista.get(i).equals(obj)) {
                existe = true;
            }
        }
        return existe;
    }

    @Override
    public List<T> listar() {
        return lista;
    }


}
