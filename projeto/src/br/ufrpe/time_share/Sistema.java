package br.ufrpe.time_share;

import br.ufrpe.time_share.dados.IRepositorioBens;
import br.ufrpe.time_share.dados.IRepositorioUsuario;
import br.ufrpe.time_share.dados.RepositorioBens;
import br.ufrpe.time_share.dados.RepositorioUsuarios;
import br.ufrpe.time_share.excecoes.SenhaInvalidaException;
import br.ufrpe.time_share.excecoes.UsuarioNaoExisteException;
import br.ufrpe.time_share.excecoes.UsuarioNaoPermitidoException;
import br.ufrpe.time_share.negocio.*;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Sistema {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        //INICIALIZAR REPOSITORIOS
        IRepositorioUsuario repositorioUsuario = RepositorioUsuarios.getInstance();
        IRepositorioBens repositorioBens = RepositorioBens.getInstancia();

        //INICIALIZANDO CONTROLADORES
        ControladorLogin controladorLogin = new ControladorLogin(repositorioUsuario);
        ControladorUsuarioGeral controladorUsuario = new ControladorUsuarioGeral(repositorioUsuario);

        boolean entrarSistema = false;
        boolean sairSistema = false;
        boolean sairTelaPrincipal = false;

        Usuario usuario = null; //Variavel que vai armazenar o usuario apos login
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
                            System.out.println("Tente novamente.");
                            // Aqui você pode decidir se quer continuar tentando fazer login ou sair.
                            System.out.println("Deseja tentar novamente? (s/n)");
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
                                        System.out.println(repositorioUsuario.buscarUsuarioPorEmail(usuario.getEmail()));
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
                                                        controladorUsuario.alterarDataAniversario(usuario.getEmail(), LocalDate.parse(novaData, DateTimeFormatter.ofPattern("dd/MM/yyyy")), usuario.getTipo());
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
                                                    sairTelaPrincipal = true;
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

                        /////////////////////////////////////////////////////////////////////////////////////////////////
                        case 2:


                    }
                } else if (usuario != null && usuario.getTipo().equals(TipoUsuario.ADMINISTRADOR)) {

                } else {
//                    throw new UsuarioNaoExisteException("Usuario invalido");
                }


                System.out.println("\n\nDeseja sair do sistema? (s/n) ");
                sairSistema = input.next().equals("s");
                if (!sairSistema) {
                    entrarSistema = false;
                }
            } while (!sairTelaPrincipal);

        }


    }
}