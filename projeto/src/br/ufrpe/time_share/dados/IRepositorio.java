package br.ufrpe.time_share.dados;

import java.util.List;

public interface IRepositorio<T> {

    void cadastrar(T obj);

    void remover(T obj);

    T buscar(long id);

    boolean existe(T obj);

    List<T> listar();
}
