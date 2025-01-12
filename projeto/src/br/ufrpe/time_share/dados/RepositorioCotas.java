package br.ufrpe.time_share.dados;

import java.util.ArrayList;

import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.UsuarioComum;

public class RepositorioCotas implements IRepositorioCotas {

    private ArrayList<Cota> listaCotas;

    {
        listaCotas = new ArrayList<>(50);
    }

    @Override
    public void cadastrarCota(Cota cota) {
      listaCotas.add(cota);
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
    public void atualizarStatusCota(Cota cota, boolean statusAtualizado, boolean isCompra, boolean isReserva) {
        //isCompra: quero mudar status de disponibilidade de compra
        //isReserva: quero mudar status de disponibilidade de reserva
        //ou posso modificar os dois
        if(isCompra&&!isReserva){
            cota.setStatusDeDisponibilidadeParaCompra(statusAtualizado);
        }
        else if(isReserva&&!isCompra){
            cota.setStatusDeDisponibilidadeParaReserva(statusAtualizado);
        }
        else{
            cota.setStatusDeDisponibilidadeParaCompra(statusAtualizado);
            cota.setStatusDeDisponibilidadeParaReserva(statusAtualizado);
        }
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
   
    //@Override
    //public Cota buscarCota(Cota cota) {
    //ISSO PODERIA SER FEITO NO CONTROLADOR ESCOLHENDO O METODO DE BUSCA:
    //POR ID, POR BEM OU POR PROPRIETARIO
    //}

    @Override
    public ArrayList<Cota> buscarCotasPorProprietario(UsuarioComum proprietario){
    return proprietario.getCotasAdquiridas();
    }

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
    public ArrayList<Cota> buscarCotasPorBem(Bem bem) {
     ArrayList<Cota> retorno=new ArrayList<>();
     for(Cota cota: listaCotas){
        if(cota.getBemAssociado().equals(bem)){
            retorno.add(cota);
        }
     }
        return retorno;
    }

    @Override
    public ArrayList<Cota> listarCotas() {
        ArrayList<Cota> retorno=new ArrayList<>();
        for(Cota cota:listaCotas){
         retorno.add(cota);
        }
        return retorno;
    }

    @Override
    public ArrayList<Cota> listarCotasDisponiveisParaVenda() {
        ArrayList<Cota>retorno=new ArrayList<>();
        for(Cota cota:listaCotas){
            if(cota.isStatusDeDisponibilidadeParaCompra()){
                retorno.add(cota);
            }
        }
        return retorno;
    }
}
