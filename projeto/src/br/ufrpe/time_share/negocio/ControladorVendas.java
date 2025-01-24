package br.ufrpe.time_share.negocio;

import br.ufrpe.time_share.dados.RepositorioBens;
import br.ufrpe.time_share.dados.RepositorioCotas;
import br.ufrpe.time_share.dados.RepositorioUsuarios;
import br.ufrpe.time_share.excecoes.*;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Promocao;
import br.ufrpe.time_share.negocio.beans.Usuario;
import br.ufrpe.time_share.negocio.beans.Venda;

import java.time.LocalDateTime;
import java.util.Random;

public class ControladorVendas {
    ControladorBens controladorBens;
    ControladorUsuarioGeral controladorUsuarioGeral;

    {
        this.controladorBens = new ControladorBens(RepositorioBens.getInstancia(), RepositorioCotas.getInstancia());
        this.controladorUsuarioGeral = new ControladorUsuarioGeral(RepositorioUsuarios.getInstance());
    }

    public Venda iniciarVenda(String cpfUsuario) throws UsuarioNaoExisteException {
        Usuario comprador = controladorUsuarioGeral.procurarUsuarioPorCpf(cpfUsuario);
        Random random = new Random();
        if (comprador != null) {
            int randomId = 99999 + random.nextInt(1000000);
            Venda venda = new Venda(randomId);
            venda.setUsuario(comprador);

            return venda;
        } else {
            throw new UsuarioNaoExisteException("Usuário não existe");
        }
    }

    public void adicionarCotaCarrinho(int idCota, Venda venda) throws CotaNaoExisteException, CotaNaoOfertadaException {
        Cota cotaVenda = controladorBens.buscarCota(idCota);

        if (cotaVenda.isStatusDeDisponibilidadeParaCompra()) {
            venda.adicionarCotaCarrinho(cotaVenda);
        } else {
            throw new CotaNaoOfertadaException("Não está ofertada");
        }

    }

    public void removeCotaCarrinho(int idCota, Venda venda) throws CotaNaoExisteException {
        Cota cota = controladorBens.buscarCota(idCota);

        if (venda.getCarrinhoDeComprasCotas().contains(cota)) {
            venda.removerCotaCarrinho(cota);
        } else {
            throw new IllegalArgumentException("Não existe essa cota no carrinho");
        }
    }

    public String finalizarCompra(Venda venda) {
        if (!venda.getCarrinhoDeComprasCotas().isEmpty()) {
            venda.finalizarCompra();
            // Poderia retornar algum Arquivo (Comprovante) de compra
            return venda.toString();
        } else {
            throw new CompraNaoFinalizada("Carrinho vazio");
        }
    }

    public boolean transferenciaDeDireitos(String cpfUsuarioRemetente, String cpfUsuarioDestinario, int idCota) throws CotaNaoExisteException, UsuarioNaoExisteException {
        Cota cotaTransferida = controladorBens.buscarCota(idCota);

        Usuario usuarioRemetente = controladorUsuarioGeral.procurarUsuarioPorCpf(cpfUsuarioRemetente);
        Usuario usuarioDestinatario = controladorUsuarioGeral.procurarUsuarioPorCpf(cpfUsuarioDestinario);

        if (cotaTransferida.getProprietario().equals(usuarioRemetente)) {
            cotaTransferida.setProprietario(usuarioDestinatario);
            return true;
        } else {
            throw new ProprietarioNaoIdentificadoException("Usuário não é o Proprietário da cota");
        }
    }

    public void verificarSeUsuarioPossuiDescontos (String cpfUsuario) throws UsuarioNaoExisteException{
        Usuario usuario = controladorUsuarioGeral.procurarUsuarioPorCpf(cpfUsuario);

        Promocao promocao = new Promocao();

        if (usuario.verificarAniversario()) {
            System.out.println("Desconto aniversário de" + promocao.getTaxaPromocaoAniversario()*100+"%");
        }

        if (promocao.eAltaTemporada(LocalDateTime.now())) {
            System.out.println("Desconto temporada de" + promocao.getTaxaPromocaoTemporada(LocalDateTime.now())*100+"%");
        }
    }

    public boolean aplicarDesconto (Venda venda, String cpfUsuario) throws UsuarioNaoExisteException {
        boolean resultado = false;

        if (venda != null && !venda.getFoiDescontoAplicado()) {
            venda.setFoiDescontoAplicado(true);
            resultado = true;
        }

        return resultado;
    }


}
