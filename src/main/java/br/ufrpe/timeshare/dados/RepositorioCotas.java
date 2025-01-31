package br.ufrpe.timeshare.dados;

import java.util.ArrayList;
import java.util.List;

import br.ufrpe.timeshare.negocio.beans.Bem;
import br.ufrpe.timeshare.negocio.beans.Cota;
import br.ufrpe.timeshare.negocio.beans.Usuario;

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
    public void cadastrarCotas(List<Cota> cotas) {
        lista.addAll(cotas);
    }


    @Override
    public List<Cota> buscarCotasPorProprietario(Usuario proprietario) {
        List<Cota> resultado = new ArrayList<>();
        for (Cota cota : lista) {
            if (!cota.getStatusDeDisponibilidadeParaCompra() && cota.getProprietario().equals(proprietario)) {
                resultado.add(cota);
            }
        }

        return resultado;
    }

    @Override
    public List<Cota> buscarCotasPorBem(Bem bem) {
        List<Cota> retorno = new ArrayList<>();
        for (Cota cota : lista) {
            if (cota.getBemAssociado().equals(bem)) {
                retorno.add(cota);
            }
        }

        return retorno;
    }

    @Override
    public List<Cota> listarCotasDisponiveisParaVenda() {
        List<Cota> resultado = new ArrayList<>();
        for (Cota cota : lista) {
            if (cota.isStatusDeDisponibilidadeParaCompra()) {
                resultado.add(cota);
            }
        }

        return resultado;
    }
}
