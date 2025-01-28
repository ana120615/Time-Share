package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Bem;

import java.util.List;

public interface IRepositorioBens extends IRepositorio<Bem> {

    Bem buscarBemPorNome(String nome);

    List<Bem> listarBensDisponiveis();

    List<Bem> listarBensByNome(String nome);

}
