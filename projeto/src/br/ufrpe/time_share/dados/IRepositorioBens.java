package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Bem;
import java.util.ArrayList;

public interface IRepositorioBens {

    void cadastrarBem(Bem bem);

    void editarBem(Bem bem);

    void removerBemPorId(int id);

    void removerBem(Bem bem);

    Bem buscarBemPorId(int id);

    Bem buscarBemPorNome(String nome);

    ArrayList<Bem> listarBensDisponiveis();

    ArrayList<Bem> listarBens();

}