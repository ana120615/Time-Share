package br.ufrpe.timeshare;

import br.ufrpe.timeshare.dados.*;
import br.ufrpe.timeshare.excecoes.*;
import br.ufrpe.timeshare.negocio.*;
import br.ufrpe.timeshare.negocio.beans.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sistema {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        //INICIALIZAR REPOSITORIOS
        IRepositorioUsuario repositorioUsuario = RepositorioUsuarios.getInstancia();
        IRepositorioBens repositorioBens = RepositorioBens.getInstancia();
        IRepositorioReservas repositorioReservas = RepositorioReservas.getInstancia();
        IRepositorioCotas repositorioCotas = RepositorioCotas.getInstancia();
        IRepositorioEstadia repositorioEstadia = RepositorioEstadia.getInstancia();

        //INICIALIZANDO CONTROLADORES
        ControladorLogin controladorLogin = new ControladorLogin(repositorioUsuario);
        ControladorUsuarioGeral controladorUsuario = new ControladorUsuarioGeral(repositorioUsuario);
        ControladorBens controladorBens = new ControladorBens(repositorioBens, repositorioCotas);
        ControladorReservas controladorReservas = new ControladorReservas(repositorioReservas, repositorioEstadia);
        ControladorVendas controladorVendas = new ControladorVendas();

        Usuario usuario = null; //Variavel que vai armazenar o usuario apos login

        // Cadastrar Usuário Comum para facilitar na entrada do Sistema

        // ADM
        try {
            controladorUsuario.cadastrar(12345678901L, "Caua", "caua@gmail.com",
                    "senha123", LocalDate.of(2005, 12, 12), TipoUsuario.ADMINISTRADOR);
        } catch (UsuarioJaExisteException | DadosInsuficientesException | UsuarioNaoPermitidoException e) {
            System.out.println(e.getMessage());
        }

        Usuario usuarioAdm = null;
        try {
            usuarioAdm = controladorUsuario.procurarUsuarioPorCpf(12345678901L);
        } catch (UsuarioNaoExisteException e) {
            System.out.println(e.getMessage());
        }


        // CADASTRANDO E OFERTANDO UM BEM
        try {
            controladorBens.cadastrar(1111, "Lar Doce Lar", "Familia Feliz é aqui",
                    "Recife-PE", 5, usuarioAdm, LocalDateTime.of(2025, 01, 01, 12, 00), 20, 6000);
        } catch (BemNaoExisteException | UsuarioNaoPermitidoException | QuantidadeDeCotasExcedidasException |
                 BemJaExisteException | UsuarioNaoExisteException e) {
            System.out.println(e.getMessage());
        }

        try {
            controladorBens.ofertarBem(1111);
        } catch (BemNaoExisteException e) {
            System.out.println(e.getMessage());
        }

        // COMUM
        try {
            controladorUsuario.cadastrar(11111111211L, "usuario1", "usuario1@gmail.com",
                    "senha123", LocalDate.of(2005, 12, 12), TipoUsuario.COMUM);
        } catch (UsuarioJaExisteException | DadosInsuficientesException | UsuarioNaoPermitidoException e) {
            System.out.println(e.getMessage());
        }

        try {
            controladorUsuario.cadastrar(22013311904L, "usuario2", "usuario2@gmail.com",
                    "senha123", LocalDate.of(2005, 12, 12), TipoUsuario.COMUM);
        } catch (UsuarioJaExisteException | DadosInsuficientesException | UsuarioNaoPermitidoException e) {
            System.out.println(e.getMessage());
        }

        RepositorioUsuarios.getInstancia().cadastrar(new Usuario(11111301215L, "usuario10", TipoUsuario.COMUM));

        boolean entrarSistema = false;
        boolean sairSistema = false;
        boolean sairTelaPrincipalAdministrador = false;
        boolean sairTelaPrincipalUsuario = false;
        boolean finalizarPrograma = false;


        while (!finalizarPrograma) {
            controladorBens.deslocarCotasAutomaticamente();
            while (!sairSistema) {
                do {
                    System.out.println("\nBEM-VINDO AO FLEX-SHARE");
                    System.out.println("Já possui cadastro? (1 - sim / 2 - não)");
                    int escolha = input.nextInt();
                    input.nextLine(); // Limpa o buffer após nextInt()

                    if (escolha == 1) {
                        boolean sairLogin = false;
                        while (!sairLogin) {
                            try {
                                System.out.println("-- LOGIN --");
                                System.out.println("Informe o email: ");
                                String email = input.next();
                                input.nextLine(); // Limpa o buffer
                                System.out.println("Informe a senha: ");
                                String password = input.next();
                                input.nextLine(); // Limpa o buffer

                                usuario = controladorLogin.efetuarLogin(email, password);
                                if (usuario != null) {
                                    System.out.println("Login realizado com sucesso!");
                                    entrarSistema = true; // Permite entrar no sistema
                                }
                                sairLogin = true; // Sai do loop de login

                            } catch (UsuarioNaoExisteException | SenhaInvalidaException e) {
                                System.out.println(e.getMessage());

                                // Aqui você pode decidir se quer continuar tentando fazer login ou sair.
                                System.out.println("Deseja tentar novamente o Login? (s/n)");
                                String resposta = input.nextLine();
                                if (resposta.equals("n")) {
                                    sairLogin = true; // Força a saída do loop de login
                                }
                            }
                        }
                    } else if (escolha == 2) {
                        boolean sairCadastro = false;
                        while (!sairCadastro) {
                            try {
                                System.out.println("\n-- CADASTRO USUARIO --");
                                String nome, email, senha, dataNascimento;
                                Long cpf;
                                System.out.println("Informe o nome: ");
                                nome = input.nextLine();  // Usando next
                                System.out.println("Informe o cpf: ");
                                cpf = input.nextLong();  // Usando next
                                System.out.println("Informe a Senha: ");
                                senha = input.next();  // Usando next
                                System.out.println("Informe o Email: ");
                                email = input.next();  // Usando next
                                System.out.println("Informe a data de nascimento (dd/MM/yyyy): ");
                                dataNascimento = input.next();  // Usando next

                                System.out.println("\nEscolha o tipo de usuario: ");
                                System.out.println("1 - ADMINISTRADOR || 2 - COMUM");
                                int tipoUsuario = input.nextInt();  // Usando nextInt para o número


                                if (tipoUsuario == 1 || tipoUsuario == 2) {
                                    controladorUsuario.cadastrar(cpf, nome, email, senha, LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy")), TipoUsuario.fromValor(tipoUsuario));
                                    System.out.println("Usuario cadastrado!");
                                } else {
                                    System.out.println("Opcao invalida!");
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            } finally {
                                sairCadastro = true;
                            }
                        }
                    } else {
                        System.out.println("Não identificado");
                    }
                } while (!entrarSistema);


                do {
                    if (usuario != null && usuario.getTipo().equals(TipoUsuario.COMUM)) {
                        System.out.println("\n\n-- TELA USUARIO --");
                        System.out.println("1 - Configuracoes");
                        System.out.println("2 - Gerenciamento de Reservas");
                        System.out.println("3 - Bens Ofertados");
                        System.out.println("4 - Minhas Cotas");
                        System.out.println("5 - Sair tela de Usuário");
                        int escolha = input.nextInt();
                        switch (escolha) {
                            case 1:
                                boolean sairConfiguracoes = false;
                                while (!sairConfiguracoes) {
                                    System.out.println("\n\n** Configuracoes **");
                                    System.out.println("1 - Ver perfil");
                                    System.out.println("2 - Editar perfil");
                                    System.out.println("3 - Excluir perfil");
                                    System.out.println("4 - Sair");
                                    escolha = input.nextInt();
                                    switch (escolha) {
                                        case 1:
                                            System.out.println(usuario);
                                            break;
                                        case 2:
                                            boolean sairEdicao = false;
                                            while (!sairEdicao) {
                                                System.out.println("O que deseja editar?");
                                                System.out.println("1 - Nome");
                                                System.out.println("2 - Senha");
                                                System.out.println("3 - Data de nascimento");
                                                System.out.println("4 - Voltar");
                                                escolha = input.nextInt();
                                                switch (escolha) {
                                                    case 1:
                                                        try {
                                                            System.out.println("Digite o novo nome: ");
                                                            String novoNome = input.next();
                                                            controladorUsuario.alterarNomeUsuario(usuario.getId(), novoNome, usuario.getTipo());
                                                            System.out.println("Valor alterado com sucesso!");
                                                        } catch (NullPointerException | UsuarioNaoExisteException |
                                                                 UsuarioNaoPermitidoException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 2:
                                                        try {
                                                            System.out.println("Digite a nova senha: ");
                                                            String novaSenha = input.next();
                                                            controladorUsuario.alterarSenhaUsuario(usuario.getEmail(), novaSenha, usuario.getTipo());
                                                            System.out.println("Valor alterado com sucesso!");
                                                        } catch (NullPointerException | UsuarioNaoExisteException |
                                                                 UsuarioNaoPermitidoException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 3:
                                                        try {
                                                            System.out.println("Digite a nova data de nascimento: ");
                                                            String novaData = input.next();
                                                            controladorUsuario.alterarDataAniversario(usuario.getId(), LocalDate.parse(novaData, DateTimeFormatter.ofPattern("dd/MM/yyyy")), usuario.getTipo());
                                                            System.out.println("Valor alterado com sucesso!");
                                                        } catch (NullPointerException | UsuarioNaoExisteException |
                                                                 UsuarioNaoPermitidoException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 4:
                                                        sairEdicao = true;
                                                        break;
                                                    default:
                                                        System.out.println("Opcao invalida!");
                                                        break;
                                                }
                                            }
                                            break;

                                        case 3:
                                            System.out.println("\n\n-- EXCLUIR PERFIL -- ");
                                            System.out.println("1 - Excluir");
                                            System.out.println("2 - Voltar");
                                            escolha = input.nextInt();
                                            switch (escolha) {
                                                case 1:
                                                    try {
                                                        repositorioUsuario.remover(usuario);
                                                        System.out.println("Perfil removido com sucesso!");
                                                        sairTelaPrincipalUsuario = true;
                                                        sairConfiguracoes = true;
                                                        entrarSistema = false;
                                                    } catch (Exception e) {
                                                        System.out.println(e.getMessage());
                                                    }
                                                    break;
                                                case 2:
                                                    continue;
                                                default:
                                                    System.out.println("Opcao invalida!");
                                                    break;
                                            }
                                            break;

                                        case 4:
                                            sairConfiguracoes = true;
                                            break;
                                        default:
                                            System.out.println("Opcao invalida!");
                                            break;
                                    }
                                }
                                break;

                            case 2:
                                boolean sairGerenciamentoReservas = false;
                                while (!sairGerenciamentoReservas) {
                                    System.out.println("GERENCIAMENTO RESERVAS");
                                    System.out.println("1 - Reservas realizadas");
                                    System.out.println("2 - Consulta de periodos disponiveis para reserva");
                                    System.out.println("3 - Voltar");
                                    escolha = input.nextInt();
                                    switch (escolha) {
                                        case 1:
                                            System.out.println("\n\n-- *** -- ");
                                            System.out.println("Minhas reservas");
                                            System.out.println(controladorReservas.listarReservasUsuario(usuario));
                                            System.out.println(" ");
                                            System.out.println("Deseja ir para Tela de Estadia? (s/n)");
                                            char respostaEstadia = input.next().charAt(0);
                                            if (respostaEstadia == 's') {
                                                boolean telaDeEstadia = false;
                                                while (!telaDeEstadia) {
                                                    System.out.println("\n\n-- *** -- ");
                                                    System.out.println("1 - Iniciar Estadia (check-in)");
                                                    System.out.println("2 - Finalizar Estadia (check-out)");
                                                    System.out.println("3 - Prolongar Estadia");
                                                    System.out.println("4 - Ver minha Estadias");
                                                    System.out.println("5 - Voltar");
                                                    escolha = input.nextInt();
                                                    switch (escolha) {
                                                        case 1:
                                                            System.out.println("Informe o id da reserva: ");
                                                            int idReserva = input.nextInt();
                                                            try {
                                                                controladorReservas.checkin(idReserva);
                                                                System.out.println("Estadia iniciada com sucesso!");
                                                            } catch (ReservaNaoExisteException |
                                                                     ReservaJaCanceladaException |
                                                                     ForaPeriodoException |
                                                                     EstadiaJaInicializadaException e) {
                                                                System.out.println(e.getMessage());
                                                            }
                                                            break;
                                                        case 2:
                                                            System.out.println("Informe o id da Estadia: ");
                                                            int idEstadiaFinalizar = input.nextInt();
                                                            try {
                                                                controladorReservas.checkout(idEstadiaFinalizar);
                                                                System.out.println("Estadia finalizada com sucesso!");
                                                            } catch (ReservaNaoExisteException | ReservaJaCanceladaException | EstadiaNaoExisteException e) {
                                                                System.out.println(e.getMessage());
                                                            }
                                                            break;
                                                        case 3:
                                                            break;
                                                        case 4 :
                                                            System.out.println("*** Minhas Estadias ***");
                                                            System.out.println(controladorReservas.listarEstadiasUsuario(usuario));
                                                            System.out.println("-----------------------------------------------------------\n");
                                                            break;
                                                        case 5:
                                                            telaDeEstadia = true;
                                                            break;
                                                    }
                                                }
                                            }
                                            break;

                                        case 2:
                                            boolean sairRealizarReserva = false;
                                            while (!sairRealizarReserva) {
                                                System.out.println("\n\n-- CONSULTA -- ");
                                                System.out.println("1 - Periodos disponiveis para reserva");
                                                System.out.println("2 - Realizar reserva ");
                                                System.out.println("3 - Cancelar Reserva");
                                                System.out.println("4 - Voltar");
                                                escolha = input.nextInt();
                                                switch (escolha) {
                                                    case 1:
                                                        //TODO: verificar o porque nao esta mostrando dos bens disponiveis para reserva
                                                        System.out.print("Informe o id do Bem: ");
                                                        int idBemParaReserva = input.nextInt();
                                                        System.out.print("Informe a data Inicial (dd/MM/yyyy): ");
                                                        String inicioPeriodo = input.next();
                                                        inicioPeriodo += " 00:00";
                                                        System.out.print("Informe a data final (dd/MM/yyyy): ");
                                                        String finalPeriodo = input.next();
                                                        finalPeriodo += " 23:59";
                                                        Bem bemReservaDisponivel = null;
                                                        try {
                                                            bemReservaDisponivel = controladorBens.buscarBemPorId(idBemParaReserva);
                                                        } catch (BemNaoExisteException e) {
                                                            System.out.println(e.getMessage());
                                                        }

                                                        try {
                                                            List<String> reservas = controladorReservas.consultarDisponibilidadeParaReserva(bemReservaDisponivel, LocalDateTime.parse(inicioPeriodo, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), LocalDateTime.parse(finalPeriodo, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), usuario);
                                                            System.out.println("\nPeriodos disponíveis para Reserva: ");
                                                            System.out.println(reservas);

                                                            System.out.println("\nSuas cotas nesse Bem: ");
                                                            System.out.println(controladorBens.listarCotasDeUmUsuarioEmUmBem(usuario, idBemParaReserva));
                                                        } catch (BemNaoExisteException | IllegalArgumentException |
                                                                 NullPointerException e) {
                                                            System.out.println(e.getMessage());
                                                        }

                                                        break;

                                                    case 2:
                                                        System.out.println("Reservar a cota (s/n)");
                                                        char esc = input.next().charAt(0);
                                                        if (esc == 'n') {
                                                            System.out.println("\nInforme os dados para realizar sua reserva");
                                                            System.out.print("Data inicial (dd/MM/yyyy): ");
                                                            String inicioReserva = input.next();
                                                            inicioReserva += " 00:00";
                                                            System.out.print("Data final (dd/MM/yyyy): ");
                                                            String fimReserva = input.next();
                                                            fimReserva += " 23:59";
                                                            System.out.print("id Bem: ");
                                                            int idBemReserva = input.nextInt();

                                                            Bem bemReserva = null;
                                                            try {
                                                                bemReserva = controladorBens.buscarBemPorId(idBemReserva);
                                                            } catch (BemNaoExisteException e) {
                                                                System.out.println(e.getMessage());
                                                            }

                                                            try {
                                                                controladorReservas.criarReserva(LocalDateTime.parse(inicioReserva, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), LocalDateTime.parse(fimReserva, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), usuario, bemReserva);
                                                                //controladorReservas.criarReserva(LocalDate.parse(inicioReserva, DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.parse(fimReserva, DateTimeFormatter.ofPattern("dd/MM/yyyy")), usuario, bemReserva);
                                                            } catch (ReservaJaExisteException |
                                                                     PeriodoJaReservadoException |
                                                                     DadosInsuficientesException |
                                                                     ForaPeriodoException |
                                                                     PeriodoNaoDisponivelParaReservaException e) {
                                                                System.out.println(e.getMessage());
                                                            } catch (CotaJaReservadaException |
                                                                     ReservaNaoExisteException e) {
                                                                throw new RuntimeException(e);
                                                            }
                                                        } else if (esc == 's') {
                                                            System.out.println("Informe o id da cota: ");
                                                            int idCota = input.nextInt();

                                                            try {
                                                                Cota cota = controladorBens.buscarCota(idCota);

                                                                try {
                                                                    controladorReservas.reservaPeriodoCota(cota, usuario);
                                                                } catch (UsuarioNaoPermitidoException |
                                                                        ProprietarioNaoIdentificadoException |
                                                                         DadosInsuficientesException |
                                                                         ReservaJaExisteException |
                                                                         ForaPeriodoException |
                                                                         PeriodoJaReservadoException |
                                                                         PeriodoNaoDisponivelParaReservaException |
                                                                         ReservaNaoExisteException |
                                                                         CotaJaReservadaException e) {
                                                                    System.out.println(e.getMessage());
                                                                }

                                                            } catch (CotaNaoExisteException e) {
                                                                System.out.println(e.getMessage());
                                                            }
                                                        }


                                                        break;
                                                    case 3:
                                                        System.out.println("\nInforme os dados para Cancelar reserva");
                                                        System.out.println("É uma reserva de uma cota? (s/n)");
                                                        char resposta = input.next().charAt(0);
                                                        if (resposta == 'n') {
                                                            System.out.print("id Reserva: ");
                                                            int idReserva = input.nextInt();
                                                            try {
                                                                controladorReservas.cancelarReserva(idReserva, usuario);
                                                            } catch (ReservaNaoExisteException |
                                                                     ReservaJaCanceladaException e) {
                                                                System.out.println(e.getMessage());
                                                            } catch (UsuarioNaoPermitidoException |
                                                                     CotaJaReservadaException |
                                                                     ReservaNaoReembolsavelException e) {
                                                                throw new RuntimeException(e);
                                                            }

                                                        } else if (resposta == 's') {
                                                            System.out.print("id Reserva: ");
                                                            int idReserva = input.nextInt();
                                                            System.out.print("id Cota: ");
                                                            int idCota = input.nextInt();

                                                            try {
                                                                Cota cota = controladorBens.buscarCota(idCota);
                                                                if (cota.getProprietario().equals(usuario)) {

                                                                    try {
                                                                        controladorReservas.liberarPeriodoCota(cota, idReserva, usuario);
                                                                    } catch (DadosInsuficientesException |
                                                                             ReservaNaoExisteException |
                                                                             UsuarioNaoPermitidoException |
                                                                             OperacaoNaoPermitidaException e) {
                                                                        System.out.println(e.getMessage());
                                                                    }

                                                                } else {
                                                                    System.out.println("Você não é o proprietário dessa cota.");
                                                                }

                                                            } catch (CotaNaoExisteException e) {
                                                                System.out.println(e.getMessage());
                                                            }
                                                        }


                                                        break;
                                                    case 4:
                                                        sairRealizarReserva = true;
                                                        break;
                                                }

                                            }
                                        case 3:
                                            sairGerenciamentoReservas = true;
                                            break;
                                    }
                                }
                                break;
                            case 3:
                                boolean sairTelaBens = false;
                                while (!sairTelaBens) {
                                    System.out.println("\n\n-- BENS OFERTADOS -- ");
                                    System.out.println("1 - Verificar Bens Ofertados ");
                                    System.out.println("2 - Comprar Cotas de um Bem ");
                                    System.out.println("3 - Voltar");
                                    escolha = input.nextInt();
                                    switch (escolha) {
                                        case 1:
                                            System.out.println(controladorBens.listarBensOfertados());
                                            break;
                                        case 2:
                                            try {
                                                Venda v1 = controladorVendas.iniciarVenda(usuario.getId());
                                                boolean parar = false;
                                                while (!parar) {
                                                    System.out.println("\n\n-- *** -- ");
                                                    System.out.println("Quantidade de itens no carrinho: " + v1.getCarrinhoDeComprasCotas().size());
                                                    System.out.println("Valor total a ser pago: " + v1.calcularValorTotal());
                                                    System.out.println("1 - adicionar || 2 - remover || 3 - finalizar compra || 4 - Promocoes ");
                                                    int escolhaCompra = input.nextInt();
                                                    int idCota = 0;

                                                    if (escolhaCompra == 1 || escolhaCompra == 2) {
                                                        System.out.print("Informe o id da Cota: ");
                                                        idCota = input.nextInt();
                                                    }


                                                    if (escolhaCompra == 1) {
                                                        try {
                                                            controladorVendas.adicionarCotaCarrinho(idCota, v1);
                                                            System.out.println("Cota adicionada ao carrinho.");
                                                        } catch (CotaNaoExisteException | CotaNaoOfertadaException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                    } else if (escolhaCompra == 2) {
                                                        try {
                                                            controladorVendas.removeCotaCarrinho(idCota, v1);
                                                            System.out.println("Cota removida do carrinho.");
                                                        } catch (CotaNaoExisteException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                    } else if (escolhaCompra == 3) {
                                                        String resultado = controladorVendas.verificarSeUsuarioPossuiDescontos(usuario.getId());
                                                        if (!resultado.isEmpty()) {
                                                            System.out.println(" - Promocoes disponiveis - ");
                                                            System.out.println(resultado);
                                                            System.out.println(resultado);
                                                            System.out.println("Deseja aplicar promocao? (s/n)");
                                                            String resposta = input.next();
                                                            if (resposta.equals("s")) {
                                                                controladorVendas.aplicarDesconto(v1, usuario.getId());
                                                            }
                                                        }

                                                        try {
                                                            String compra = controladorVendas.finalizarCompra(v1);
                                                            System.out.println(compra);
                                                            parar = true;
                                                        } catch (CompraNaoFinalizada e) {
                                                            System.out.println(e.getMessage());
                                                        }

                                                    } else if (escolhaCompra == 4) {
                                                        String resultado = controladorVendas.verificarSeUsuarioPossuiDescontos(usuario.getId());
                                                        if (resultado.isEmpty()) {
                                                            System.out.println("Sem promocoes disponiveis no momento");
                                                        } else {
                                                            System.out.println(resultado);
                                                        }
                                                    }
                                                }
                                            } catch (UsuarioNaoExisteException e) {
                                                System.out.println(e.getMessage());
                                            }


                                            break;
                                        case 3:
                                            sairTelaBens = true;
                                            break;
                                        default:
                                            System.out.println("Opcao invalida!");
                                            break;
                                    }
                                }
                                break;
                            case 4:
                                boolean pararMinhasCotas = false;
                                while (!pararMinhasCotas) {
                                    System.out.println("\n\nMINHAS COTAS");
                                    try {
                                        System.out.println(controladorBens.listarCotasDeUmUsuario(usuario));
                                    } catch (BemNaoExisteException | UsuarioNaoExisteException e) {
                                        System.out.println(e.getMessage());
                                    }


                                    System.out.println("1 - Repassar Direito de Uso:");
                                    System.out.println("2 - Sair");
                                    escolha = input.nextInt();

                                    switch (escolha) {
                                        case 1:
                                            System.out.print("\n Informe de cota: ");
                                            int idCotaTransfetir = input.nextInt();
                                            System.out.println("Informe o cpf do destinatário: ");
                                            Long cpfDestinatario = input.nextLong();

                                            try {
                                                controladorVendas.transferenciaDeDireitos(usuario.getId(), cpfDestinatario, idCotaTransfetir);
                                                System.out.println("Transferencia realizada com sucesso! Deseja gerar comprovante de transferencia? (s/n)");
                                                String resposta = input.next();
                                                if (resposta.equals("s")) {
                                                    System.out.println(controladorVendas.gerarComprovanteTransferencia(usuario.getId(), cpfDestinatario, idCotaTransfetir));
                                                }

                                            } catch (CotaNaoExisteException | UsuarioNaoExisteException |
                                                     TransferenciaInvalidaException e) {
                                                System.out.println(e.getMessage());
                                            }
                                            break;
                                        case 2:
                                            pararMinhasCotas = true;
                                            break;
                                    }
                                }
                                break;
                            case 5:
                                sairTelaPrincipalUsuario = true;
                                break;
                        }
                    } else if (usuario != null && usuario.getTipo().equals(TipoUsuario.ADMINISTRADOR)) {
                        int escolha = 0;
                        System.out.println("\n\n-- TELA ADMINISTRADOR --");
                        System.out.println("1 - Configurações");
                        System.out.println("2 - Gerenciamento de Bens");
                        System.out.println("3 - Relatórios");
                        System.out.println("4 - Sair Tela de Administrador");
                        escolha = input.nextInt();
                        switch (escolha) {
                            case 1:
                                boolean sairConfiguracoes = false;
                                while (!sairConfiguracoes) {
                                    System.out.println("\n\n** Configuracoes **");
                                    System.out.println("1 - Ver perfil");
                                    System.out.println("2 - Editar perfil");
                                    System.out.println("3 - Excluir perfil");
                                    System.out.println("4 - Sair");
                                    escolha = input.nextInt();
                                    switch (escolha) {
                                        case 1:
                                            System.out.println(usuario);
                                            break;
                                        case 2:
                                            boolean sairEdicao = false;
                                            while (!sairEdicao) {
                                                System.out.println("O que deseja editar?");
                                                System.out.println("1 - Nome");
                                                System.out.println("2 - Senha");
                                                System.out.println("3 - Data de nascimento");
                                                System.out.println("4 - Voltar");
                                                escolha = input.nextInt();
                                                switch (escolha) {
                                                    case 1:

                                                        try {
                                                            System.out.println("Digite o novo nome: ");
                                                            String novoNome = input.next();
                                                            controladorUsuario.alterarNomeUsuario(usuario.getId(), novoNome, usuario.getTipo());
                                                            System.out.println("Valor alterado com sucesso!");
                                                        } catch (NullPointerException | UsuarioNaoExisteException |
                                                                 UsuarioNaoPermitidoException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 2:
                                                        try {
                                                            System.out.println("Digite a nova senha: ");
                                                            String novaSenha = input.next();
                                                            controladorUsuario.alterarSenhaUsuario(usuario.getEmail(), novaSenha, usuario.getTipo());
                                                            System.out.println("Valor alterado com sucesso!");
                                                        } catch (NullPointerException | UsuarioNaoExisteException |
                                                                 UsuarioNaoPermitidoException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 3:
                                                        try {
                                                            System.out.println("Digite a nova data de nascimento: ");
                                                            String novaData = input.next();
                                                            controladorUsuario.alterarDataAniversario(usuario.getId(), LocalDate.parse(novaData, DateTimeFormatter.ofPattern("dd/MM/yyyy")), usuario.getTipo());
                                                            System.out.println("Valor alterado com sucesso!");
                                                        } catch (NullPointerException | UsuarioNaoExisteException |
                                                                 UsuarioNaoPermitidoException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 4:
                                                        sairEdicao = true;
                                                        break;
                                                    default:
                                                        System.out.println("Opcao invalida!");
                                                        break;
                                                }
                                            }
                                            break;

                                        case 3:
                                            System.out.println("\n\n-- EXCLUIR PERFIL -- ");
                                            System.out.println("1 - Excluir");
                                            System.out.println("2 - Voltar");
                                            escolha = input.nextInt();
                                            switch (escolha) {
                                                case 1:
                                                    try {
                                                        repositorioUsuario.remover(usuario);
                                                        System.out.println("Perfil removido com sucesso!");
                                                        sairTelaPrincipalUsuario = true;
                                                        sairConfiguracoes = true;
                                                        entrarSistema = false;
                                                    } catch (Exception e) {
                                                        System.out.println(e.getMessage());
                                                    }
                                                    break;
                                                case 2:
                                                    continue;
                                                default:
                                                    System.out.println("Opcao invalida!");
                                                    break;
                                            }
                                            break;

                                        case 4:
                                            sairConfiguracoes = true;
                                            break;
                                        default:
                                            System.out.println("Opcao invalida!");
                                            break;
                                    }
                                }
                                break;
                            case 2:
                                boolean sairGerenciamentoBens = false;
                                while (!sairGerenciamentoBens) {
                                    System.out.println("\n\n** Gerenciamento de Bens **");
                                    System.out.println("1 - Cadastrar Bem");
                                    System.out.println("2 - Listar Bens Cadastrados");
                                    System.out.println("3 - Filtrar Bens Ofertados");
                                    System.out.println("4 - Editar Bem");
                                    System.out.println("5 - Calcular Deslocamento das cotas do Bem");
                                    System.out.println("6 - Sair");
                                    escolha = input.nextInt();
                                    switch (escolha) {
                                        case 1:
                                            System.out.println("\n\n-- CADASTRO DE BEM --");
                                            System.out.print("Informe um id para o Bem:");
                                            int idBem = input.nextInt();
                                            input.nextLine();
                                            System.out.print("Nome: ");
                                            String nome = input.nextLine();
                                            System.out.print("Descrição: ");
                                            String descricao = input.nextLine();
                                            System.out.print("Localização: ");
                                            String localizacao = input.nextLine();
                                            System.out.print("Capacidade de pessoas: ");
                                            int capacidade = input.nextInt();
                                            input.nextLine();
                                            System.out.print("Informe a data Inicial das cotas (dd/MM/yyyy): ");
                                            String dataInicial = input.next();
                                            dataInicial += " 00:00";
                                            System.out.print("Quantidade de cotas: ");
                                            int quantidadeCotas = input.nextInt();
                                            System.out.print("Preço de uma Cota R$: ");
                                            double precoCota = input.nextDouble();
                                            try {
                                                controladorBens.cadastrar(idBem, nome, descricao, localizacao, capacidade, usuario, LocalDateTime.parse(dataInicial, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), quantidadeCotas, precoCota);
                                                System.out.println("Bem cadastrado com Sucesso!");
                                            } catch (BemNaoExisteException | UsuarioNaoPermitidoException |
                                                     QuantidadeDeCotasExcedidasException | BemJaExisteException |
                                                     UsuarioNaoExisteException e) {
                                                System.out.println(e.getMessage());
                                            }
                                            break;
                                        case 2:
                                            System.out.println("\n\n-- LISTA DE BEM CADASTRADOS --");
                                            System.out.println(controladorBens.listarBensUsuario(usuario));
                                            break;
                                        case 3:
                                            System.out.println("\n\n-- LISTA DE BEM OFERTADOS --");
                                            System.out.println(controladorBens.listarBensOfertadosUsuario(usuario));
                                            break;

                                        case 4:
                                            boolean sairEdicaoBem = false;
                                            while (!sairEdicaoBem) {
                                                System.out.println("O que deseja editar?");
                                                System.out.println("1 - Nome");
                                                System.out.println("2 - Descrição");
                                                System.out.println("3 - Localização");
                                                System.out.println("4 - Capacidade");
                                                System.out.println("5 - Ofertar");
                                                System.out.println("6 - Excluir");
                                                System.out.println("7 - Voltar");
                                                escolha = input.nextInt();
                                                switch (escolha) {
                                                    case 1:
                                                        try {
                                                            System.out.print("Digite o id: ");
                                                            int idBemOfertar = input.nextInt();
                                                            System.out.println("Digite o novo nome: ");
                                                            String novoNome = input.next();
                                                            controladorBens.alterarNomeBem(idBemOfertar, novoNome);
                                                            System.out.println("Valor alterado com sucesso!");
                                                        } catch (NullPointerException | BemNaoExisteException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 2:
                                                        try {
                                                            System.out.print("Digite o id: ");
                                                            int idBemOfertar = input.nextInt();
                                                            System.out.println("Digite a nova descricao: ");
                                                            String novaDescricao = input.next();
                                                            controladorBens.alterarDescricaoBem(idBemOfertar, novaDescricao);
                                                            System.out.println("Valor alterado com sucesso!");
                                                        } catch (NullPointerException | BemNaoExisteException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 3:
                                                        try {
                                                            System.out.print("Digite o id: ");
                                                            int idBemOfertar = input.nextInt();
                                                            System.out.println("Digite a nova localizacao: ");
                                                            String novaLocalizacao = input.next();
                                                            controladorBens.alterarLocalizacaoBem(idBemOfertar, novaLocalizacao);
                                                            System.out.println("Valor alterado com sucesso!");
                                                        } catch (NullPointerException | BemNaoExisteException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 4:
                                                        try {
                                                            System.out.print("Digite o id: ");
                                                            int idBemOfertar = input.nextInt();
                                                            System.out.println("Digite a nova capacidade: ");
                                                            int novaCapacidade = input.nextInt();
                                                            controladorBens.alterarCapacidadeBem(idBemOfertar, novaCapacidade);
                                                            System.out.println("Valor alterado com sucesso!");
                                                        } catch (IllegalArgumentException | BemNaoExisteException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 5:
                                                        try {
                                                            System.out.print("Digite o id: ");
                                                            int idBemOfertar = input.nextInt();
                                                            controladorBens.ofertarBem(idBemOfertar);
                                                            System.out.println("Bem ofertado com sucesso!");
                                                        } catch (BemNaoExisteException e) {
                                                            System.out.println(e.getMessage());
                                                        }

                                                        break;
                                                    case 6:
                                                        try {
                                                            System.out.print("Digite o id: ");
                                                            int bemASerRemovido = input.nextInt();
                                                            controladorBens.remover(bemASerRemovido);
                                                            System.out.println("Bem removido com sucesso!");
                                                        } catch (BemNaoExisteException | IllegalAccessException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 7:
                                                        sairEdicaoBem = true;
                                                        break;
                                                }
                                            }
                                            break;

                                        case 5:
                                            System.out.println("\n\n-- CÁLCULO DE DESLOCAMENTO DE COTAS --");
                                            System.out.print("Informe o ID do bem: ");
                                            int id = input.nextInt();
                                            System.out.println("Data para Deslocamento (dd/MM/yyyy): ");
                                            String data = input.next();
                                            data += " 23:59";
                                            try {
                                                ArrayList<Cota> cotas = controladorBens.calcularDeslocamentoDasCotas(id, LocalDateTime.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                                                for (Cota cota : cotas) {
                                                    System.out.println(cota + "\n");
                                                }
                                            } catch (BemNaoExisteException e) {
                                                System.out.println(e.getMessage());
                                            }

                                            break;
                                        case 6:
                                            sairGerenciamentoBens = true;
                                            break;
                                        default:
                                            System.out.println("Opcao invalida");
                                    }

                                }
                                break;
                            case 3:
                                break;
                            case 4:
                                sairTelaPrincipalAdministrador = true;
                                break;

                        }

                    }
                } while (!sairTelaPrincipalUsuario && !sairTelaPrincipalAdministrador);

                System.out.println("\n\nDeseja logar novamente? (s/n) ");
                char decisao = input.next().charAt(0);
                if (decisao == 's') {
                    entrarSistema = false;
                    // sairSistema = false;
                    sairTelaPrincipalAdministrador = false;
                    sairTelaPrincipalUsuario = false;

                } else if (decisao == 'n') {
                    finalizarPrograma = true;
                    break;
                }


            }


        }
    }
}