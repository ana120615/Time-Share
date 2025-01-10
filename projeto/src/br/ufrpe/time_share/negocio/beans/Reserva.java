package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;
import java.util.List;

public class Reserva {
    private int id;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean status;
    private double taxa;
    private Usuario usuario;
    private UsuarioComum usuarioComum;
    private UsuarioAdm usuarioAdm;
    private Bem bem;
    private List<Cota> cotas;
    public Reserva(int id, LocalDate dataInicio, LocalDate dataFim, Usuario usuario, Bem bem, List<Cota> cotas) {
        this.id = id;
        this.status = true;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.taxa = 0.00d;
        this.usuario = usuario;
        this.bem = bem;
        this.cotas = cotas;
    }

  

    public void cancelarReserva() {
        this.status = false;
    }
    public boolean validarReserva(){
       
            for (Cota cota: cotas){
                if(!cota.isStatusDeDisponibilidadeParaReserva()){
                    // para ajudar no debug 
                    System.out.println("cota id" + cota.getId()+ "não está disponível para comprar");
                    return false;
                }
               
            } 
            //debug
            System.out.println("Períodos e cotas estão disponíveis e válidos!");
            return true;
       
    
    }

    public Bem getBem() {
        return bem;
    }

    public void setBem(Bem bem) {
        this.bem = bem;
    }

    public double getTaxa() {
        return taxa;
    }

    public void setTaxa(double taxa) {
        this.taxa = taxa;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return this.status;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public List<Cota> getCotas() {
        return cotas;
    }

    public void setCotas(List<Cota> cotas) {
        this.cotas = cotas;
  
    }
    public UsuarioComum getUsuarioComum() {
        return usuarioComum;
    }



    public void setUsuarioComum(UsuarioComum usuarioComum) {
        this.usuarioComum = usuarioComum;
    }
    public UsuarioAdm getUsuarioAdm() {
        return usuarioAdm;
    }



    public void setUsuarioAdm(UsuarioAdm usuarioAdm) {
        this.usuarioAdm = usuarioAdm;
    }


}
