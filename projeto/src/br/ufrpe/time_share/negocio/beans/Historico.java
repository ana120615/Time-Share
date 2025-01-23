package br.ufrpe.time_share.negocio.beans;
import java.time.LocalDate;
public class Historico{

private int idHistorico;
private int idBem;
private int idUsuario; 
private LocalDate dataInicio;
private LocalDate dataFim;
private String detalhes;


public Historico (int idHistorico, int idBem, int idUsuario, LocalDate dataInicio, LocalDate dataFim ){
    this.idHistorico = idHistorico;
        this.idBem = idBem;
        this.idUsuario = idUsuario;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.detalhes = detalhes;
}
public int getIdHistorico() {
    return idHistorico;
}

public void setIdHistorico(int idHistorico) {
    this.idHistorico = idHistorico;
}

public int getIdBem() {
    return idBem;
}

public void setIdBem(int idBem) {
    this.idBem = idBem;
}

public int getIdUsuario() {
    return idUsuario;
}

public void setIdUsuario(int idUsuario) {
    this.idUsuario = idUsuario;
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

public String getDetalhes() {
    return detalhes;
}

public void setDetalhes(String detalhes) {
    this.detalhes = detalhes;
}

//@Override
   // public String toString() {
     //   return "Historico{" +
       //         "idBem=" + idBem +
         //       ", usuario=" + usuarioComum.getCPF()
           //     ", dataInicio=" + dataInicio +
             //   ", dataFim=" + dataFim +
               // '}';
    //}
}