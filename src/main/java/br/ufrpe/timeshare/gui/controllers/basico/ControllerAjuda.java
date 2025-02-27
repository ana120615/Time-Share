package br.ufrpe.timeshare.gui.controllers.basico;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ControllerAjuda implements ControllerBase{

    @FXML
    private VBox perguntasContainer;

    @FXML
    private TextField txtBuscar;

    private Scene telaAnterior;


    @FXML
    private Button btnVoltar;

    // Mapeamento de perguntas e respostas
    private final Map<String, String> perguntasRespostas = new HashMap<>();

    @FXML
    public void initialize() {
        // Adiciona perguntas e respostas no Map
        perguntasRespostas.put("Não consigo acessar minha conta", "Se você já está cadastrado, tente recuperar sua senha clicando em 'Esqueceu a senha?' na tela de login. Nela, você deverá colocar o email utilizado no cadastro da sua conta e apertar em buscar. Caso o email seja válido, será enviado um token de recuperação para ele, que precisará ser inserido na tela de recuperar a senha e, caso seja validado, sua senha aparecerá na tela.");
        perguntasRespostas.put("Como a taxa para períodos em que não tenho cota é calculada?", "A taxa é baseada no valor de uma cota do bem associado. É então feito o seguinte cálculo: valor_cota * 0,05 * quantidade_dias, ou seja a taxa corresponde a 5% do valor da cota por dia reservado.");
        perguntasRespostas.put("Qual a diferença entre o administrador e o usuário comum?", "O administrador pode cadastrar bens, ofertá-los, fazer edições, consultar relatórios de reservas e vendas. O usuário comum apenas pode comprar cotas dos bens e realizar reservas.");
        perguntasRespostas.put("Não quero mais utilizar esse sistema. Tem como me desvincular?", "Sim, é possível se desvincular. Uma maneira simples de fazer isso é excluindo a sua conta.");
        perguntasRespostas.put("Por que preciso fazer uma reserva mesmo já possuindo uma cota?", "A reserva garante que seu uso seja registrado e evita conflitos de agendamento. Além disso, ela funciona como um aviso de que o bem será utilizado e podem ser realizados serviços para garantir seu bom estado para uso, como limpeza, por exemplo.");
        perguntasRespostas.put("Tenho alguns imóveis e gostaria de vender algumas parcelas deles. Como eu faço isso?", "Você pode cadastrar os seus imóveis no Flex Share e disponibilizar uma certa quantidade de cotas que posteriormente podem ser ofertadas para que possam ser vendidas a outros usuários. As cotas somente são disponibilizadas para venda depois que o bem é ofertado.");
        perguntasRespostas.put("Fiz uma reforma no meu imóvel e a capacidade aumentou. Como registro isso?", "Em sua tela principal de administrador, você pode visualizar os seus bens cadastrados e, ao clicar neles, é possível editar informações como capacidade.");
        perguntasRespostas.put("Os valores das cotas estão muito altos", "Os valores variam conforme as características do bem. Porém, em períodos de alta temporada é possível garantir um desconto de 25% e nos aniversários o desconto é de 15%.");
        perguntasRespostas.put("Quero fazer uma reserva em um período que não possuo cota. Tem como?", "Sim, porém será cobrada uma taxa a depender da quantidade de dias reservados e do valor de 1 cota do bem.");
        perguntasRespostas.put("O bem pode ter quantas cotas no máximo?", "Como 1 ano possui aproximadamente 52 semanas e cada cota corresponde a 1 semana, 52 é o número máximo de cotas de um bem.");
        perguntasRespostas.put("Tem como acessar quais os bens com mais cotas vendidas?", "Sim, essa informação pode ser acessada na aba de relatórios localizada na tela do administrador.");
        perguntasRespostas.put("Como acessar histórico de uso dos bens?", "Essa informação pode ser acessada na aba de relatórios localizada na tela do administrador.");
        perguntasRespostas.put("Fiz uma reserva, mas queria fazer check-in antes do período", "Só é possível realizar o check-in dentro do período reservado para evitar conflitos de agendamento.");
        perguntasRespostas.put("Não vou poder fazer check-in, pois tive imprevistos. Vou perder meu dinheiro?", "Pode obter um reembolso parcial, que corresponde a 30% da taxa extra. Ou seja, se você reservar em um período em que não possui cotas, é possível conseguir reembolso, caso contrário, nenhum valor é retornado.");
        perguntasRespostas.put("Como saber se uma casa estará disponível daqui a 2 meses?", "Você pode consultar a disponibilidade futura do bem");
        perguntasRespostas.put("Gostei muito de uma chácara que reservei. Queria passar mais alguns dias nela", "Você pode prolongar a estadia se houver disponibilidade.");
        perguntasRespostas.put("Como repasso minhas cotas?", "Você pode transferir cotas para outro usuário pela plataforma. Para isso, basta clicar na cota que deseja repassar e em seguida no botão 'transferir direitos de uso' e preencher as informações necessárias.");
        perguntasRespostas.put("Como funciona o deslocamento das cotas?", "Anualmente, as cotas são deslocadas em 1 semana.");

        // Exibir todas as perguntas inicialmente
        exibirPerguntas(perguntasRespostas.keySet());
    }

    @Override
    public void receiveData(Object data) {
        if (data instanceof Stage) {
            this.telaAnterior = ((Stage) data).getScene(); 
        }
    }

    private void exibirPerguntas(Iterable<String> perguntas) {
        perguntasContainer.getChildren().clear();
        for (String pergunta : perguntas) {
            Hyperlink link = new Hyperlink(pergunta);
            link.setOnAction(event -> mostrarResposta(pergunta));
            perguntasContainer.getChildren().add(link);
        }
    }

    private void mostrarResposta(String pergunta) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrpe/timeshare/gui/application/AjudaRespostas.fxml"));
        Pane root = loader.load();

        ControllerAjudaRespostas controllerRespostas = loader.getController();
        controllerRespostas.exibirResposta(perguntasRespostas.get(pergunta));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Resposta");
        stage.show();
    } catch (IOException e) {
        exibirAlertaErro("Erro", "Problema ao mostrar resposta", e.getMessage()); 
    }
}

private void exibirAlertaErro(String titulo, String header, String contentText) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(header);
        alerta.setContentText(contentText);
        alerta.getDialogPane().setStyle("-fx-background-color:  #ffcccc;"); 
        alerta.showAndWait();
    }

    @FXML
    private void buscarPerguntas(ActionEvent event) {
        String perguntaBusca = txtBuscar.getText().trim().toLowerCase();
        if (perguntaBusca.isEmpty()) {
            exibirPerguntas(perguntasRespostas.keySet()); 
        } else {
            exibirPerguntas(perguntasRespostas.keySet().stream()
                    .filter(pergunta -> pergunta.toLowerCase().contains(perguntaBusca))
                    .collect(Collectors.toList()));
        }
    }
    

    @FXML
    public void voltar(ActionEvent event) {
        if (telaAnterior != null) {
            Stage stage = (Stage) btnVoltar.getScene().getWindow();
            stage.setScene(telaAnterior); 
        } else {
            exibirAlertaErro("Erro", "Problema ao voltar", "Tela anterior nula");
        }
    }

}