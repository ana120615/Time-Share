package br.ufrpe.timeshare.dados;

import br.ufrpe.timeshare.negocio.beans.Bem;

import java.util.List;

public interface IRepositorioBens extends IRepositorio<Bem> {

    List<Bem> listarBensUsuarioPorNome(String nome, long idUsuario);

    List<Bem> listarBensDisponiveis();

    List<Bem> listarBensDisponiveisPorNome(String nome);

    List<Bem> listarBensPorLocalizacao(String localizacao);
    
    List<Bem> listarBensPorDescricao(String descricao);

}
