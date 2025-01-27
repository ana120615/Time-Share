package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Estadia;

import java.util.ArrayList;

public class RepositorioEstadia extends RepositorioGenerico<Estadia> implements IRepositorioEstadia {

    // INSTANCIA UNICA DO REPOSITORIO
    private static RepositorioEstadia instancia;

    public RepositorioEstadia() {
        super();
    }

    // METODO PARA OBTER A INSTANCIA DO REPOSITORIO
    public static RepositorioEstadia getInstancia() {
        if (instancia == null) {
            synchronized (RepositorioEstadia.class) { // Thread-safe
                if (instancia == null) {
                    instancia = new RepositorioEstadia();
                }
            }
        }
        return instancia;
    }


    @Override
    public ArrayList<Estadia> buscarEstadiaPorUsuario(long cpfUsuario) {
        ArrayList<Estadia> estadia = new ArrayList<>();

        for (Estadia est : lista) {
            if (est.getReserva().getUsuarioComum().getId() == cpfUsuario) {
                estadia.add(est);
            }
        }
        return estadia;
    }

    @Override
    public ArrayList<Estadia> buscarEstadiasPorBem(int idBem) {
        ArrayList<Estadia> estadia = new ArrayList<>();

        for (Estadia est : lista) {
            if (est.getReserva().getBem().getId() == idBem) {
                estadia.add(est);
            }
        }
        return estadia;
    }

    @Override
    public ArrayList<Estadia> buscarEstadiaPorReserva(int idReserva) {
        ArrayList<Estadia> estadia = new ArrayList<>();

        for (Estadia est : lista) {
            if (est.getReserva().getId() == idReserva) {
                estadia.add(est);
            }
        }
        return estadia;
    }

}
