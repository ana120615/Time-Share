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
                    LocalDateTime dataFim = dataInicio.plusDays(6).toLocalDate().atTime(23, 59, 59);
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

    public void ofertarBem(int id) throws BemJaOfertadoException, BemNaoExisteException {
        Bem bem = repositorioBens.buscar(id);
        if (bem.isOfertado()) {
            throw new BemJaOfertadoException("Bem já ofertado");
        }
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


    public List<Bem> listarBensUsuario(Usuario usuario) throws DadosInsuficientesException {
        if (usuario == null) {
            throw new DadosInsuficientesException("Usuario nulo");
        }
        List<Bem> resultado = new ArrayList<>();
        if (repositorioBens != null && usuario != null) {
            for (Bem bem : repositorioBens.listar()) {
                if (bem.getCadastradoPor().equals(usuario)) {
                    resultado.add(bem);
                }
            }
        }
        Collections.sort(resultado);
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
        Collections.sort(resultado);
        return resultado;
    }

    public List<Bem> listarBensOfertados() {
        return repositorioBens.listarBensDisponiveis();
    }

    public List<Bem> listarBensPorNome(String nome) throws DadosInsuficientesException {
        if (nome == null) {
            throw new DadosInsuficientesException("Nome nulo");
        }
        return repositorioBens.listarBensDisponiveisPorNome(nome);
    }


    public List<Bem> listarBensUsuarioPorNome(String nome, long idusuario) throws DadosInsuficientesException {
        if (nome == null) {
            throw new DadosInsuficientesException("Nome nulo");
        }
        return repositorioBens.listarBensUsuarioPorNome(nome, idusuario);
    }

    public List<Bem> listarBensUsuarioPorLocalizacao(String localizacao) throws DadosInsuficientesException {
        if (localizacao == null) {
            throw new DadosInsuficientesException("Localizacao nula");
        }
        return repositorioBens.listarBensPorLocalizacao(localizacao);
    }


    public ArrayList<Cota> calcularDeslocamentoDasCotas(long bemId, LocalDateTime dataParaDeslocamento) throws BemNaoExisteException {
        Bem bem = repositorioBens.buscar(bemId);
        if (bem == null) {
            throw new BemNaoExisteException("Bem não existe");
        }

        Bem bemClonado = bem.clone();
        List<Cota> cotasGeradas = new ArrayList<>();
        int anoAtual = LocalDateTime.now().getYear();

        for (Cota cotaOriginal : bemClonado.getCotas()) {
            LocalDateTime dataInicio = cotaOriginal.getDataInicio();
            LocalDateTime dataFim = cotaOriginal.getDataFim();

            if (dataInicio.getYear() == anoAtual) {
                dataInicio = dataInicio.plusYears(1).plusDays(7);
                dataFim = dataInicio.plusDays(6);
            }

            while (!dataInicio.isAfter(dataParaDeslocamento)) {
                Cota novaCota = cotaOriginal.clone();
                novaCota.setDataInicio(dataInicio);
                novaCota.setDataFim(dataFim);
                cotasGeradas.add(novaCota);

                dataInicio = dataInicio.plusYears(1).plusDays(7);
                dataFim = dataInicio.plusDays(6);
            }
        }

        Collections.sort(cotasGeradas);
        return new ArrayList<>(cotasGeradas);
    }

    public ArrayList<Cota> calcularDeslocamentoDeCota(Cota cota, LocalDateTime dataParaDeslocamento) {
        List<Cota> cotasGeradas = new ArrayList<>();
        int anoAtual = LocalDateTime.now().getYear(); // Obtém o ano atual

        LocalDateTime dataInicio = cota.getDataInicio();
        LocalDateTime dataFim = cota.getDataFim();

        // Se a cota estiver no ano atual, ajusta para começar no próximo ano
        if (dataInicio.getYear() == anoAtual) {
            dataInicio = dataInicio.plusYears(1).plusDays(7);
            dataFim = dataInicio.plusDays(6);
        }

        // Gera deslocamentos até a data desejada
        while (!dataInicio.isAfter(dataParaDeslocamento)) {
            Cota novaCota = cota.clone();
            novaCota.setDataInicio(dataInicio);
            novaCota.setDataFim(dataFim);
            cotasGeradas.add(novaCota);

            // Avança para o próximo deslocamento (1 ano e 7 dias)
            dataInicio = dataInicio.plusYears(1).plusDays(7);
            dataFim = dataInicio.plusDays(6);
        }

        Collections.sort(cotasGeradas);
        return new ArrayList<>(cotasGeradas);
    }

    public Cota buscarCota(long idCota) throws CotaNaoExisteException {
        Cota cota = repositorioCotas.buscar(idCota);
        if (cota == null) {
            throw new CotaNaoExisteException("Cota não existe");
        }
        return cota;
    }


    public List<Cota> listarCotasDeUmBem(int idBem) throws BemNaoExisteException {
        ArrayList<Cota> resultado = new ArrayList<>();
        Bem bem = repositorioBens.buscar(idBem);
        if (bem == null) {
            throw new BemNaoExisteException("Bem não existe");
        }
        for (Cota cota : bem.getCotas()) {
            resultado.add(cota);
        }

        return resultado;
    }


    public List<Cota> listarCotasDeUmUsuario(Usuario usuario) throws BemNaoExisteException, UsuarioNaoExisteException {
        if (usuario != null) {
            return repositorioCotas.buscarCotasPorProprietario(usuario);
        } else {
            throw new UsuarioNaoExisteException("Não encontramos o usuário");
        }
    }

    public List<Cota> listarCotasDeUmUsuarioPorNomeBem(Usuario usuario, String nome) throws UsuarioNaoPermitidoException {
        if (usuario != null) {
            return repositorioCotas.buscarCotasPorProprietarioPorNomeBem(usuario, nome);
        } else {
            throw new UsuarioNaoPermitidoException("Esse usuario nao existe");
        }
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

    public void alterarCaminhoDaImagemBem(int id, String novoCaminhoDeImagem) throws BemNaoExisteException, IllegalArgumentException {
        Bem bem = this.repositorioBens.buscar(id);
        if (bem != null) {
            bem.setCaminhoImagem(novoCaminhoDeImagem);
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

    public List<Bem> listarBensPorDescricao(String descricao) throws DadosInsuficientesException {
        if (descricao == null) {
            throw new DadosInsuficientesException("Descricao nula");
        }
        return repositorioBens.listarBensPorDescricao(descricao);
    }

    //busca de bem por nome, localizacao ou id
    public List<Bem> buscarBensPorAtributo(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            return repositorioBens.listar();
        }

        try {
            int id = Integer.parseInt(filtro);
            Bem bem;
            try {
                bem = buscarBemPorId(id);
                if (bem != null) {
                    return List.of(bem);
                }
            } catch (BemNaoExisteException e) {
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
        }

        List<Bem> bensEncontrados = new ArrayList<>();
        try {
            bensEncontrados.addAll(listarBensPorNome(filtro));
        } catch (DadosInsuficientesException e) {
            e.printStackTrace();
        }
        try {
            bensEncontrados.addAll(listarBensUsuarioPorLocalizacao(filtro));
        } catch (DadosInsuficientesException e) {
            e.printStackTrace();
        }
        try {
            bensEncontrados.addAll(listarBensPorDescricao(filtro));
        } catch (DadosInsuficientesException e) {
            e.printStackTrace();
        }

        // Remover duplicatas
        List<Bem> resultadoFinal = new ArrayList<>();
        for (Bem bem : bensEncontrados) {
            if (!resultadoFinal.contains(bem)) {
                resultadoFinal.add(bem);
            }
        }

        return resultadoFinal;
    }

    public long buscarBemIdPorNome (String bemSelecionado) {
        int resultdado;
        for (Bem bem : repositorioBens.listar()) {
            if (bem.getNome().equals(bemSelecionado)) {
                return bem.getId();
            }
        }
        return -1;
    }

    // RELATÓRIO
    public int quantidadeVendasBem(Bem bem) {
        int resultado = 0;
        for (Cota c : bem.getCotas()) {
            if (c.getProprietario() != null && !c.getStatusDeDisponibilidadeParaCompra()) {
                resultado++;
            }
        }
        return resultado;
    }

}





