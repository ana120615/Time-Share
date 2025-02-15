package br.ufrpe.timeshare.negocio;

import br.ufrpe.timeshare.excecoes.*;
import br.ufrpe.timeshare.negocio.beans.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ControladorVendas {
    ControladorBens controladorBens;
    ControladorUsuarioGeral controladorUsuarioGeral;
    ControladorReservas controladorReservas;

    {
        this.controladorBens = new ControladorBens();
        this.controladorUsuarioGeral = new ControladorUsuarioGeral();
        this.controladorReservas = new ControladorReservas();
    }

    public Venda iniciarVenda(long cpfUsuario) throws UsuarioNaoExisteException {
        Usuario comprador = controladorUsuarioGeral.procurarUsuarioPorCpf(cpfUsuario);
        Random random = new Random();
        if (comprador != null) {
            int randomId = 99999 + random.nextInt(1000000);
            Venda venda = new Venda(randomId);
            venda.setUsuario(comprador);

            return venda;
        } else {
            throw new UsuarioNaoExisteException("Usuário não existe.");
        }
    }

    public void adicionarCotaCarrinho(int idCota, Venda venda) throws CotaNaoExisteException, CotaNaoOfertadaException {
        Cota cotaVenda = controladorBens.buscarCota(idCota);

        if (cotaVenda.getStatusDeDisponibilidadeParaCompra()) {
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


    public String finalizarCompra(Venda venda) throws CompraNaoFinalizada {
        if (!venda.getCarrinhoDeComprasCotas().isEmpty()) {
            venda.finalizarCompra();
            return venda.toString();
        } else {
            throw new CompraNaoFinalizada("Carrinho vazio.");
        }
    }

    public boolean transferenciaDeDireitos(long cpfUsuarioRemetente, long cpfUsuarioDestinario, int idCota) throws CotaNaoExisteException, UsuarioNaoExisteException, TransferenciaInvalidaException {
        Cota cotaTransferida = controladorBens.buscarCota(idCota);

        Usuario usuarioRemetente = controladorUsuarioGeral.procurarUsuarioPorCpf(cpfUsuarioRemetente);
        Usuario usuarioDestinatario = controladorUsuarioGeral.procurarUsuarioPorCpf(cpfUsuarioDestinario);

        if (!cotaTransferida.getStatusDeDisponibilidadeParaReserva()) {
            throw new TransferenciaInvalidaException("Não é possível transferir uma cota que já foi reservada.");
        }

        if (cotaTransferida.getProprietario().equals(usuarioRemetente)) {
            if (!usuarioRemetente.equals(usuarioDestinatario)) {
                cotaTransferida.setProprietario(usuarioDestinatario);
                return true;
            } else {
                throw new TransferenciaInvalidaException("Operação não permitida.");
            }

        } else {
            throw new ProprietarioNaoIdentificadoException("Usuário não é o Proprietário da cota.");
        }
    }

    public String verificarSeUsuarioPossuiDescontos(long cpfUsuario) throws UsuarioNaoExisteException {
        String resultado = "";
        Usuario usuario = controladorUsuarioGeral.procurarUsuarioPorCpf(cpfUsuario);
        Promocao promocao = new Promocao();

        if (usuario.verificarAniversario() || promocao.ehAltaTemporada(LocalDateTime.now())) {
            if (usuario.verificarAniversario()) {
                resultado += "\nDesconto aniversário de " + promocao.getTaxaPromocaoAniversario() * 100 + "%.";
            }

            if (promocao.ehAltaTemporada(LocalDateTime.now())) {
                resultado += "\nDesconto temporada de " + promocao.getTaxaPromocaoTemporada(LocalDateTime.now()) * 100 + "%.";
            }
        }
        return resultado;
    }

    public boolean aplicarDesconto(Venda venda, long cpfUsuario) throws UsuarioNaoExisteException {
        boolean resultado = false;

        if (venda != null && !venda.getFoiDescontoAplicado()) {
            venda.setFoiDescontoAplicado(true);
            resultado = true;
        }
        return resultado;
    }

    public List<Reserva> getReservasNoPeriodoVenda(Venda venda) {
        List<Reserva> resultado = new ArrayList<>();
        for(Cota cotas : venda.getCarrinhoDeComprasCotas()) {
            resultado.addAll(controladorReservas.buscarReservasPorMultiplosPeriodos(cotas.getBemAssociado(), cotas.getDataInicio(), cotas.getDataFim()));
        }
        return resultado;
    }

    private void cancelarReservasEmCota(Venda venda) {
        controladorReservas.cancelarListaReservas(getReservasNoPeriodoVenda(venda));
    }

    public String gerarComprovanteTransferencia(long cpfUsuarioRemetente, long cpfUsuarioDestinario, int idCota) throws CotaNaoExisteException, UsuarioNaoExisteException, TransferenciaInvalidaException {
        String resultado = "";
        Cota cotaTransferida = controladorBens.buscarCota(idCota);

        Usuario usuarioRemetente = controladorUsuarioGeral.procurarUsuarioPorCpf(cpfUsuarioRemetente);
        Usuario usuarioDestinatario = controladorUsuarioGeral.procurarUsuarioPorCpf(cpfUsuarioDestinario);
        resultado += "\nComprovante de Transferencia\n";
        resultado += "Cliente Remetente: " + usuarioRemetente.getNome() + " | CPF: " + usuarioRemetente.getId() + "\n";
        resultado += "Cliente Destinatario: " + usuarioDestinatario.getNome() + " | CPF: " + usuarioDestinatario.getId() + "\n";
        resultado += "--------------------------------------\n";
        resultado += " FLEX SHARE \n";
        resultado += "--------------------------------------\n";
        resultado += "Informacoes da Cota Transferida\n";
        resultado += "Id: " + cotaTransferida.getId() + " | Bem associado: " + cotaTransferida.getBemAssociado() + "\n| Preco: " + cotaTransferida.getPreco() + " | Periodo Correspondente ao Ano Atual: " + cotaTransferida.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "-" + cotaTransferida.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n";
        resultado += "Data de Emissão: " + LocalDate.now() + "\n";
        String numeroComprovante = UUID.randomUUID().toString();
        resultado += "\nNúmero do Comprovante: " + numeroComprovante + "\n";
        return resultado;
    }
}
