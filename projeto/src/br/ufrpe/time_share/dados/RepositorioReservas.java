package br.ufrpe.time_share.dados;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Reserva;
// pendencias : listarReservas, salvar Reservas, ver a lógica para quem não tem cotas. 
public class RepositorioReservas implements IRepositorioReservas {

    private RepositorioCotas repositorioCotas;
    //Construtor
    public RepositorioReservas(){
        super();
        this.reservas = new ArrayList<>();
    }
    private List<Reserva> reservas;
        private LocalDate novaDataFim;
        
        //depois vai ser alterado para integrar o controlador 
       public void disponibilizarCotas (Reserva reserva){
        for (Cota cota : reserva.getCotas()){
            //*repositorioCotas.atualizarStatusCota(cota.getId(), true);  como comentário porque o repositorio de reservas está todo como comentário*//
        }
       }
    
    
        @Override
        public void cadastrarReserva(Reserva reserva) {
    
        }
    
        @Override
        public boolean removerReserva( int idReserva) {
            
         Reserva reserva = buscarReservasPorId(idReserva);
            if ( reserva != null && reserva.getStatus()){
            reserva.cancelarReserva();
            disponibilizarCotas(reserva);
            return true;
            }
            return false;
    
        }
    
        @Override
        public Reserva atualizarReserva(int idReserva, LocalDate novaDataInicio, LocalDate novaDataFim ){
            this.novaDataFim = novaDataFim;
            Reserva reserva =  buscarReservasPorId(idReserva);
        if (reserva != null ){
            reserva.setDataInicio(novaDataInicio);
            reserva.setDataFim(novaDataFim);
            return reserva;
        }else {
            System.out.println("reserva não encontrada");
            return null;
        }
         
    }

    @Override
    public Reserva buscarReserva(Reserva reserva) {
        return null;
    }

    @Override
    public ArrayList<Reserva> buscarReservasPorUsuario(int idUsuario) {
       
            List<Reserva> reservasPorUsuario = new ArrayList<>();
            for (Reserva reserva : reservas ){
                if (reserva.getUsuarioAdm().getCpf() == idUsuario){
                    reservasPorUsuario.add(reserva);
                }
                if ( reserva.getUsuarioComum().getCpf() == idUsuario){
                    reservasPorUsuario.add(reserva);
                }
            } 
            return (ArrayList<Reserva>) reservasPorUsuario;
        }
    

    @Override
    public ArrayList<Reserva> buscarReservasPorBem(int idBem) {
     
            List<Reserva> reservasPorBem = new ArrayList<>();
     
            for ( Reserva reserva : reservas){
     
             if(reserva.getBem().getId() == idBem){
                 reservasPorBem.add(reserva);
             }
             
            }
            return (ArrayList<Reserva>) reservasPorBem;
    }

    @Override
    public Reserva buscarReservasPorId(int idReserva) {
     
    for (Reserva reserva : reservas) { 
        if (reserva.getId() == idReserva) {
          return reserva;
        }
    }
    return null;
}

    @Override
    public ArrayList<Reserva> listarReservas() {
        return null;
    }
}
