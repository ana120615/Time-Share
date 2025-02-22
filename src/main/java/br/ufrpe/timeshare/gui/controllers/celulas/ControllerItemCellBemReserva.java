package br.ufrpe.timeshare.gui.controllers.celulas;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;

import javafx.geometry.Insets;

import javafx.scene.Scene;

import javafx.scene.control.Alert;

import javafx.scene.control.Button;

import javafx.scene.control.DateCell;

import javafx.scene.control.DatePicker;

import javafx.scene.control.Label;

import javafx.scene.image.Image;

import javafx.scene.image.ImageView;

import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;

import javafx.stage.Modality;

import javafx.stage.Stage;

import br.ufrpe.timeshare.gui.application.ScreenManager;

import br.ufrpe.timeshare.negocio.ControladorReservas;

import br.ufrpe.timeshare.negocio.beans.Bem;

import br.ufrpe.timeshare.negocio.beans.Usuario;


import java.io.File;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;

import java.util.List;

import java.util.stream.Collectors;


public class ControllerItemCellBemReserva {


    @FXML

    private HBox itemCell;


    @FXML

    private ImageView imagemBem;


    @FXML

    private Label nomeBem;


    @FXML

    private Label descricaoBem;


    @FXML

    private Label localizacaoBem;


    @FXML

    private Label capacidadeBem;


    private Bem bem;

    private ControladorReservas controladorReservas;

    private Usuario usuarioLogado;

    private DatePicker dataInicioPicker;

    private DatePicker dataFimPicker;


    public void initialize() {

        this.controladorReservas = new ControladorReservas();


        // Usuario logado

        Object data = ScreenManager.getInstance().getData();

        if (data instanceof Usuario) {

            this.usuarioLogado = (Usuario) data;

        }
        this.dataInicioPicker = new DatePicker();

        this.dataFimPicker = new DatePicker();

    }


