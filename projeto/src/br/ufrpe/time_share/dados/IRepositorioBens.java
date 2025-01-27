package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Bem;

import java.util.ArrayList;

public interface IRepositorioBens extends IRepositorio<Bem> {

    Bem buscarBemPorNome(String nome);

    ArrayList<Bem> listarBensDisponiveis();

    ArrayList<Bem> listarBensByNome(String nome);

    // TODO listar bem de um adm
}
