package br.ufrpe.time_share.negocio;

import java.time.LocalDateTime;
import java.util.ArrayList;


import br.ufrpe.time_share.dados.RepositorioBens;
import br.ufrpe.time_share.dados.RepositorioHistorico;
import br.ufrpe.time_share.dados.RepositorioUsuarios;
import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Historico;
import br.ufrpe.time_share.negocio.beans.Usuario;

public class ControladorHistorico {
    private RepositorioHistorico repositorioHistorico;
    private RepositorioBens repositorioBens;

    public ControladorHistorico() {
        this.repositorioHistorico = new RepositorioHistorico();
        new ControladorUsuarioGeral(RepositorioUsuarios.getInstance());
        this.repositorioBens = RepositorioBens.getInstancia();
    }

    private void validarAcessoAoRelatorio(int idBem, Usuario usuario) {
        if (usuario.getCpf() == null) {
            throw new IllegalArgumentException("Usuário não pode ter cpf nulo");
        }

        Bem bem = repositorioBens.buscarBemPorId(idBem);

        if (bem == null) {
            throw new IllegalArgumentException("Bem não encontrado para o id: " + idBem);
        }
        // mudar lógica futuramente para criar um relatório de usuários comuns
        if (!usuario.equals(bem.getCadastradoPor())) {
            throw new SecurityException("Acesso neagdo! Apenas o usuário que cadastrou o bem pode acessar este relatório.");
        }
    }

    public ArrayList<Historico> listarHistoricoPorBem(int idBem, Usuario usuario) {
        validarAcessoAoRelatorio(idBem, usuario);

        ArrayList<Historico> historicos = repositorioHistorico.listarHistoricoPorBem(idBem);

        if (historicos.isEmpty()) {
            System.out.println("Não foram encontrados históricos para o bem com ID: " + idBem);
        } else {
            System.out.println("Históricos encontrados para o bem com ID: " + idBem);
            for (Historico historico : historicos) {
                System.out.println(historico);
            }
        }

        return historicos;
    }

    public void validarCadastroRelatorio(Historico historico, int idBem) {
        if (historico == null) {
            throw new IllegalArgumentException("O relatório não pde ser nulo.");
        }

        

        if (historico.getIdHistorico() == null) {
            throw new IllegalArgumentException("O id do relatório não pode ser nulo.");
        }

        if (historico.getIdBem() == null) {
            throw new IllegalArgumentException("O id do bem associado ao relatório não pode ser nulo.");
        }

      
                Bem bem = repositorioBens.buscarBemPorId(idBem);

        if (bem == null) {
            throw new IllegalArgumentException("Bem não encontrado para o ID: " + historico.getIdBem());
        }

        repositorioHistorico.cadastrarHistorico(historico);

    }
    
}
