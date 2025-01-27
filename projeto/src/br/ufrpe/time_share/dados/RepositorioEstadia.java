package br.ufrpe.time_share.dados;

import br.ufrpe.time_share.negocio.beans.Estadia;

import java.util.ArrayList;

public class RepositorioEstadia implements IRepositorioEstadia {

    // INSTANCIA UNICA DO REPOSITORIO
    public static final RepositorioEstadia INSTANCE = new RepositorioEstadia();

    private ArrayList<Estadia> estadias;

    {
        this.estadias = new ArrayList<>();
    }

    // METODO PARA OBTER A INSTANCIA DO REPOSITORIO
    public static RepositorioEstadia getInstance() {
        return INSTANCE;
    }

    @Override
    public void cadastrarEstadia(Estadia estadia) {
        this.estadias.add(estadia);
    }

    @Override
    public void removerEstadia(Estadia estadia) {
        this.estadias.remove(estadia);
    }

    @Override
    public Estadia buscarEstadiaPorId(int id) {
        Estadia estadia = null;
        boolean encontrado = false;
        int i;
        for (i = 0; i < this.estadias.size() && !encontrado; i++) {
            if (this.estadias.get(i).getId() == id) {
                encontrado = true;
            }
        }

        if (encontrado) {
            estadia = this.estadias.get(i - 1);
        }
        return estadia;
    }

    @Override
    public ArrayList<Estadia> buscarEstadiaPorUsuario(String cpfUsuario) {
        ArrayList<Estadia> estadia = new ArrayList<>();

        for (Estadia est : this.estadias) {
            if (est.getReserva().getUsuarioComum().getId().equals(cpfUsuario)) {
                estadia.add(est);
            }
        }
        return estadia;
    }

    @Override
    public ArrayList<Estadia> buscarEstadiasPorBem(int idBem) {
        ArrayList<Estadia> estadia = new ArrayList<>();

        for (Estadia est : this.estadias) {
            if (est.getReserva().getBem().getId() == idBem) {
                estadia.add(est);
            }
        }
        return estadia;
    }

    @Override
    public ArrayList<Estadia> buscarEstadiaPorReserva(int idReserva) {
        ArrayList<Estadia> estadia = new ArrayList<>();

        for (Estadia est : this.estadias) {
            if (est.getReserva().getId() == idReserva) {
                estadia.add(est);
            }
        }
        return estadia;
    }

    @Override
    public ArrayList<Estadia> listarEstadias() {
        return this.estadias;
    }
}
