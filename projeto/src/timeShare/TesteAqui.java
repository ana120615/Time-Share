package timeShare;

import java.time.LocalDate;

public class TesteAqui {
    public static void main(String[] args) {

        Administrador adm1 = new Administrador("Samara", "1234567", "samara@gmail.com,"
                ,"samara.2005", LocalDate.of(2005,2,1));

        adm1.cadastrarBem(1111,"Casa na praia", "Linda e limpa", "Recife-PE",
                7);
        
        adm1.cadastrarBem(1345,"Apartamento", "Centro cidade", "Recife-PE",
                3);
        
        System.out.println(adm1.getBens());
        
       

    }
}
