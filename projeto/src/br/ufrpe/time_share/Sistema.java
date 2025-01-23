package br.ufrpe.time_share;

import br.ufrpe.time_share.dados.*;
import br.ufrpe.time_share.excecoes.*;
import br.ufrpe.time_share.negocio.*;
import br.ufrpe.time_share.negocio.beans.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Sistema {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        //INICIALIZAR REPOSITORIOS
        IRepositorioUsuario repositorioUsuario = RepositorioUsuarios.getInstance();
        IRepositorioBens repositorioBens = RepositorioBens.getInstancia();
        IRepositorioReservas repositorioReservas = RepositorioReservas.getInstance();

        //INICIALIZANDO CONTROLADORES
        ControladorLogin controladorLogin = new ControladorLogin(repositorioUsuario);
        ControladorUsuarioGeral controladorUsuario = new ControladorUsuarioGeral(repositorioUsuario);
        ControladorBens controladorBens = new ControladorBens(repositorioBens);
        ControladorReservas controladorReservas = new ControladorReservas(repositorioReservas);
        ControladorVendas controladorVendas = new ControladorVendas();

        Usuario usuario = null; //Variavel que vai armazenar o usuario apos login

        // Cadastrar Usuário Comum para facilitar na entrada do Sistema
        try {
            controladorUsuario.cadastrar("12345678901", "Caua", "caua@gmail.com",
                    "senha123", LocalDate.of(2005, 12, 12), TipoUsuario.ADMINISTRADOR);
        } catch (UsuarioJaExisteException | DadosInsuficientesException | UsuarioNaoPermitidoException e) {
            System.out.println(e.getMessage());
        }


        boolean entrarSistema = false;
        boolean sairSistema = false;
        boolean sairTelaPrincipalAdministrador = false;
        boolean sairTelaPrincipalUsuario = false;
        boolean finalizarPrograma = false;


        while (!finalizarPrograma) {
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
                                String nome, cpf, email, senha, dataNascimento;
                                System.out.println("Informe o nome: ");
                                nome = input.next();  // Usando next
                                System.out.println("Informe o cpf: ");
                                cpf = input.next();  // Usando next
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
                        int escolha = 0;
                        System.out.println("\n\n-- TELA USUARIO --");
                        System.out.println("1 - Configuracoes");
                        System.out.println("2 - Gerenciamento de Reservas");
                        System.out.println("3 - Bens Ofertados");
                        System.out.println("4 - Minhas Cotas");
                        System.out.println("5 - Sair tela de Usuário");
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
                                                        escolha = 0;
                                                        try {
                                                            System.out.println("Digite o novo nome: ");
                                                            String novoNome = input.next();
                                                            controladorUsuario.alterarNomeUsuario(usuario.getCpf(), novoNome, usuario.getTipo());
                                                        } catch (NullPointerException | UsuarioNaoExisteException |
                                                                 UsuarioNaoPermitidoException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 2:
                                                        escolha = 0;
                                                        try {
                                                            System.out.println("Digite a nova senha: ");
                                                            String novaSenha = input.next();
                                                            controladorUsuario.alterarSenhaUsuario(usuario.getEmail(), novaSenha, usuario.getTipo());
                                                        } catch (NullPointerException | UsuarioNaoExisteException |
                                                                 UsuarioNaoPermitidoException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 3:
                                                        escolha = 0;
                                                        try {
                                                            System.out.println("Digite a nova data de nascimento: ");
                                                            String novaData = input.next();
                                                            controladorUsuario.alterarDataAniversario(usuario.getCpf(), LocalDate.parse(novaData, DateTimeFormatter.ofPattern("dd/MM/yyyy")), usuario.getTipo());
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
                                            System.out.println("1 - Ver reservas");
                                            System.out.println("2 - Voltar");
                                            escolha = input.nextInt();
                                            switch (escolha) {
                                                case 1:
                                                    System.out.println("Printar Reservas");
                                                    break;
                                                case 2:
                                                    break;
                                            }
                                            break;
                                        case 2:
                                            boolean sairRealizarReserva = false;
                                            while (!sairRealizarReserva) {
                                                System.out.println("\n\n-- CONSULTA -- ");
                                                System.out.println("1 - Periodos disponiveis para reserva ");
                                                System.out.println("2 - Realizar reserva ");
                                                System.out.println("3 - Cancelar Reserva");
                                                System.out.println("4 - Voltar");
                                                escolha = input.nextInt();
                                                switch (escolha) {
                                                    case 1:

                                                        System.out.println("=== Bens disponíveis com suas Cotas para reserva ===");

                                                        System.out.println("\n=== Bens disponíveis com todas as Cotas ===");
                                                        break;

                                                    case 2:
                                                        System.out.println("\nInforme os dados para Criar reserva");

                                                        break;
                                                    case 3:
                                                        System.out.println("\nInforme os dados para Cancelar reserva");

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
                                                Venda v1 = controladorVendas.iniciarVenda(usuario.getCpf());
                                                boolean parar = false;
                                                while (!parar) {

                                                    System.out.println("1 - adicionar || 2 - remover || 3 - finalizar compra ");
                                                    int escolhaCompra = input.nextInt();
                                                    int idCota = 0;

                                                    if (escolhaCompra == 1 || escolhaCompra == 2) {
                                                        System.out.print("Informe o id da Cota: ");
                                                        idCota = input.nextInt();
                                                    }


                                                    if (escolhaCompra == 1) {
                                                        try {
                                                            controladorVendas.adicionarCotaCarrinho(idCota, v1);
                                                        } catch (CotaNaoExisteException | CotaNaoOfertadaException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                    } else if (escolhaCompra == 2) {
                                                        try {
                                                            controladorVendas.removeCotaCarrinho(idCota, v1);
                                                        } catch (CotaNaoExisteException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                    } else if (escolhaCompra == 3) {
                                                        String compra = controladorVendas.finalizarCompra(v1);
                                                        System.out.println(compra);
                                                        parar = true;
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
                                        System.out.println(controladorBens.listarCotasDeUmUsuario(usuario.getCpf()));
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
                                            String cpfDestinatario = input.next();

                                            try {
                                                controladorVendas.transferenciaDeDireitos(usuario.getCpf(), cpfDestinatario, idCotaTransfetir);
                                            } catch (CotaNaoExisteException | UsuarioNaoExisteException e) {
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
                                                        escolha = 0;
                                                        try {
                                                            System.out.println("Digite o novo nome: ");
                                                            String novoNome = input.next();
                                                            controladorUsuario.alterarNomeUsuario(usuario.getCpf(), novoNome, usuario.getTipo());
                                                        } catch (NullPointerException | UsuarioNaoExisteException |
                                                                 UsuarioNaoPermitidoException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 2:
                                                        escolha = 0;
                                                        try {
                                                            System.out.println("Digite a nova senha: ");
                                                            String novaSenha = input.next();
                                                            controladorUsuario.alterarSenhaUsuario(usuario.getEmail(), novaSenha, usuario.getTipo());
                                                        } catch (NullPointerException | UsuarioNaoExisteException |
                                                                 UsuarioNaoPermitidoException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 3:
                                                        escolha = 0;
                                                        try {
                                                            System.out.println("Digite a nova data de nascimento: ");
                                                            String novaData = input.next();
                                                            controladorUsuario.alterarDataAniversario(usuario.getCpf(), LocalDate.parse(novaData, DateTimeFormatter.ofPattern("dd/MM/yyyy")), usuario.getTipo());
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
                                            System.out.print("Nome: ");
                                            String nome = input.next();
                                            System.out.print("Descrição: ");
                                            String descricao = input.next();
                                            System.out.print("Localização: ");
                                            String localizacao = input.next();
                                            System.out.print("Capacidade de pessoas: ");
                                            int capacidade = input.nextInt();
                                            System.out.print("Informe a data Inicial das cotas (dd/MM/yyyy): ");
                                            String dataInicial = input.next();
                                            dataInicial += " 00:00";
                                            System.out.print("Quantidade de cotas: ");
                                            int quantidadeCotas = input.nextInt();
                                            System.out.print("Preço de uma Cota R$: ");
                                            double precoCota = input.nextDouble();
                                            try {
                                                controladorBens.cadastrar(idBem, nome, descricao, localizacao, capacidade, usuario.getCpf(), LocalDateTime.parse(dataInicial, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), quantidadeCotas, precoCota);
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
                                                        escolha = 0;
                                                        try {
                                                            System.out.print("Digite o id: ");
                                                            int idBemOfertar = input.nextInt();
                                                            System.out.println("Digite o novo nome: ");
                                                            String novoNome = input.next();
                                                            controladorBens.alterarNomeBem(idBemOfertar, novoNome);
                                                        } catch (NullPointerException | BemNaoExisteException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 2:
                                                        escolha = 0;
                                                        try {
                                                            System.out.print("Digite o id: ");
                                                            int idBemOfertar = input.nextInt();
                                                            System.out.println("Digite a nova descricao: ");
                                                            String novaDescricao = input.next();
                                                            controladorBens.alterarDescricaoBem(idBemOfertar, novaDescricao);
                                                        } catch (NullPointerException | BemNaoExisteException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 3:
                                                        escolha = 0;
                                                        try {
                                                            System.out.print("Digite o id: ");
                                                            int idBemOfertar = input.nextInt();
                                                            System.out.println("Digite a nova localizacao: ");
                                                            String novaLocalizacao = input.next();
                                                            controladorBens.alterarLocalizacaoBem(idBemOfertar, novaLocalizacao);
                                                        } catch (NullPointerException | BemNaoExisteException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 4:
                                                        escolha = 0;
                                                        try {
                                                            System.out.print("Digite o id: ");
                                                            int idBemOfertar = input.nextInt();
                                                            System.out.println("Digite a nova capacidade: ");
                                                            int novaCapacidade = input.nextInt();
                                                            controladorBens.alterarCapacidadeBem(idBemOfertar, novaCapacidade);
                                                        } catch (IllegalArgumentException | BemNaoExisteException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                    case 5:
                                                        try {
                                                            System.out.print("Digite o id: ");
                                                            int idBemOfertar = input.nextInt();
                                                            controladorBens.ofertarBem(idBemOfertar);
                                                        } catch (BemNaoExisteException e) {
                                                            System.out.println(e.getMessage());
                                                        }

                                                        break;
                                                    case 6:
                                                        try {
                                                            System.out.print("Digite o id: ");
                                                            int idBemOfertar = input.nextInt();
                                                            controladorBens.remover(idBemOfertar);
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
                                            System.out.println("Ano para o qual quer deslocar: ");
                                            int ano = input.nextInt();

                                            try {
                                                ArrayList<Cota> cotas = controladorBens.calcularDeslocamentoDasCotas(id, ano);
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

                } else {
                    finalizarPrograma = true;
                }


            }


        }
    }
}