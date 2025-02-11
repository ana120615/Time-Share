package br.ufrpe.timeshare.negocio;

import br.ufrpe.timeshare.dados.IRepositorioBens;
import br.ufrpe.timeshare.dados.IRepositorioCotas;
import br.ufrpe.timeshare.dados.RepositorioBens;
import br.ufrpe.timeshare.dados.RepositorioCotas;
import br.ufrpe.timeshare.excecoes.*;
import br.ufrpe.timeshare.negocio.beans.Bem;
import br.ufrpe.timeshare.negocio.beans.Cota;
import br.ufrpe.timeshare.negocio.beans.TipoUsuario;
import br.ufrpe.timeshare.negocio.beans.Usuario;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ControladorBens {
    private IRepositorioBens repositorioBens;
    private IRepositorioCotas repositorioCotas;


    public ControladorBens() {
        this.repositorioBens = RepositorioBens.getInstancia();
        this.repositorioCotas = RepositorioCotas.getInstancia();
    }


    // Cadastrar bem e inicializar as cotas dado a quantidade de cotas, a data inicial e quantidadeDeDiasPorCota

    public void cadastrar(int id, String nome, String descricao,
                          String localizacao, int capacidade, Usuario usuario,
                          LocalDateTime diaInicial, int quantidadeDeCotas,
                          double precoDeUmaCota, String caminhoImagem) throws BemNaoExisteException, UsuarioNaoPermitidoException, QuantidadeDeCotasExcedidasException, BemJaExisteException, UsuarioNaoExisteException {
        Bem newBem = new Bem(id, nome, descricao, localizacao, capacidade, usuario, caminhoImagem);
        if (usuario == null || newBem == null) {
            throw new NullPointerException();
        } else {
            if (usuario.getTipo().equals(TipoUsuario.ADMINISTRADOR)) {
                boolean jaExiste = repositorioBens.existe(newBem);
                if (jaExiste) {
                    throw new BemJaExisteException("Bem já existe");
                }
                if (repositorioBens.buscar(id) != null) {
                    throw new BemJaExisteException("Id já utilizado");
                }
                newBem.setDiaInicial(diaInicial);
                // Criação das cotas
                if (quantidadeDeCotas > 52) {
                    throw new QuantidadeDeCotasExcedidasException("Valor não permitido de cotas.");
                } else {
                    ArrayList<Cota> cotas = new ArrayList<>();
                    Random random = new Random();
                    LocalDateTime dataInicio = diaInicial;
                    LocalDateTime dataFim = dataInicio.plusDays(6).toLocalDate().atTime(LocalTime.MAX);
                    for (int i = 0; i < quantidadeDeCotas; ) {
                        int randomNumberCota = 1000 + random.nextInt(9999);
                        Cota c = repositorioCotas.buscar(randomNumberCota);
                        if (!repositorioCotas.existe(c)) {
                            cotas.add(i, new Cota(randomNumberCota, dataInicio, dataFim, precoDeUmaCota, newBem));
                            i++;
                        }
                        dataInicio = dataFim.plusDays(1);
                        dataFim = dataInicio.plusDays(6);
                    }
                    newBem.setCotas(cotas);
                    repositorioCotas.cadastrarCotas(cotas);
                    repositorioBens.cadastrar(newBem);
                }
            } else {
                throw new UsuarioNaoPermitidoException("Usuário não permitido");
            }
        }
    }


    public void remover(int id) throws BemNaoExisteException, IllegalAccessException {
        Bem removido = repositorioBens.buscar(id);
        if (removido != null && !removido.isOfertado()) {
            for (Cota c : removido.getCotas()) {
                repositorioCotas.remover(c);
            }
            repositorioBens.remover(removido);
        } else if (removido == null) {
            throw new BemNaoExisteException("Bem não existe");
        } else {
            throw new IllegalAccessException("Bem ja foi ofertado");
        }
    }

    public void ofertarBem(int id) throws BemNaoExisteException {
        Bem bem = repositorioBens.buscar(id);
        if (bem != null) {
            bem.setOfertado(true);
            for (Cota c : bem.getCotas()) {
                c.setStatusDeDisponibilidadeParaCompra(bem.isOfertado());
            }
        } else {
            throw new BemNaoExisteException("Bem não existe");
        }
    }

    public List<Bem> listarBens() {
        return repositorioBens.listar();
    }

    public List<Bem> listarBensUsuario(Usuario usuario) {
        List<Bem> resultado = new ArrayList<>();
        if (repositorioBens != null && usuario != null) {
            for (Bem bem : repositorioBens.listar()) {
                if (bem.getCadastradoPor().equals(usuario)) {
                    resultado.add(bem);
                }
            }
        }
        return resultado;
    }


    public List<Bem> listarBensOfertadosUsuario(Usuario usuario) {
        List<Bem> resultado = new ArrayList<>();
        if (repositorioBens != null && usuario != null) {
            for (Bem bem : repositorioBens.listar()) {
                if (bem.getCadastradoPor().equals(usuario) && bem.isOfertado()) {
                    resultado.add(bem);
                }
            }
        }
        return resultado;
    }

    public List<Bem> listarBensOfertados() {
        return repositorioBens.listarBensDisponiveis();
    }


    public void listarCotasDisponiveis() {
        List<Cota> cotas = repositorioCotas.listarCotasDisponiveisParaVenda();
        for (Cota cota : cotas) {
            System.out.println(cota + "\n");
        }
    }


    public ArrayList<Cota> calcularDeslocamentoDasCotas(long bemId, LocalDateTime dataParaDeslocamento) throws BemNaoExisteException {
        Bem bem = repositorioBens.buscar(bemId);
        if (bem == null) {
            throw new BemNaoExisteException("Bem não existe");
        }
        Bem bemClonado = bem.clone();
        List<Cota> cotasClonadas = new ArrayList<>();
        for (Cota cota : bemClonado.getCotas()) {
            cotasClonadas.add(cota.clone());
        }
        for (Cota cota : cotasClonadas) {
            LocalDateTime dataInicialCota = cota.getDataInicio();
            LocalDateTime dataFinalCota = cota.getDataFim();
            // Verifica se passou 1 ano da data inicial de uma única Cota
            if (dataParaDeslocamento.isAfter(dataFinalCota)) {
                // Bem remove a cota
                bemClonado.getCotas().remove(cota);
                cota.setDataInicio(dataInicialCota.plusYears(1).plusDays(7));
                cota.setDataFim(cota.getDataInicio().plusDays(6));
                // Joga para o final da lista de cotas do bem
                bemClonado.getCotas().add(cota);// Atualiza a data final
            }
        }
        Collections.sort(bemClonado.getCotas());
        return bemClonado.getCotas();
    }


    public Cota buscarCota(int idCota) throws CotaNaoExisteException {
        Cota cota = repositorioCotas.buscar(idCota);
        if (cota == null) {
            throw new CotaNaoExisteException("Cota não existe");
        }
        return cota;
    }


    public void listarCotasDeUmBem(int idBem) throws BemNaoExisteException {
        ArrayList<Cota> resultado = new ArrayList<>();
        Bem bem = repositorioBens.buscar(idBem);
        if (bem == null) {
            throw new BemNaoExisteException("Bem não existe");
        }
        for (Cota cota : bem.getCotas()) {
            System.out.println(cota);
        }
    }


    public List<Cota> listarCotasDeUmUsuario(Usuario usuario) throws BemNaoExisteException, UsuarioNaoExisteException {
        if (usuario != null) {
            return repositorioCotas.buscarCotasPorProprietario(usuario);
        } else {
            throw new UsuarioNaoExisteException("Não encontramos o usuário");
        }
    }


    public List<Cota> registros(Usuario usuario) throws UsuarioNaoExisteException {
        List<Cota> resultado = new ArrayList<>();
        for (Cota cota : repositorioCotas.listar()) {
            if (cota.getProprietario().equals(usuario)) {
                resultado.add(cota);
            }
        }
        return resultado;
    }


    public void deslocarCotasAutomaticamente() {
        List<Cota> cotas = repositorioCotas.listar();
        for (Cota cota : cotas) {
            LocalDateTime agora = LocalDateTime.now();
            LocalDateTime dataInicialCota = cota.getDataInicio();
            LocalDateTime dataFinalCota = cota.getDataFim();
            // Verifica se passou 1 ano da data inicial de uma única Cota
            if (agora.isAfter(dataFinalCota)) {
                Bem bemDaCota = cota.getBemAssociado();
                // Bem remove a cota
                bemDaCota.getCotas().remove(cota);
                cota.setDataInicio(dataInicialCota.plusYears(1).plusDays(7));
                cota.setDataFim(cota.getDataInicio().plusDays(6));
                // Joga para o final da lista de cotas do bem
                bemDaCota.getCotas().add(cota);// Atualiza a data final
            }
        }
    }


    public void alterarNomeBem(int id, String novoNome) throws BemNaoExisteException, NullPointerException {
        if (novoNome == null) {
            throw new NullPointerException("Nome nulo.");
        }
        Bem bem = this.repositorioBens.buscar(id);
        if (bem != null) {
            bem.setNome(novoNome);
        } else {
            throw new BemNaoExisteException("Bem não existe.");
        }
    }

    public void alterarDescricaoBem(int id, String novaDescricao) throws BemNaoExisteException, NullPointerException {
        if (novaDescricao == null) {
            throw new NullPointerException("Descricao nula.");
        }
        Bem bem = this.repositorioBens.buscar(id);
        if (bem != null) {
            bem.setDescricao(novaDescricao);
        } else {
            throw new BemNaoExisteException("Bem não existe.");
        }
    }


    public void alterarLocalizacaoBem(int id, String novaLocalizacao) throws BemNaoExisteException, NullPointerException {
        if (novaLocalizacao == null) {
            throw new NullPointerException("Localizacao nula.");
        }
        Bem bem = this.repositorioBens.buscar(id);
        if (bem != null) {
            bem.setLocalizacao(novaLocalizacao);
        } else {
            throw new BemNaoExisteException("Bem não existe.");
        }
    }


    public void alterarCapacidadeBem(int id, int novaCapacidade) throws BemNaoExisteException, IllegalArgumentException {
        if (novaCapacidade == 0) {
            throw new IllegalArgumentException("Capacidade invalida.");
        }
        Bem bem = this.repositorioBens.buscar(id);
        if (bem != null) {
            bem.setCapacidade(novaCapacidade);
        } else {
            throw new BemNaoExisteException("Bem não existe.");
        }
    }


    public Bem buscarBemPorId(int idBem) throws BemNaoExisteException {
        Bem bem = repositorioBens.buscar(idBem);
        if (bem == null) {
            throw new BemNaoExisteException("Bem não existe");
        }
        return bem;
    }


    public List<Cota> listarCotasDeUmUsuarioEmUmBem(Usuario usuario, int idBemParaReserva) {
        List<Cota> resultado = new ArrayList<>();
        Bem bem = repositorioBens.buscar(idBemParaReserva);
        for (Cota c : repositorioCotas.buscarCotasPorProprietario(usuario)) {
            if (c.getBemAssociado().equals(bem)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    //RELATORIOS

    //FRACOES MAIS COMPRADAS
}





