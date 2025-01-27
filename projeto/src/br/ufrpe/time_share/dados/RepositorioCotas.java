package br.ufrpe.time_share.dados;

import java.util.ArrayList;

import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Usuario;

public class RepositorioCotas extends RepositorioGenerico<Cota> implements IRepositorioCotas {

    // INSTANCIA UNICA DO REPOSITORIO
    private static RepositorioCotas instancia;

    public RepositorioCotas() {
        super();
    }

    // METODO PARA OBTER A INSTANCIA DO REPOSITORIO
    public static RepositorioCotas getInstancia() {
        if (instancia == null) {
            synchronized (RepositorioCotas.class) { // Thread-safe
                if (instancia == null) {
                    instancia = new RepositorioCotas();
                }
            }
        }
        return instancia;
    }


    @Override
    public void cadastrarCotas(ArrayList<Cota> cotas) {
        lista.addAll(cotas);
    }


    @Override
    public ArrayList<Cota> buscarCotasPorProprietario(Usuario proprietario) {
        ArrayList<Cota> resultado = new ArrayList<>();
        for (Cota cota : lista) {
            if (!cota.getStatusDeDisponibilidadeParaCompra() && cota.getProprietario().equals(proprietario)) {
                resultado.add(cota);
            }
        }

        return resultado;
    }

    @Override
    public ArrayList<Cota> buscarCotasPorBem(Bem bem) {
        ArrayList<Cota> retorno = new ArrayList<>();
        for (Cota cota : lista) {
            if (cota.getBemAssociado().equals(bem)) {
                retorno.add(cota);
            }
        }
        return new ArrayList<>(retorno);
    }

    @Override
    public ArrayList<Cota> listarCotasDisponiveisParaVenda() {
        ArrayList<Cota> resultado = new ArrayList<>();
        for (Cota cota : lista) {
            if (cota.isStatusDeDisponibilidadeParaCompra()) {
                resultado.add(cota);
            }
        }
        return new ArrayList<>(resultado);
    }
}
