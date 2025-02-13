package br.ufrpe.timeshare.dados;

import br.ufrpe.timeshare.negocio.beans.Bem;

import java.util.List;

public interface IRepositorioBens extends IRepositorio<Bem> {

    List<Bem> listarBensUsuarioPorNome(String nome, int idUsuario);

    List<Bem> listarBensDisponiveis();

    List<Bem> listarBensPorNome(String nome);

}
