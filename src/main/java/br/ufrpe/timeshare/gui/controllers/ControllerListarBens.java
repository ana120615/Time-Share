package br.ufrpe.timeshare.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

import br.ufrpe.timeshare.negocio.beans.Bem;
import br.ufrpe.timeshare.dados.IRepositorioBens;
import br.ufrpe.timeshare.dados.IRepositorioCotas;
import br.ufrpe.timeshare.dados.RepositorioBens;
import br.ufrpe.timeshare.dados.RepositorioCotas;
import br.ufrpe.timeshare.gui.application.ScreenManager;
import br.ufrpe.timeshare.negocio.ControladorBens;

import java.io.InputStream;
import java.util.List;

public class ControllerListarBens implements ControllerBase {

    @FXML
    private ListView<Bem> listViewBens;

    @Override
    public void receiveData(Object data) {
        IRepositorioBens repositorioBens = RepositorioBens.getInstancia();
        IRepositorioCotas repositorioCotas = RepositorioCotas.getInstancia();

        ControladorBens controladorBens = new ControladorBens(repositorioBens, repositorioCotas);
        List<Bem> bens = controladorBens.listarBens();

        if (bens == null || bens.isEmpty()) {
            System.out.println("A lista de bens esta vazia ou nula.");
            return;
        }

        // Definir o CellFactory para exibiçao na ListView
        listViewBens.setCellFactory(param -> new ListCell<Bem>() {
            @Override
            protected void updateItem(Bem bem, boolean empty) {
                super.updateItem(bem, empty);

                if (empty || bem == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                
                HBox hbox = new HBox(10);
                hbox.setStyle("-fx-padding: 5px;");

                
                Label labelNome = new Label(bem.getNome());
                Label labelDescricao = new Label(bem.getDescricao());
                Label labelId = new Label("ID: " + bem.getId());
                Label labelLocalizacao = new Label("Localização: " + bem.getLocalizacao());
                Label labelCapacidade = new Label("Capacidade: " + bem.getCapacidade());
                Label labelOfertado = new Label("Ofertado: " + (bem.isOfertado() ? "Sim" : "Não"));
                Label labelCadastradoPor = new Label("Cadastrado por: " + (bem.getCadastradoPor() != null ? bem.getCadastradoPor().getNome() : "Desconhecido"));

                // armazenar as informações de texto
                VBox vbox = new VBox(5);
                vbox.setStyle("-fx-padding: 5px;");
                vbox.getChildren().addAll(labelNome, labelDescricao, labelId, labelLocalizacao, labelCapacidade, labelOfertado, labelCadastradoPor);

                // Verifica caminho para a imagem
                String caminhoRelativo = bem.getCaminhoImagem();
                if (caminhoRelativo != null && !caminhoRelativo.isEmpty()) {
                    try (InputStream input = getClass().getResourceAsStream("/" + caminhoRelativo)) {
                        
                        if (input != null) {
                            ImageView imageView = new ImageView(new Image(input));
                            imageView.setFitHeight(50);
                            imageView.setFitWidth(50);
                            hbox.getChildren().addAll(imageView, vbox);
                        } else {
                            // Caso a imagem nao seja encontrada, nao adiciona a imagem
                            System.err.println("Imagem não encontrada: " + caminhoRelativo);
                            hbox.getChildren().add(vbox); 
                        }
                    } catch (Exception e) {
                        System.err.println("Erro ao carregar imagem: " + e.getMessage());
                        hbox.getChildren().add(vbox); // Apenas exibe os textos
                    }
                } else {
                    // Caso não haja imagem, apenas exibe os textos
                    hbox.getChildren().add(vbox);
                }

                
                setGraphic(hbox);
            }
        });

        
        listViewBens.getItems().setAll(bens);
    }

    @FXML
    public void voltarParaTelaAnterior(ActionEvent event) {
        ScreenManager.getInstance().showAdmPrincipalScreen();
    }
}
