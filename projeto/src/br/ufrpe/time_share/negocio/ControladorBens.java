package br.ufrpe.time_share.negocio;

import br.ufrpe.time_share.dados.IRepositorioBens;
import br.ufrpe.time_share.dados.IRepositorioCotas;
import br.ufrpe.time_share.dados.RepositorioCotas;
import br.ufrpe.time_share.dados.RepositorioUsuarios;
import br.ufrpe.time_share.excecoes.*;
import br.ufrpe.time_share.negocio.beans.Bem;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ControladorBens {
    private IRepositorioBens repositorioBens;
    private IRepositorioCotas repositorioCotas;
    private ControladorUsuarioGeral controladorUsuarioGeral;


    public ControladorBens(IRepositorioBens instanciaIneterface) {
        this.repositorioBens = instanciaIneterface;
        this.repositorioCotas = RepositorioCotas.getInstancia();
        this.controladorUsuarioGeral = new ControladorUsuarioGeral(RepositorioUsuarios.getInstance());
    }


    // Cadastrar bem e inicializar as cotas dado a quantidade de cotas, a data inicial e quantidadeDeDiasPorCota

    public void cadastrar(int id, String nome, String descricao,
                          String localizacao, int capacidade, String cpfUsuario,
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
                    LocalDateTime dataFim = dataInicio.plusDays(6);

                    for (int i = 0; i < quantidadeDeCotas; ) {
                        int randomNumberCota = 1000 + random.nextInt(9999);
                        Cota c = repositorioCotas.buscarCotaPorId(randomNumberCota);

                        if (!repositorioCotas.existeCota(c)) {
                            cotas.add(i, new Cota(randomNumberCota, dataInicio, dataFim, precoDeUmaCota, newBem));
                            i++;
                        }


                        dataInicio = dataFim.plusDays(1);
                        dataFim = dataInicio.plusDays(6);
                    }
                    newBem.setCotas(cotas);
                    repositorioCotas.cadastrarCotas(cotas);
                    repositorioBens.cadastrarBem(newBem);
                }


            } else {
                throw new UsuarioNaoPermitidoException("Usuário não permitido");
            }

        }

    }


    public void remover(int id) throws BemNaoExisteException {
        Bem removido = repositorioBens.buscarBemPorId(id);

        if (removido != null) {
            repositorioBens.removerBem(removido);
        } else {
            throw new BemNaoExisteException("Bem não existe");
        }

    }

    public void ofertarBem(int id) throws BemNaoExisteException {
        Bem bem = repositorioBens.buscarBemPorId(id);

        if (bem != null) {
            bem.setOfertado(true);
            for (Cota c : bem.getCotas()) {
                c.setStatusDeDisponibilidadeParaCompra(bem.isOfertado());
            }
        } else {
            throw new BemNaoExisteException("Bem não existe");
        }
    }

    public ArrayList<Bem> listarBens() {
        return repositorioBens.listarBens();
    }

    public ArrayList<Bem> listarBensUsuario (Usuario usuario) {
        ArrayList<Bem> resultado = new ArrayList<>();

        if (repositorioBens != null && usuario != null) {
            for (Bem bem : repositorioBens.listarBens()) {
                if (bem.getCadastradoPor().equals(usuario)) {
                    resultado.add(bem);
                }
            }
        }
        return resultado;
    }

    public ArrayList<Bem> listarBensOfertadosUsuario (Usuario usuario) {
        ArrayList<Bem> resultado = new ArrayList<>();

        if (repositorioBens != null && usuario != null) {
            for (Bem bem : repositorioBens.listarBens()) {
                if (bem.getCadastradoPor().equals(usuario) && bem.isOfertado()) {
                    resultado.add(bem);
                }
            }
        }

        return resultado;
    }

    public ArrayList<Bem> listarBensDisponiveis() {
        return repositorioBens.listarBensDisponiveis();
    }

    public void listarCotasDisponiveis() {
        ArrayList<Cota> cotas =  repositorioCotas.listarCotasDisponiveisParaVenda();

        for (Cota cota : cotas) {
            System.out.println(cota + "\n");
        }
    }

    public ArrayList<Cota> calcularDeslocamentoDasCotas(int bemId, int anoParaDeslocamento) throws BemNaoExisteException {

        Bem bem = repositorioBens.buscarBemPorId(bemId);

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
        Cota cota = repositorioCotas.buscarCotaPorId(idCota);

        if (cota == null) {
            throw new CotaNaoExisteException("Cota não existe");
        }

        return cota;
    }

    public void listarCotasDeUmBem(int idBem) throws BemNaoExisteException {
        ArrayList<Cota> resultado = new ArrayList<>();
        Bem bem = repositorioBens.buscarBemPorId(idBem);

        if (bem == null) {
            throw new BemNaoExisteException("Bem não existe");
        }

        for (Cota cota : bem.getCotas()) {
            System.out.println(cota);
        }

    }

    public void listarCotasDeUmUsuario(String cpfUsuario) throws BemNaoExisteException, UsuarioNaoExisteException {
        ArrayList<Cota> resultado = new ArrayList<>();
        Usuario usuario = controladorUsuarioGeral.procurarUsuarioPorCpf(cpfUsuario);

        ArrayList<Cota> cotas = repositorioCotas.buscarCotasPorProprietario(usuario);

        for (Cota cota : cotas) {
            System.out.println(cota + "\n");
        }

    }

    public ArrayList<Cota> registros(String usuarioCpf) throws UsuarioNaoExisteException {
        ArrayList<Cota> resultado = new ArrayList<>();
        Usuario usuario = controladorUsuarioGeral.procurarUsuarioPorCpf(usuarioCpf);

        for (Cota cota : repositorioCotas.listarCotas()) {
            if (cota.getProprietario().equals(usuario)) {
                resultado.add(cota);
            }
        }
        return resultado;
    }

    public void iniciarDeslocamentoAutomatico() {
        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                deslocarCotasAutomaticamente();
            }
        };

        // Agenda a tarefa para rodar a cada 24 horas
        long umDiaEmMilissegundos = 24 * 60 * 60 * 1000; // 24 * 60 * 60 * 1000
        timer.scheduleAtFixedRate(task, 0, umDiaEmMilissegundos);
    }

    public void deslocarCotasAutomaticamente()  {
        ArrayList<Bem> bens = repositorioBens.listarBens();

        for (Bem bem : bens) {
            LocalDateTime agora = LocalDateTime.now();
            LocalDateTime dataInicialBem = bem.getDiaInicial();

            // Verifica se passou 1 ano da data inicial do bem
            if (agora.isAfter(dataInicialBem.plusYears(1))) {
                try {
                    bem.setCotas(calcularDeslocamentoDasCotas(bem.getId(), agora.getYear()));
                } catch (BemNaoExisteException e) {
                    System.out.println(e.getMessage());
                }


            }

        }
    }
}





