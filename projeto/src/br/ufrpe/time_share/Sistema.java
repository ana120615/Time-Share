package br.ufrpe.time_share;

import br.ufrpe.time_share.dados.IRepositorioBens;
import br.ufrpe.time_share.dados.IRepositorioUsuario;
import br.ufrpe.time_share.dados.RepositorioBens;
import br.ufrpe.time_share.dados.RepositorioUsuarios;
import br.ufrpe.time_share.excecoes.SenhaInvalidaException;
import br.ufrpe.time_share.excecoes.UsuarioNaoExisteException;
import br.ufrpe.time_share.negocio.*;
import br.ufrpe.time_share.negocio.beans.TipoUsuario;
import br.ufrpe.time_share.negocio.beans.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Sistema {
    public static void main(String[] args) throws UsuarioNaoExisteException {
        Scanner input = new Scanner(System.in);

        //INICIALIZAR REPOSITORIOS
        IRepositorioUsuario repositorioUsuario = RepositorioUsuarios.getInstance();
        IRepositorioBens repositorioBens = RepositorioBens.getInstancia();

        //INICIALIZANDO CONTROLADORES
        ControladorLogin controladorLogin = new ControladorLogin(repositorioUsuario);
        ControladorUsuarioGeral controladorUsuario = new ControladorUsuarioGeral(repositorioUsuario);

        boolean entrarSistema = false;
        boolean sairSistema = false;

        Usuario usuario = null; //Variavel que vai armazenar o usuario apos login

        while (!sairSistema) {
            while (!entrarSistema) {
                boolean sairLogin = false;
                System.out.println("\nBEM-VINDO AO FELX-SHARE");
                System.out.println("Ja possui cadastro?(1 - sim/ 2 - nao)");
                int escolha = input.nextInt();
                if (escolha == 1) {
                    while (!sairLogin) {
                        try {
                            System.out.println("-- LOGIN --");
                            String email, password;
                            System.out.println("Informe o email: ");
                            email = input.next();
                            System.out.println("Informe a Senha: ");
                            password = input.next();
                            usuario = controladorLogin.efetuarLogin(email, password);
                            sairLogin = true;
                            entrarSistema = true;
                        } catch (UsuarioNaoExisteException | SenhaInvalidaException e) {
                            System.out.println(e.getMessage());
                            sairLogin = true;
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
                            System.out.println("1 - COMUM || 2 - ADM");
                            int tipoUsuario = input.nextInt();  // Usando nextInt para o número


                            if (tipoUsuario == 1) {
                                controladorUsuario.cadastrar(cpf, nome, email, senha, LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy")), TipoUsuario.fromValor(1));
                            } else if (tipoUsuario == 2) {
                                controladorUsuario.cadastrar(cpf, nome, email, senha, LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy")), TipoUsuario.fromValor(2));
                            } else {
                                System.out.println("Opcao invalida!");
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        } finally {
                            sairCadastro = true;
                        }
                    }
                }
            }

            boolean sairTelaPrincipal = false;
            while (!sairTelaPrincipal) {
                if (usuario != null && usuario.getTipo().equals(TipoUsuario.COMUM)) {

                } else if (usuario != null && usuario.getTipo().equals(TipoUsuario.ADMINISTRADOR)) {

                } else {
                    throw new UsuarioNaoExisteException("Usuario invalido");
                }
            }


            System.out.println("\n\nDeseja sair do sistema? (s/n) ");
            sairSistema = input.next().equals("s");
            if (!sairSistema) {
                entrarSistema = false;
            }
        }
    }
}

