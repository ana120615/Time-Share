package br.ufrpe.time_share.negocio;

import br.ufrpe.time_share.dados.IRepositorioBens;
import br.ufrpe.time_share.dados.IRepositorioCotas;
import br.ufrpe.time_share.dados.RepositorioBens;
import br.ufrpe.time_share.dados.RepositorioUsuarios;
import br.ufrpe.time_share.excecoes.CompraNaoFinalizada;
import br.ufrpe.time_share.excecoes.CotaNaoExisteException;
import br.ufrpe.time_share.excecoes.CotaNaoOfertadaException;
import br.ufrpe.time_share.excecoes.UsuarioNaoExisteException;
import br.ufrpe.time_share.negocio.beans.Cota;
import br.ufrpe.time_share.negocio.beans.Usuario;
import br.ufrpe.time_share.negocio.beans.Venda;

import java.util.Random;

public class ControladorVendas {
    ControladorBens controladorBens;
    ControladorUsuarioGeral controladorUsuarioGeral;

    {
        this.controladorBens = new ControladorBens(RepositorioBens.getInstancia());
        this.controladorUsuarioGeral = new ControladorUsuarioGeral(RepositorioUsuarios.getInstance());
    }

    public Venda iniciarVenda (String cpfUsuario) throws UsuarioNaoExisteException{
        Usuario comprador = controladorUsuarioGeral.procurarUsuarioPorCpf(cpfUsuario);
        Random random = new Random();
        if (comprador != null) {
            int randomId = 99999 + random.nextInt(1000000);
            Venda venda = new Venda(randomId);
            venda.setUsuario(comprador);

            return venda;
        }
        else {
            throw new UsuarioNaoExisteException("Usuário não existe");
        }
    }

    public void adicionarCotaCarrinho (int idCota, Venda venda) throws CotaNaoExisteException, CotaNaoOfertadaException{
        Cota cotaVenda = controladorBens.buscarCota(idCota);

        if (cotaVenda.isStatusDeDisponibilidadeParaCompra()) {
            venda.adicionarCotaCarrinho(cotaVenda);
        } else {
            throw new CotaNaoOfertadaException("Não está ofertada");
        }

    }

    public void removeCotaCarrinho (int idCota, Venda venda) throws CotaNaoExisteException {
        Cota cota = controladorBens.buscarCota(idCota);

        if (venda.getCarrinhoDeComprasCotas().contains(cota)) {
            venda.removerCotaCarrinho(cota);
        }
        else {
            throw new IllegalArgumentException("Não existe essa cota no carrinho");
        }
    }

    public String finalizarCompra (Venda venda) {
        if (!venda.getCarrinhoDeComprasCotas().isEmpty()) {
            venda.finalizarCompra();
            // Poderia retornar algum Arquivo (Comprovante) de compra
            return venda.toString();
        }
        else {
            throw new CompraNaoFinalizada("Carrinho vazio");
        }
    }

    public boolean transferenciaDeDireitos(String cpfUsuarioRemetente, String cpfUsuarioDestinario, int idCota) {
        return false;
    }

    public boolean revendaDeDireitos(String cpfUsuarioRemetente, String cpfUsuarioDestinario, int idCota) {
        return false;
    }
}
