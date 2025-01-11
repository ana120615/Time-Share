package br.ufrpe.time_share.dados;

import java.util.ArrayList;

import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;

public class RepositorioCotas implements IRepositorioCotas {

    private ArrayList<Cota> listaCotas;

    {
        listaCotas = new ArrayList<>(50);
    }

    @Override
    public void cadastrarCota(Cota cota) {

    }

    @Override
    public void alterarCota(Cota cota) {

    }

    @Override
    public void excluirCota(Cota cota) {

    }

    @Override
    public void atualizarStatusCota(Cota cota, boolean statusAtualizado) {

    }

    @Override
    public boolean existeCota(Cota cota) {
        boolean existe = false;
        for (int i = 0; i < this.listaCotas.size() && !existe; i++) {
            if (this.listaCotas.get(i).equals(cota)) {
                existe = true;
            }
        }
        return existe;
    }

    @Override
    public Cota buscarCota(Cota cota) {
        return null;
    }

    @Override
    public Cota buscarCotaPorId(int id) {
        return null;
    }

    @Override
    public ArrayList<Cota> buscarCotasPorBem(Bem id) {
        return null;
    }

    @Override
    public ArrayList<Cota> listarCota() {
        return null;
    }

    @Override
    public ArrayList<Cota> listarCotaDisponivelParaVenda(Cota cota) {
        return null;
    }


//    //Construtor
//    public RepositorioCotas() {
//        super();
//    }
//
//    public void atualizarStatusCota(int cotaId, boolean status) {
//        for (Cota cota : getEntidades()) {
//            if (cota.getId() == cotaId) {
//                cota.setStatusDeDisponibilidadeParaCompra(status);
//            }
//        }
//    }
//
//    public void buscarCotasPorBem(Bem bem) {
//        bem.buscarCotas();
//    }
//
//    public List<Cota> buscarPorUsuario(Usuario usuario) {
//        List<Cota> resultado = new ArrayList<>();
//        for (Cota cota : getEntidades()) {
//            if (cota.getProprietario().equals(usuario)) {
//                resultado.add(cota);
//            }
//        }
//        return resultado;
//    }
//
//    public List<Cota> buscarCotasDisponiveis() {
//        List<Cota> resultado = new ArrayList<>();
//        for (Cota cota : getEntidades()) {
//            if (cota.verificarDisponibilidade()) {
//                resultado.add(cota);
//            }
//        }
//        return resultado;
//    }
}
