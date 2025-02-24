package br.ufrpe.timeshare.gui.controllers.usuarioComum.telaReservas;

import br.ufrpe.timeshare.excecoes.*;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.gui.controllers.basico.ControllerBase;
import br.ufrpe.timeshare.negocio.ControladorReservas;
import br.ufrpe.timeshare.negocio.beans.Reserva;
import br.ufrpe.timeshare.negocio.beans.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerMinhasReservas implements ControllerBase {
    @FXML
    private ListView<VBox> reservasListView;

    @FXML
    private TextField nomeBemTextField;

    private ObservableList<Reserva> reservas = FXCollections.observableArrayList();

    ControladorReservas controladorReservas;

    private Usuario usuarioLogado;

    private DatePicker dataInicioPicker;

    private DatePicker dataFimPicker;

    @FXML
    public void initialize() {
        controladorReservas = new ControladorReservas();
        this.dataInicioPicker = new DatePicker();
        this.dataFimPicker = new DatePicker();
    }

    public void receberReservas(Usuario usuario) {
        reservas.clear();
        reservas.addAll(controladorReservas.listarReservasUsuario(usuario));
    }

    @Override
    public void receiveData(Object data) {
        System.out.println("receiveData chamado com: " + data);
        if (data instanceof Usuario) {
            this.usuarioLogado = (Usuario) data;
            System.out.println("Usuário definido: " + usuarioLogado.getNome());
            receberReservas(usuarioLogado);
            exibirReservas();
        } else {
            System.err.println("Erro: receiveData recebeu um objeto inválido.");
        }
    }


    
    @FXML
    public void buscarReservas(ActionEvent event) {
        
        String nomeBem = null;
        // Se o nome do bem for preenchido, armazena o valor
        if (nomeBemTextField != null && !nomeBemTextField.getText().trim().isEmpty()) {
            nomeBem = nomeBemTextField.getText().trim();
        }
    
            List<Reserva> reservasFiltradas = null;
            if (nomeBem == null) {
                reservasFiltradas = controladorReservas.listarReservasUsuario(usuarioLogado);  // Método para buscar todas as reservas
                System.out.println("Reservas encontradas: " + reservasFiltradas.size());
            }
            else{
            try {
                reservasFiltradas = controladorReservas.buscarReservaPorNomeBem(usuarioLogado, nomeBem);
            } catch (Exception e) {
                exibirAlertaErro("Erro", "Problema ao buscar reserva por atributo", e.getMessage());
            }
            System.out.println("Reservas encontradas: " + reservasFiltradas.size());
            }
            // Atualiza a lista com as reservas filtradas, garantindo que seja ObservableList<VBox>
            ObservableList<VBox> itens = FXCollections.observableArrayList();
    
            // Se a lista de reservas não estiver vazia, exibe as reservas
            if (reservasFiltradas != null && !reservasFiltradas.isEmpty()) {
                // Limpa as reservas e adiciona os novos itens em formato VBox
                for (Reserva reserva : reservasFiltradas) {
                    VBox item = criarItemReserva(reserva);
                    itens.add(item);
                }
            } else {
                // Caso não haja reservas encontradas, limpa a lista
                itens.clear();
            }
    
            // Atualiza o ListView com a lista de itens
            reservasListView.setItems(itens);  // Agora estamos passando ObservableList<VBox>
            nomeBemTextField.clear();
    }
    


    private void exibirReservas() {
        ObservableList<VBox> itens = FXCollections.observableArrayList();
        for (Reserva reserva : reservas) {
            VBox item = criarItemReserva(reserva);
            itens.add(item);
        }
        reservasListView.setItems(itens);
    }

    private VBox criarItemReserva(Reserva reserva) {
        Label labelReserva = new Label(reserva.toString());

        Button checkinButton = new Button("Check-in");
        checkinButton.setOnAction(e -> fazerCheckin(reserva));

        Button alterarPeriodoButton = new Button("Alterar Período");
        alterarPeriodoButton.setOnAction(e -> alterarPeriodo(reserva));

        Button cancelarReservaButton = new Button("Cancelar");
        cancelarReservaButton.setOnAction(e -> cancelarReserva(reserva));

        HBox botoes = new HBox(checkinButton, alterarPeriodoButton, cancelarReservaButton);
        botoes.setSpacing(10);

        return new VBox(labelReserva, botoes);
    }

    //para mensagem de erro
    private void exibirAlertaErro(String titulo, String header, String contentText) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(header);
        alerta.setContentText(contentText);
        alerta.getDialogPane().setStyle("-fx-background-color:  #ffcccc;"); // Vermelho claro
        alerta.showAndWait();
    }

    private void exibirAlertaInfo(String titulo, String header, String contentText) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle(titulo);

        alerta.setHeaderText(header);

        alerta.setContentText(contentText);

        alerta.showAndWait();

    }

    //Realizar checkin
    private void fazerCheckin(Reserva reserva) {
        System.out.println("Check-in da reserva: ");
        int id = (int) reserva.getId();
        String comprovante = null;
        try {
            comprovante = controladorReservas.checkin(id);
            exibirAlertaInfo("Check in realizado com sucesso", "Comprovante", comprovante);
            receberReservas(usuarioLogado);
            exibirReservas();
        } catch (EstadiaJaInicializadaException | ReservaNaoExisteException | ReservaJaCanceladaException
                 | ForaPeriodoException e) {
            exibirAlertaErro("Erro", "Problema ao realizar check in", e.getMessage());
        }
    }


    //Para date picker
    private void configurarDayCellFactory(Reserva reserva) {
        if (dataInicioPicker != null && dataFimPicker != null) {
            try {
                LocalDate fimDoAno = LocalDate.of(Year.now().getValue(), 12, 31);
                List<String> periodosDisponiveisString = controladorReservas.consultarDisponibilidadeParaReserva(reserva.getBem(), LocalDate.now().atStartOfDay(), fimDoAno.atStartOfDay(), usuarioLogado);
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
                System.out.println("Erro: " + e.getMessage());
                exibirAlertaErro("Erro", "Erro ao carregar datas disponíveis", e.getMessage());
            }
        } else {
            exibirAlertaErro("Erro", "Erro ao inicializar date pickers", "Os date pickers não foram inicializados corretamente");
        }
    }


    private boolean alterarPeriodoReserva(Reserva reserva, LocalDate dataInicio, LocalDate dataFim) {

        try {

            LocalDateTime inicioPeriodo = dataInicio.atStartOfDay();

            LocalDateTime fimPeriodo = dataFim.atStartOfDay();


            List<String> periodosDisponiveisString = controladorReservas.consultarDisponibilidadeParaReserva(reserva.getBem(), inicioPeriodo, fimPeriodo, usuarioLogado);


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

    public void confirmarAlteracao(ActionEvent event, Reserva reserva, LocalDate dataInicio, LocalDate dataFim) {

        boolean periodoDisponivel = alterarPeriodoReserva(reserva, dataInicio, dataFim);

        if (periodoDisponivel) {

            LocalDateTime inicioPeriodo = dataInicio.atStartOfDay();

            LocalDateTime fimPeriodo = dataFim.atStartOfDay();

            int id = (int) reserva.getId();
            String comprovante = null;
            try {
                comprovante = controladorReservas.alterarPeriodoReserva(id, inicioPeriodo, fimPeriodo, usuarioLogado);
                exibirAlertaInfo("Alteracao realizada com sucesso", "Comprovante", comprovante);
                receberReservas(usuarioLogado);
                exibirReservas();
            } catch (ReservaNaoExisteException | ReservaJaCanceladaException | ForaPeriodoException
                     | PeriodoJaReservadoException | PeriodoNaoDisponivelParaReservaException | CotaJaReservadaException
                     | ReservaJaExisteException | DadosInsuficientesException | UsuarioNaoPermitidoException e) {
                exibirAlertaErro("Erro", "Problema ao alterar periodo da reserva", e.getMessage());
            }

        } else {

            exibirAlertaErro("Erro", "Período indisponível", "O período não está disponível para reserva.");

        }
    }

    public void alterarPeriodo(Reserva reserva) {

        Stage popupStage = new Stage();

        popupStage.initModality(Modality.APPLICATION_MODAL);

        popupStage.setTitle("Alterar período ");


        Button confirmarButton = new Button("Confirmar");

        Button cancelarButton = new Button("Cancelar");

        configurarDayCellFactory(reserva);

        confirmarButton.setOnAction(e -> {


            LocalDate dataInicio = dataInicioPicker.getValue();

            LocalDate dataFim = dataFimPicker.getValue();


            if (dataInicio != null && dataFim != null) {

                confirmarAlteracao(e, reserva, dataInicio, dataFim);

                popupStage.close(); // Fechar a mini tela ao confirmar


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


    private void cancelarReserva(Reserva reserva) {
        // Lógica para cancelar a reserva
        System.out.println("Cancelar reserva: ");
        int id = (int) reserva.getId();
        String comprovante = null;

        try {
            comprovante = controladorReservas.cancelarReserva(id, usuarioLogado);
            exibirAlertaInfo("Reserva cancelada com sucesso", "Comprovante", comprovante);
            receberReservas(usuarioLogado);
            exibirReservas();
        } catch (ReservaNaoExisteException | ReservaJaCanceladaException | CotaJaReservadaException
                 | UsuarioNaoPermitidoException | ReservaNaoReembolsavelException | DadosInsuficientesException e) {
            exibirAlertaErro("Erro", "Problema ao cancelar reserva", e.getMessage());
        }
    }

    //voltar para tela principal
    public void voltarParaPrincipalComum(ActionEvent event) {
        ScreenManager.getInstance().showUsuarioComumPrincipalScreen();
    }
}