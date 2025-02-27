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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.util.List;

public class ControllerGraficoBensMaisUsados implements ControllerBase {
    private final ControladorReservas controladorReservas;
    private final ControladorBens controladorBens;
    private Usuario usuario;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private BarChart<String, Number> barChartBensMaisUsados;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    private List<Bem> bens;

    public ControllerGraficoBensMaisUsados() {
        this.controladorReservas = new ControladorReservas();
        this.controladorBens = new ControladorBens();
    }

    @FXML
    public void initialize() {
        System.out.println("initialize");

        xAxis.setAutoRanging(true); // Garante que as categorias sejam reconhecidas

        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
            @Override
            public String toString(Number object) {
                return String.format("%d", object.intValue());
            }
        });

        comboBox.setOnAction(event -> atualizarGrafico(comboBox.getValue()));
    }

    @Override
    public void receiveData(Object data) {
        System.out.println("receiveData chamado com: " + data);
        if (data instanceof Usuario) {
            this.usuario = (Usuario) data;
        } else {
            System.err.println("Erro: receiveData recebeu um objeto inválido.");
            return;
        }

        try {
            bens = controladorBens.listarBensUsuario(usuario);

            comboBox.getItems().clear();
            comboBox.getItems().add("Todos");
            for (Bem bem : bens) {
                comboBox.getItems().add(bem.getNome());
            }
            comboBox.setValue("Todos");

            atualizarGrafico("Todos");

        } catch (DadosInsuficientesException e) {
            System.out.println("Erro ao carregar bens: " + e.getMessage());
        }
    }

    private void atualizarGrafico(String bemSelecionado) {
        barChartBensMaisUsados.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Uso de Bens");

        try {
            if (bens == null || bens.isEmpty()) {
                System.out.println("Nenhum bem encontrado.");
                return;
            }

            List<String> categorias = new ArrayList<>();

            if (bemSelecionado.equals("Todos")) {
                for (Bem bem : bens) {
                    String nome = bem.getNome();
                    Number qtdVendas = controladorBens.quantidadeVendasBem(bem);
                    series.getData().add(new XYChart.Data<>(nome, qtdVendas));
                    categorias.add(nome);
                }
            } else {
                for (Bem bem : bens) {
                    if (bem.getNome().equals(bemSelecionado)) {
                        Number qtdVendas = controladorBens.quantidadeVendasBem(bem);
                        series.getData().add(new XYChart.Data<>(bem.getNome(), qtdVendas));
                        categorias.add(bem.getNome());
                        break;
                    }
                }
            }

            // Forçar a atualização do eixo X
            xAxis.setCategories(null); // Reseta as categorias para evitar conflitos
            xAxis.setCategories(javafx.collections.FXCollections.observableArrayList(categorias));

            // Adiciona a série ao gráfico
            barChartBensMaisUsados.getData().add(series);
            barChartBensMaisUsados.layout(); // Força atualização da interface

            ajustarEscalaEixoX(series);
        } catch (Exception e) {
            System.out.println("Erro ao atualizar gráfico: " + e.getMessage());
        }
    }

    private void ajustarEscalaEixoX(XYChart.Series<String, Number> series) {
        if (series.getData().size() == 1) {
            barChartBensMaisUsados.setCategoryGap(100);
            barChartBensMaisUsados.setBarGap(20);
        } else {
            barChartBensMaisUsados.setCategoryGap(30);
            barChartBensMaisUsados.setBarGap(10);
        }
    }

    public void voltarTelaPrincipalUsuarioComum(ActionEvent event) {
        ScreenManager.getInstance().showAdmPrincipalScreen();
    }
}