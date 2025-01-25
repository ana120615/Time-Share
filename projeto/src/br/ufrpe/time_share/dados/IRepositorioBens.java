package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Bem;
import java.util.ArrayList;

public interface IRepositorioBens {

    void cadastrarBem(Bem bem);

    void removerBem(Bem bem);

    Bem buscarBemPorId(int id);

    Bem buscarBemPorNome(String nome);

    boolean existe(Bem bem);

    ArrayList<Bem> listarBensDisponiveis();

    ArrayList<Bem> listarBens();

    ArrayList<Bem> listarBensByNome(String nome);

}
