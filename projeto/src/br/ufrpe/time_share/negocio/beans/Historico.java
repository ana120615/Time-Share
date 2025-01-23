package br.ufrpe.time_share.negocio.beans;
import java.time.LocalDate;
public class Historico{

private String idHistorico;
private String idBem;
private String idUsuario; 
private LocalDate dataInicio;
private LocalDate dataFim;
private String detalhes;


public Historico (String idHistorico, String idBem, String  idUsuario, LocalDate dataInicio, LocalDate dataFim ){
    this.idHistorico = idHistorico;
        this.idBem = idBem;
        this.idUsuario = idUsuario;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.detalhes = detalhes;
}
public String  getIdHistorico() {
    return idHistorico;
}

public void setIdHistorico(String idHistorico) {
    this.idHistorico = idHistorico;
}

public String getIdBem() {
    return idBem;
}

public void setIdBem(String  idBem) {
    this.idBem = idBem;
}

public String getIdUsuario() {
    return idUsuario;
}

public void setIdUsuario(String idUsuario) {
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