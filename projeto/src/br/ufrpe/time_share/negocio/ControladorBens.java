package br.ufrpe.time_share.negocio;

import br.ufrpe.time_share.dados.IRepositorioBens;
import br.ufrpe.time_share.excecoes.BemJaExisteException;
import br.ufrpe.time_share.excecoes.BemNaoExisteException;
import br.ufrpe.time_share.excecoes.UsuarioNaoPermitidoException;
import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;


public class ControladorBens {
    private IRepositorioBens repositorio;

    public ControladorBens(IRepositorioBens instanciaIneterface) {
        this.repositorio = instanciaIneterface;
    }

    public void cadastrar(Usuario usuario, Bem bem) throws BemJaExisteException, UsuarioNaoPermitidoException {
        if (usuario == null || bem == null) {
            throw new NullPointerException();
        } else {
            if (usuario.getTipo().equals(TipoUsuario.ADMINISTRADOR)) {
                boolean jaExiste = repositorio.existe(bem);

                if (jaExiste) {
                    throw new BemJaExisteException("Bem já existe");
                }

                repositorio.cadastrarBem(bem);
            } else {
                throw new UsuarioNaoPermitidoException("Usuário não permitido");
            }

        }

    }

    // Cadastrar bem e inicializar as cotas dado a quantidade de cotas, a data inicial
    // e quantidadeDeDiasPorCota
    public void cadastrar(int id, String nome, String descricao,
                          String localizacao, int capacidade, Usuario usuario,
                          LocalDate diaInicial, int quantidadeDeCotas,
                          double precoDeUmaCota) throws BemNaoExisteException, UsuarioNaoPermitidoException {

        Bem newBem = new Bem(id, nome, descricao, localizacao, capacidade, usuario);

        if (usuario == null || newBem == null) {
            throw new NullPointerException();
        } else {
            if (usuario.getTipo().equals(TipoUsuario.ADMINISTRADOR)) {
                boolean jaExiste = repositorio.existe(newBem);

                if (jaExiste) {
                    throw new BemJaExisteException("Bem já existe");
                }

                newBem.setDiaInicial(diaInicial);
                // Criação das cotas

                ArrayList<Cota> cotas = new ArrayList<>();
                Random random = new Random();
                LocalDate dataInicio = diaInicial;
                LocalDate dataFim = dataInicio.plusDays(6);

                for (int i = 0; i < quantidadeDeCotas; i++) {
                    int randomNumberCota = 1000 + random.nextInt(9999);
                    cotas.add(i, new Cota(randomNumberCota, dataInicio, dataFim, precoDeUmaCota, newBem));

                    dataInicio = dataFim.plusDays(1);
                    dataFim = dataInicio.plusDays(6);

                }

                newBem.setCotas(cotas);
                repositorio.cadastrarBem(newBem);


            } else {
                throw new UsuarioNaoPermitidoException("Usuário não permitido");
            }

        }

    }


    public void remover(int id) throws BemNaoExisteException {
        Bem removido = repositorio.buscarBemPorId(id);

        if (removido != null) {
            repositorio.removerBem(removido);
        } else {
            throw new BemNaoExisteException("Bem não existe");
        }

    }

    public void ofertarBem(int id) throws BemNaoExisteException {
        Bem bem = repositorio.buscarBemPorId(id);

        if (bem != null) {
            bem.setOfertado(true);
        } else {
            throw new BemNaoExisteException("Bem não existe");
        }
    }

    public ArrayList<Bem> listarBens() {
        return repositorio.listarBens();
    }

    public ArrayList<Bem> listarBensDisponiveis() {
        return repositorio.listarBensDisponiveis();
    }

    public ArrayList<Cota> calcularDeslocamentoDasCotas(int bemId, int anoParaDeslocamento) {

        Bem bem = repositorio.buscarBemPorId(bemId);

        ArrayList<Cota> resultado = new ArrayList<>();

        if (anoParaDeslocamento < bem.getDiaInicial().getYear()) {
            throw new IllegalArgumentException("Não permitido");
        } else {

            int delta = bem.getDiaInicial().getYear() - anoParaDeslocamento;
            int diasDeslocados = delta * 7;

            LocalDate dataInicio = bem.getDiaInicial().plusDays(6);
            LocalDate dataFinal = dataInicio.plusDays(6);

            for (Cota c : bem.getCotas()) {
                c.setDataInicio(dataInicio);
                c.setDataFim(dataFinal);
                resultado.add(c);

                dataInicio = dataFinal.plusDays(1);
                dataFinal = dataInicio.plusDays(6);
            }
        }

        return resultado;
    }


}



