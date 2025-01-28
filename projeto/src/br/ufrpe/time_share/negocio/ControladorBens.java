package br.ufrpe.time_share.negocio;

import br.ufrpe.time_share.dados.IRepositorioBens;
import br.ufrpe.time_share.dados.IRepositorioCotas;
import br.ufrpe.time_share.dados.RepositorioUsuarios;
import br.ufrpe.time_share.excecoes.*;
import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class ControladorBens {
    private IRepositorioBens repositorioBens;
    private IRepositorioCotas repositorioCotas;
    private ControladorUsuarioGeral controladorUsuarioGeral;


    public ControladorBens(IRepositorioBens instanciaInterfaceBens, IRepositorioCotas instanciaInterfaceCota) {
        this.repositorioBens = instanciaInterfaceBens;
        this.repositorioCotas = instanciaInterfaceCota;
        this.controladorUsuarioGeral = new ControladorUsuarioGeral(RepositorioUsuarios.getInstancia());
    }


    // Cadastrar bem e inicializar as cotas dado a quantidade de cotas, a data inicial e quantidadeDeDiasPorCota

    public void cadastrar(int id, String nome, String descricao,
                          String localizacao, int capacidade, long cpfUsuario,
                          LocalDateTime diaInicial, int quantidadeDeCotas,
                          double precoDeUmaCota) throws BemNaoExisteException, UsuarioNaoPermitidoException, QuantidadeDeCotasExcedidasException, BemJaExisteException, UsuarioNaoExisteException {

        Usuario usuario = controladorUsuarioGeral.procurarUsuarioPorCpf(cpfUsuario);
        Bem newBem = new Bem(id, nome, descricao, localizacao, capacidade, usuario);

        if (usuario == null || newBem == null) {
            throw new NullPointerException();
        } else {
            if (usuario.getTipo().equals(TipoUsuario.ADMINISTRADOR)) {
                boolean jaExiste = repositorioBens.existe(newBem);

                if (jaExiste) {
                    throw new BemJaExisteException("Bem já existe");
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

    public ArrayList<Cota> calcularDeslocamentoDasCotas(long bemId, int anoParaDeslocamento) throws BemNaoExisteException {

        Bem bem = repositorioBens.buscar(bemId);

        if (bem == null) {
            throw new BemNaoExisteException("Bem não existe");
        }

        Bem bemClonado = bem.clone();
        ArrayList<Cota> resultado = new ArrayList<>();

        if (anoParaDeslocamento < bem.getDiaInicial().getYear()) {
            throw new IllegalArgumentException("Não permitido");
        } else {

            int delta = anoParaDeslocamento - bem.getDiaInicial().getYear();

            LocalDateTime dataInicio = bem.getDiaInicial();
            dataInicio = dataInicio.plusDays(7 * delta).plusYears(delta); // Deslocamento inicial baseado em delta
            LocalDateTime dataFinal = dataInicio.plusDays(6);

            for (Cota c : bemClonado.getCotas()) {
                c.setDataInicio(dataInicio);
                c.setDataFim(dataFinal);
                resultado.add(c);

                // Atualiza a nova data inicial para 7 dias depois do início atual
                dataInicio = dataInicio.plusDays(7);
                dataFinal = dataInicio.plusDays(6);
            }
        }

        return resultado;
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

    public List<Cota> listarCotasDeUmUsuario(long cpfUsuario) throws BemNaoExisteException, UsuarioNaoExisteException {
        Usuario usuario = controladorUsuarioGeral.procurarUsuarioPorCpf(cpfUsuario);
        if (usuario != null) {
            return repositorioCotas.buscarCotasPorProprietario(usuario);
        } else {
            throw new UsuarioNaoExisteException("Não encontramos o usuário");
        }

    }

    public List<Cota> registros(long usuarioCpf) throws UsuarioNaoExisteException {
        List<Cota> resultado = new ArrayList<>();
        Usuario usuario = controladorUsuarioGeral.procurarUsuarioPorCpf(usuarioCpf);

        for (Cota cota : repositorioCotas.listar()) {
            if (cota.getProprietario().equals(usuario)) {
                resultado.add(cota);
            }
        }
        return resultado;
    }

//    public void iniciarDeslocamentoAutomatico() {
//        Timer timer = new Timer(true);
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                deslocarCotasAutomaticamente();
//            }
//        };
//
//        // Agenda a tarefa para rodar a cada 24 horas
//        long umDiaEmMilissegundos = 24 * 60 * 60 * 1000; // 24 * 60 * 60 * 1000
//        timer.scheduleAtFixedRate(task, 0, umDiaEmMilissegundos);
//    }

    // TODO ajustar o deslocamento das cotas
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
}





