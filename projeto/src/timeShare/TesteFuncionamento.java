package timeShare;

public class TesteFuncionamento {
    public static void main(String[] args) {

        Bem b1 = new Bem("casaNaPraia", "Para todas as pessoas",
                "Recife-Pe", 7);

        Cota c1 = new Cota(124554, 50, 18000);


        b1.criarCotasParaBem(c1);

    }
}
