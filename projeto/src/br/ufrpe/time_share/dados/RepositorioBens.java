package br.ufrpe.time_share.dados;

import java.util.ArrayList;
import java.util.List;

import br.ufrpe.time_share.negocio.beans.Bem;

public class RepositorioBens implements IRepositorioBens {
    @Override
    public void cadastrarBem(Bem bem) {

    }

    @Override
    public void editarBem(Bem bem) {

    }

    @Override
    public void removerBemPorId(int id) {

    }

    @Override
    public void removerBem(Bem bem) {

    }

    @Override
    public Bem buscarBemPorId(int id) {
        return null;
    }

    @Override
    public Bem buscarBemPorNome(String nome) {
        return null;
    }

    @Override
    public ArrayList<Bem> listarBensDisponiveis() {
        return null;
    }

    @Override
    public ArrayList<Bem> listarBens() {
        return null;
    }

//    //Construtor
//    public RepositorioBens() {
//        super();
//    }
//
//    public Bem buscarBemPorId(int id) {
//        Bem resultado = null;
//        for (Bem bem : getEntidades()) {
//            if (bem.getId() == id) {
//                resultado = bem;
//            }
//        }
//        return resultado; // Retorna null se não encontrar
//    }
//
//    public void atualizarCapacidade(int bemId, int novaCapacidade) {
//        for (Bem bem : getEntidades()) {
//            if (bem.getId() == bemId) {
//                bem.setCapacidade(novaCapacidade);
//            }
//        }
//    }
//
//    public List<Bem> buscarBensDisponiveis() {
//        List<Bem> resultado = new ArrayList<>();
//        for (Bem bem : getEntidades()) {
//            if (bem.verificarDisponibilidade()) {
//                resultado.add(bem);
//            }
//        }
//        return resultado;
//    }
}


