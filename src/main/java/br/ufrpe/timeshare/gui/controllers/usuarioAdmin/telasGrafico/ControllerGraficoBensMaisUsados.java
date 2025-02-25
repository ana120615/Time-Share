package br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telasGrafico;

import br.ufrpe.timeshare.excecoes.DadosInsuficientesException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.gui.controllers.basico.ControllerBase;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.ControladorReservas;
import br.ufrpe.timeshare.negocio.beans.Bem;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;

public class ControllerGraficoBensMaisUsados implements ControllerBase {
    private Usuario usuario;
    private final ControladorReservas controladorReservas;
    private final ControladorBens controladorBens;
    @FXML
    private BarChart<String, Number> barChartBensMaisUsados;
    @FXML
    private NumberAxis yAxis;

    public ControllerGraficoBensMaisUsados() {
        this.controladorReservas = new ControladorReservas();
        this.controladorBens = new ControladorBens();
    }

    @FXML
    public void initialize() {
        System.out.println("initialize");

        // Configura o eixo Y para mostrar apenas números inteiros
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
            @Override
            public String toString(Number object) {
                return String.format("%d", object.intValue()); // Converte para inteiro
            }
        });
    }

    @Override
    public void receiveData (Object data) {
        System.out.println("receiveData chamado com: " + data);
        if (data instanceof Usuario) {
            this.usuario = (Usuario) data;
        } else {
            System.err.println("Erro: receiveData recebeu um objeto inválido.");
        }

        barChartBensMaisUsados.getData().clear();
        barChartBensMaisUsados.setBarGap(10);
        barChartBensMaisUsados.setCategoryGap(30);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Bens mais Vendidos");

        try {
            List<Bem> bens = controladorBens.listarBensUsuario(usuario);

            for (Bem bem : bens) {
                String nome = bem.getNome();
                Number qtdVendas = controladorBens.quantidadeVendasBem(bem);
                series.getData().add(new XYChart.Data<>(nome, qtdVendas));
            }

            // Adiciona a série ao gráfico
            barChartBensMaisUsados.getData().add(series);

            // Força atualização do gráfico
            barChartBensMaisUsados.layout();

        } catch (DadosInsuficientesException e) {
            System.out.println("Erro ao carregar bens: " + e.getMessage());
        }
    }


    public void voltarTelaPrincipalUsuarioComum(ActionEvent event) {
        ScreenManager.getInstance().showAdmPrincipalScreen();
    }

}
