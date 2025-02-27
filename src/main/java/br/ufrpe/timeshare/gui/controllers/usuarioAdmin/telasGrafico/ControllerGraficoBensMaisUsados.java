package br.ufrpe.timeshare.gui.controllers.usuarioAdmin.telasGrafico;

import br.ufrpe.timeshare.excecoes.DadosInsuficientesException;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.gui.controllers.basico.ControllerBase;
import br.ufrpe.timeshare.negocio.ControladorBens;
import br.ufrpe.timeshare.negocio.ControladorReservas;
import br.ufrpe.timeshare.negocio.beans.Bem;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControllerGraficoBensMaisUsados implements ControllerBase {
    private final ControladorReservas controladorReservas;
    private final ControladorBens controladorBens;
    private Usuario usuario;

    @FXML
    private ComboBox<String> cbBens;

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
        carregarBens();
        cbBens.setOnAction(event -> atualizarGrafico(cbBens.getValue()));

        // Configura o eixo Y para mostrar apenas números inteiros
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
            @Override
            public String toString(Number object) {
                return String.format("%d", object.intValue()); // Converte para inteiro
            }
        });
    }

    private void carregarBens() {
        List<Bem> bens = controladorBens.listarBens();
        List<String> nomesBens = new ArrayList<>();
        nomesBens.add("Todos os Bens");

        for (Bem bem : bens) {
            nomesBens.add(bem.getNome());
        }

        cbBens.setItems(FXCollections.observableArrayList(nomesBens));
        cbBens.getSelectionModel().selectFirst(); // Seleciona "Todos os Bens" por padrão
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

            cbBens.getItems().clear();
            cbBens.getItems().add("Todos os Bens");
            for (Bem bem : bens) {
                cbBens.getItems().add(bem.getNome());
            }
            cbBens.setValue("Todos os Bens");

            atualizarGrafico("Todos os Bens");

        } catch (DadosInsuficientesException e) {
            System.out.println("Erro ao carregar bens: " + e.getMessage());
        }
    }

    private void atualizarGrafico(String bemSelecionado) {
        barChartBensMaisUsados.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Reservas por Mês");

        try {
            Map<YearMonth, Long> dadosReservas;

            if ("Todos os Bens".equals(bemSelecionado)) {
                dadosReservas = controladorReservas.reservasPorMesTodosOsBens();
            } else {
                long idBem = controladorBens.buscarBemIdPorNome(bemSelecionado);
                dadosReservas = controladorReservas.reservasPorMes((int) idBem);
            }

            List<String> categorias = new ArrayList<>();
            List<YearMonth> sortedYearMonths = new ArrayList<>(dadosReservas.keySet());

            // Ordenar os YearMonth cronologicamente
            sortedYearMonths.sort((ym1, ym2) -> ym1.compareTo(ym2));

            // Adicionar os dados ordenados no gráfico
            for (YearMonth yearMonth : sortedYearMonths) {
                String mesFormatado = yearMonth.getMonth().toString().substring(0, 3) + "/" + yearMonth.getYear();
                series.getData().add(new XYChart.Data<>(mesFormatado, dadosReservas.get(yearMonth)));
                categorias.add(mesFormatado);
            }

            // Configurar o eixo X corretamente com os meses ordenados
            xAxis.setCategories(FXCollections.observableArrayList(categorias));

            // Adicionar a série ao gráfico
            barChartBensMaisUsados.getData().add(series);
            barChartBensMaisUsados.layout(); // Força atualização da interface

        } catch (Exception e) {
            System.out.println("Erro ao atualizar gráfico: " + e.getMessage());
        }
    }



    public void voltarTelaPrincipalUsuarioComum(ActionEvent event) {
        ScreenManager.getInstance().showAdmPrincipalScreen();
    }
}
