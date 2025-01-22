package br.ufrpe.time_share.negocio.beans;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import java.util.ArrayList;

public class Venda {
    private int id;
    private LocalDateTime data;
    private Cota cota;
    private Usuario usuario;
    private ArrayList<Cota> carrinhoDeComprasCotas;

    {
        this.carrinhoDeComprasCotas = new ArrayList<>();
    }

    public Venda(int id) {
        this.id = id;
//        this.usuario = usuario;


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

    public void setUsuario(Usuario usuarioComum) {
        this.usuario = usuarioComum;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }


//Outros métodos


    public double calcularValorTotal() {
        double resultado = 0;
        if (carrinhoDeComprasCotas != null) {
            for (Cota c : carrinhoDeComprasCotas) {
                resultado += c.getPreco();
            }
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
        notaFiscal += "Data de Emissão: " + LocalDate.now() + "\n";
        notaFiscal += "Cotas no carrinho: " + "\n";
        for (Cota c : carrinhoDeComprasCotas) {
            notaFiscal += c + "\n";
        }
        String numeroNotaFiscal = UUID.randomUUID().toString();
        notaFiscal += "\nNúmero da Nota Fiscal: " + numeroNotaFiscal + "\n";

        return notaFiscal;
    }
}