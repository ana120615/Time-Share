package br.ufrpe.timeshare.dados;

import br.ufrpe.timeshare.negocio.beans.Venda;

public class RepositorioVenda extends RepositorioGenerico<Venda> implements IRepositorioVenda {

    // INSTANCIA UNICA DO REPOSITORIO
    private static volatile RepositorioVenda instancia;

    public RepositorioVenda() {
        super();
    }

    // METODO PARA OBTER A INSTANCIA DO REPOSITORIO
    public static RepositorioVenda getInstancia() {
        if (instancia == null) { // Primeira verificação sem bloqueio
            synchronized (RepositorioVenda.class) { // Bloqueia somente se for necessário
                if (instancia == null) { // Segunda verificação dentro do bloco sincronizado
                    instancia = new RepositorioVenda();
                }
            }
        }
        return instancia;
    }

    @Override
    public Venda buscarVendaUsuario(long cpfUsuario) {
        Venda resultado = null;
        boolean encontrada = false;
        int i;
        for (i = 0; i < lista.size() && !encontrada; i++) {
            if (lista.get(i).getUsuario().getId() == cpfUsuario) {
                encontrada = true;
            }
        }

        if (encontrada) {
            resultado = lista.get(i-1);
        }

        return resultado;
    }
}
