package br.ufrpe.timeshare.dados;

import br.ufrpe.timeshare.negocio.beans.Estadia;

import java.util.ArrayList;
import java.util.List;

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
    public List<Estadia> buscarEstadiaPorUsuario(long cpfUsuario) {
        List<Estadia> estadia = new ArrayList<>();

        for (Estadia est : lista) {
            if (est.getReserva().getUsuarioComum().getId() == cpfUsuario) {
                estadia.add(est);
            }
        }
        return estadia;
    }

    @Override
    public List<Estadia> buscarEstadiasPorBem(int idBem) {
        List<Estadia> estadia = new ArrayList<>();

        for (Estadia est : lista) {
            if (est.getReserva().getBem().getId() == idBem) {
                estadia.add(est);
            }
        }
        return estadia;
    }

    @Override
    public List<Estadia> buscarEstadiaPorReserva(int idReserva) {
        List<Estadia> estadia = new ArrayList<>();

        for (Estadia est : lista) {
            if (est.getReserva().getId() == idReserva) {
                estadia.add(est);
            }
        }
        return estadia;
    }

}
