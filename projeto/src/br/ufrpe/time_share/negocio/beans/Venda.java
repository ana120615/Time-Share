package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import java.util.ArrayList;

public class Venda {
    private int id;
    private LocalDateTime data;
    private Cota cota;
    private UsuarioComum usuarioComum;
    private ArrayList<Cota> carrinhoDeComprasCotas;

    {
        this.carrinhoDeComprasCotas = new ArrayList<>(10);
    }

    public Venda(int id, UsuarioComum usuarioComum) {
        this.id = id;
        this.usuarioComum = usuarioComum;


    }

    //Métodos get e set
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setCota(Cota cota) {
        this.cota = cota;
    }

    public Cota getCota() {
        return this.cota;
    }

    public void setUsuario(UsuarioComum usuarioComum) {
        this.usuarioComum = usuarioComum;
    }

    public Usuario getUsuario() {
        return this.usuarioComum;
    }


//Outros métodos

    public String gerarNotaFiscal() {
        String notaFiscal = "Nota Fiscal: \n ";

        notaFiscal += "Cliente: " + usuarioComum.getNome() + "\n";
        notaFiscal += "--------------------------------------\n";
        notaFiscal += " Flex Share \n";
        notaFiscal += "--------------------------------------\n";
        notaFiscal += "CPF: " + usuarioComum.getCpf() + "\n";
        notaFiscal += "Valor: R$" + calcularValorTotal() + "\n";
        notaFiscal += "Data de Emissão: " + LocalDate.now() + "\n";
        String numeroNotaFiscal = UUID.randomUUID().toString();
        notaFiscal += "Número da Nota Fiscal: " + numeroNotaFiscal + "\n";

        return notaFiscal;


    }

    public double calcularValorTotal() {
        double resultado = 0;
        for (Cota c : carrinhoDeComprasCotas) {
            resultado += c.getPreco();
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
		usuarioComum.setCotasAdquiridas(carrinhoDeComprasCotas);
        for (Cota cota : carrinhoDeComprasCotas) {
            cota.setProprietario(usuarioComum);
        }
        carrinhoDeComprasCotas = null;
        System.out.println("Compra finalizada");
    }

    public ArrayList<Cota> getCarrinhoDeComprasCotas() {
        return this.carrinhoDeComprasCotas;
    }


    @Override
    public String toString() {
        return "Venda{" +
                "id=" + id +
                ", data=" + data +
                ", valorTotal=" + calcularValorTotal() +
                ", cota=" + cota +
                ", usuarioComum=" + usuarioComum +
                ", carrinhoDeComprasCotas=" + carrinhoDeComprasCotas +
                '}';
    }
}