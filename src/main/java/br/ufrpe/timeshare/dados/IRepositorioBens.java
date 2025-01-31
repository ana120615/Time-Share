package br.ufrpe.timeshare.dados;

import br.ufrpe.timeshare.negocio.beans.Bem;

import java.util.List;

public interface IRepositorioBens extends IRepositorio<Bem> {

    Bem buscarBemPorNome(String nome);

    List<Bem> listarBensDisponiveis();

    List<Bem> listarBensByNome(String nome);

}