    private void configurarDayCellFactory() {
        if (dataInicioPicker != null && dataFimPicker != null) {
            try {
                LocalDate fimDoAno = LocalDate.of(Year.now().getValue(), 12, 31);
                List<String> periodosDisponiveisString = controladorReservas.consultarDisponibilidadeParaReserva(bem, LocalDate.now().atStartOfDay(), fimDoAno.atStartOfDay(), usuarioLogado);
                List<LocalDate> datasDisponiveis = periodosDisponiveisString.stream()
                        .map(dateString -> LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .map(LocalDateTime::toLocalDate)
                        .collect(Collectors.toList());

                dataInicioPicker.setDayCellFactory(picker -> new DateCell() {
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        if (empty || date == null || !datasDisponiveis.contains(date)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #cccccc;");
                        } else {
                            setStyle("-fx-background-color: #ccffcc;");
                        }
                    }
                });

                dataFimPicker.setDayCellFactory(picker -> new DateCell() {
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        if (empty || date == null || !datasDisponiveis.contains(date)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #cccccc;");
                        } else {
                            setStyle("-fx-background-color: #ccffcc;");
                        }
                    }
                });
            } catch (Exception e) {
                exibirAlertaErro("Erro", "Erro ao carregar datas disponíveis", e.getMessage());
            }
        } else {
            exibirAlertaErro("Erro", "Erro ao inicializar date pickers", "Os date pickers não foram inicializados corretamente");
        }
    }


    public void setItem(Bem bem) {

        if (bem != null) {

            this.bem = bem;
            nomeBem.setText(bem.getNome());

            descricaoBem.setText(bem.getDescricao());

            localizacaoBem.setText(bem.getLocalizacao());

            capacidadeBem.setText(String.valueOf(bem.getCapacidade()));


            if (bem.getCaminhoImagem() != null && !bem.getCaminhoImagem().isEmpty()) {

                File file = new File(bem.getCaminhoImagem());

                if (file.exists()) {

                    Image image = new Image(file.toURI().toString());

                    imagemBem.setImage(image);

                } else {

                    imagemBem.setImage(null);

                }

            } else {

                imagemBem.setImage(null);

            }

        }

    }


    @FXML

    public void abrirMiniTelaReserva(ActionEvent event) {

        Stage popupStage = new Stage();

        popupStage.initModality(Modality.APPLICATION_MODAL);

        popupStage.setTitle("Reservar " + bem.getNome());


        Button confirmarButton = new Button("Confirmar");

        Button cancelarButton = new Button("Cancelar");

        configurarDayCellFactory();

        confirmarButton.setOnAction(e -> {


            LocalDate dataInicio = dataInicioPicker.getValue();

            LocalDate dataFim = dataFimPicker.getValue();


            if (dataInicio != null && dataFim != null) {

                try {

                    confirmarReserva(event, dataInicio, dataFim);

                    popupStage.close(); // Fechar a mini tela ao confirmar

                } catch (NumberFormatException ex) {

                    exibirAlertaErro("Erro", "Período inválido", "Por favor, insira um número válido de meses.");

                }

            } else {

                exibirAlertaErro("Erro", "Dados inválidos", "Por favor, preencha todos os campos.");

            }

        });


        cancelarButton.setOnAction(e -> popupStage.close());

        VBox layout = new VBox(10);

        layout.getChildren().addAll(dataInicioPicker, dataFimPicker, confirmarButton, cancelarButton);

        layout.setPadding(new Insets(20));


        Scene scene = new Scene(layout);

        popupStage.setScene(scene);

        popupStage.showAndWait();

    }


    private boolean reservarBem(LocalDate dataInicio, LocalDate dataFim) {

        try {

            LocalDateTime inicioPeriodo = dataInicio.atStartOfDay();

            LocalDateTime fimPeriodo = dataFim.atStartOfDay();


            List<String> periodosDisponiveisString = controladorReservas.consultarDisponibilidadeParaReserva(bem, inicioPeriodo, fimPeriodo, usuarioLogado);


            List<LocalDate> datasDisponiveis = periodosDisponiveisString.stream()

                    .map(dateString -> LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME))

                    .map(LocalDateTime::toLocalDate)

                    .collect(Collectors.toList());


// Filtrar DatePickers para exibir apenas datas disponíveis

            dataInicioPicker.setDayCellFactory(picker -> new DateCell() {

                public void updateItem(LocalDate date, boolean empty) {

                    super.updateItem(date, empty);

                    if (empty || date == null || !datasDisponiveis.contains(date)) {

                        setDisable(true);

                        setStyle("-fx-background-color: #cccccc;"); // Cinza claro para datas indisponíveis

                    } else {

                        setStyle("-fx-background-color: #ccffcc;"); // Verde claro para datas disponíveis
                    }

                }

            });

            dataFimPicker.setDayCellFactory(picker -> new DateCell() {

                public void updateItem(LocalDate date, boolean empty) {

                    super.updateItem(date, empty);
                    if (empty || date == null || !datasDisponiveis.contains(date)) {

                        setDisable(true);

                        setStyle("-fx-background-color: #cccccc;"); // Cinza claro para datas indisponíveis

                    } else {

                        setStyle("-fx-background-color: #ccffcc;"); // Verde claro para datas disponíveis
                    }

                }

            });


// Verificar se o período selecionado está disponível

            LocalDateTime dataAtual = inicioPeriodo;

            boolean periodoDisponivel = true;


            while (dataAtual.isBefore(fimPeriodo)) {

                if (!datasDisponiveis.contains(dataAtual.toLocalDate())) {

                    periodoDisponivel = false;

                    break;

                }

                dataAtual = dataAtual.plusDays(1);

            }

            return periodoDisponivel;

        } catch (Exception e) {

            exibirAlertaErro("Erro", "Erro ao verificar disponibilidade", e.getMessage());

            return false;

        }

    }


    public void confirmarReserva(ActionEvent event, LocalDate dataInicio, LocalDate dataFim) {
        String comprovante = null;

        boolean periodoDisponivel = reservarBem(dataInicio, dataFim);

        if (periodoDisponivel) {

            LocalDateTime inicioPeriodo = dataInicio.atStartOfDay();

            LocalDateTime fimPeriodo = dataFim.atStartOfDay();

            try {

                comprovante = controladorReservas.criarReserva(inicioPeriodo, fimPeriodo, usuarioLogado, bem);

                exibirAlertaInfo("Sucesso", "Reserva realizada", comprovante);


            } catch (Exception e) {

                exibirAlertaErro("Erro", "Erro ao criar reserva", e.getMessage());

            }

        } else {

            exibirAlertaErro("Erro", "Período indisponível", "O período não está disponível para reserva.");

        }

    }


    private void exibirAlertaErro(String titulo, String header, String contentText) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle(titulo);

        alerta.setHeaderText(header);

        alerta.setContentText(contentText);

        alerta.getDialogPane().setStyle("-fx-background-color: #ffcccc;"); // Vermelho claro

        alerta.showAndWait();

    }


    private void exibirAlertaInfo(String titulo, String header, String contentText) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle(titulo);

        alerta.setHeaderText(header);

        alerta.setContentText(contentText);

        alerta.showAndWait();

    }
}