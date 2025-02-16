package br.ufrpe.timeshare.dados;

import br.ufrpe.timeshare.negocio.beans.Bem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepositorioBens extends RepositorioGenerico<Bem> implements IRepositorioBens {

    // INSTANCIA UNICA DO REPOSITORIO
    private static RepositorioBens instancia;

    public RepositorioBens() {
        super();
    }

    // METODO PARA OBTER A INSTANCIA DO REPOSITORIO
    public static RepositorioBens getInstancia() {
        if (instancia == null) {
            synchronized (RepositorioBens.class) { // Thread-safe
                if (instancia == null) {
                    instancia = new RepositorioBens();
                }
            }
        }
        return instancia;
    }


    @Override
    public List<Bem> listarBensDisponiveis() {
        List<Bem> listaBensDisponiveis = new ArrayList<>();
        for (Bem bem : lista) {
            if (bem.isOfertado()) {
                listaBensDisponiveis.add(bem);
            }
        }
        Collections.sort(listaBensDisponiveis);
        return listaBensDisponiveis;
    }

    @Override
    public List<Bem> listarBensPorNome(String nome) {
        List<Bem> listaBensByNome = new ArrayList<>();
        String nomePesquisa = nome.trim().toLowerCase();
        for (Bem bem : lista) {
            String nomeBem = bem.getNome().trim().toLowerCase(); // Removendo espaços e padronizando
            if (nomeBem.contains(nomePesquisa)) { // Verificando se o nome contém a pesquisa
                listaBensByNome.add(bem);
            }

        }
        Collections.sort(listaBensByNome);
        return listaBensByNome;
    }

    @Override
    public List<Bem> listarBensPorLocalizacao(String localizacao) {
        List<Bem> listaBensPorLocalizacao = new ArrayList<>();
        String descricaoValor = localizacao.trim().toLowerCase();
        for (Bem bem : lista) {
            String localizacaoBem = bem.getLocalizacao().trim().toLowerCase(); // Removendo espaços e padronizando

            if (localizacaoBem.contains(descricaoValor)) { // Verificando se o nome contém a pesquisa
                listaBensPorLocalizacao.add(bem);
            }
        }
        Collections.sort(listaBensPorLocalizacao);
        return listaBensPorLocalizacao;
    }


    @Override
    public List<Bem> listarBensUsuarioPorNome(String nome, long idUsuario) {
        List<Bem> listaBensByNome = new ArrayList<>();
        String nomePesquisa = nome.trim().toLowerCase();
        for (Bem bem : lista) {
            if (bem.getCadastradoPor().getId() == idUsuario) {
                String nomeBem = bem.getNome().trim().toLowerCase(); // Removendo espaços e padronizando

                if (nomeBem.contains(nomePesquisa)) { // Verificando se o nome contém a pesquisa
                    listaBensByNome.add(bem);
                }
            }
        }
        Collections.sort(listaBensByNome);
        return listaBensByNome;
    }

}


