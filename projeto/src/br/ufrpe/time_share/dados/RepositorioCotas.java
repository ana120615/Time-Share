package br.ufrpe.time_share.dados;

import java.util.ArrayList;
import java.util.List;

import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Usuario;

public class RepositorioCotas extends Repositorio<Cota> {
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
