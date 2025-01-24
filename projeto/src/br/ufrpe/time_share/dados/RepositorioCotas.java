package br.ufrpe.time_share.dados;

import java.util.ArrayList;

import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Usuario;

public class RepositorioCotas implements IRepositorioCotas {

    // INSTANCIA UNICA DO REPOSITORIO
    private static final RepositorioCotas INSTANCE = new RepositorioCotas();

    private ArrayList<Cota> listaCotas;

    {
        listaCotas = new ArrayList<>();
    }

    // METODO PARA OBTER A INSTANCIA DO REPOSITORIO
    public static RepositorioCotas getInstancia() {
        return INSTANCE;
    }

    @Override
    public void cadastrarCota(Cota cota) {
      listaCotas.add(cota);
    }

    @Override
    public void cadastrarCotas(ArrayList<Cota> cotas) {
        listaCotas.addAll(cotas);
    }

    @Override
    public void alterarCota(Cota cotaAtualizada) {
    //no controlador seria feita a verificacao 
    //de cada parte que seria modificada e se a cota existe, 
    //entre outros aspectos, mas aqui ha uma substituicao direta
    for (int i = 0; i < listaCotas.size(); i++) {
        if (listaCotas.get(i).getId() == cotaAtualizada.getId()) {
            listaCotas.set(i, cotaAtualizada);
        }
    }
    }

    @Override
    public void excluirCota(Cota cota) {
    listaCotas.remove(cota);
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

//    @Override
//    public ArrayList<Cota> buscarCotasPorProprietario(Usuario proprietario){
//        ArrayList<Cota> resultado = new ArrayList<>();
//        for(Cota cota : listaCotas) {
//            if(cota.getProprietario().equals(proprietario)) {
//                resultado.add(cota);
//            }
//        }
//        return new ArrayList<>(resultado);
//    }

    @Override
    public Cota buscarCotaPorId(int id) {
        Cota retorno = null;
    for(Cota cota: listaCotas){
        if(cota.getId()==id){
        retorno = cota;
        }
    }
        return retorno;
    }

    @Override
    public ArrayList<Cota> buscarCotasPorProprietario(Usuario proprietario) {
        ArrayList<Cota> resultado = new ArrayList<>();
        for (Cota cota : listaCotas) {
            if (!cota.getStatusDeDisponibilidadeParaCompra() && cota.getProprietario().equals(proprietario)) {
                resultado.add(cota);
            }
        }

        return resultado;
    }

    @Override
    public ArrayList<Cota> buscarCotasPorBem(Bem bem) {
     ArrayList<Cota> retorno=new ArrayList<>();
     for(Cota cota: listaCotas){
        if(cota.getBemAssociado().equals(bem)){
            retorno.add(cota);
        }
     }
        return new ArrayList<>(retorno);
    }

    @Override
    public ArrayList<Cota> listarCotas() {
        return new ArrayList<>(listaCotas);
    }
    
    @Override
    public ArrayList<Cota> listarCotasDisponiveisParaVenda() {
        ArrayList<Cota> resultado = new ArrayList<>();
        for(Cota cota : listaCotas) {
            if(cota.isStatusDeDisponibilidadeParaCompra()) {
                resultado.add(cota);
            }
        }
        return new ArrayList<>(resultado);
    }
}
