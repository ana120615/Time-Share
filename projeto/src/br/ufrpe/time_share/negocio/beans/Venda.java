package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import java.util.ArrayList;

public class Venda {
    private int id;
    private LocalDateTime data;
    private Usuario usuario;
    private ArrayList<Cota> carrinhoDeComprasCotas;
    private boolean foiDescontoAplicado;
    private Promocao promocao;

    {
        this.carrinhoDeComprasCotas = new ArrayList<>();
    }

    public Venda(int id) {
        this.id = id;
        this.foiDescontoAplicado = false;
        this.promocao = new Promocao();
    }

    //Métodos get e set
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setUsuario(Usuario usuarioComum) {
        this.usuario = usuarioComum;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public boolean getFoiDescontoAplicado() {
        return foiDescontoAplicado;
    }

    public void setFoiDescontoAplicado(boolean foiDescontoAplicado) {
        this.foiDescontoAplicado = foiDescontoAplicado;
    }

    //Outros métodos


    public double calcularValorTotal() {

        double resultado = 0;
        if (carrinhoDeComprasCotas != null) {
            for (Cota c : carrinhoDeComprasCotas) {
                resultado += c.getPreco();
            }
        }

        if (getFoiDescontoAplicado()) {
            resultado = resultado - (resultado * promocao.calcularTaxaPromocao(LocalDateTime.now(), usuario));
        }

        return resultado;
    }

    public void adicionarCotaCarrinho(Cota cota) {

        if (cota != null) {
            carrinhoDeComprasCotas.add(cota);
            cota.setStatusDeDisponibilidadeParaCompra(false);
            calcularValorTotal();
        }

    }

    public void removerCotaCarrinho(Cota cota) {

        if (cota != null) {
            carrinhoDeComprasCotas.remove(cota);
            cota.setStatusDeDisponibilidadeParaCompra(true);
            calcularValorTotal();
        }

    }

    public void finalizarCompra() {
        if (carrinhoDeComprasCotas != null) {
            for (Cota cota : carrinhoDeComprasCotas) {
                cota.setProprietario(usuario);
            }
        }
        System.out.println("Compra finalizada");
    }

    public ArrayList<Cota> getCarrinhoDeComprasCotas() {
        return this.carrinhoDeComprasCotas;
    }


    @Override
    public String toString() {

        String notaFiscal = "Nota Fiscal: \n ";

        notaFiscal += "Cliente: " + usuario.getNome() + "\n";
        notaFiscal += "--------------------------------------\n";
        notaFiscal += " FLEX SHARE \n";
        notaFiscal += "--------------------------------------\n";
        notaFiscal += "CPF: " + usuario.getCpf() + "\n";
        notaFiscal += "Valor: R$" + calcularValorTotal() + "\n";
        notaFiscal += "Data de Emissão: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n";
        notaFiscal += "Cotas no carrinho: " + "\n";
        for (Cota c : carrinhoDeComprasCotas) {
            notaFiscal += c + "\n";
        }
        String numeroNotaFiscal = UUID.randomUUID().toString();
        notaFiscal += "\nNúmero da Nota Fiscal: " + numeroNotaFiscal + "\n";

        return notaFiscal;
    }
}