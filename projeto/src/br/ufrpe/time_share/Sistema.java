package br.ufrpe.time_share;

import br.ufrpe.time_share.dados.IRepositorioUsuario;
import br.ufrpe.time_share.dados.RepositorioUsuarios;
import br.ufrpe.time_share.excecoes.SenhaInvalidaException;
import br.ufrpe.time_share.excecoes.UsuarioNaoExisteException;
import br.ufrpe.time_share.negocio.*;
import br.ufrpe.time_share.negocio.beans.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Sistema {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        IRepositorioUsuario repositorioUsuario = RepositorioUsuarios.getInstance();
        ControladorLogin controladorLogin = new ControladorLogin(repositorioUsuario);
        ControladorUsuarioComum controladorUsuarioComum = new ControladorUsuarioComum(repositorioUsuario);
        ControladorAdm controladorAdm = new ControladorAdm(repositorioUsuario);

        boolean sairLogin = false;
        boolean entrarSistema = false;

        while (!entrarSistema) {
            System.out.println("BEM-VINDO AO FELX-SHARE");
            System.out.println("Ja possui cadastro?(s/n)");
            boolean escolha = input.next().equalsIgnoreCase("s");
            if(escolha) {
                while (!sairLogin) {
                    try {
                        System.out.println("-- LOGIN --");
                        String email, password;
                        System.out.println("Informe o email: ");
                        email = input.nextLine();
                        input.next();
                        System.out.println("Informe a Senha: ");
                        password = input.nextLine();
                        input.next();
                        Usuario usuario = controladorLogin.efetuarLogin(email, password);
                        sairLogin = true;
                        entrarSistema = true;
                    }
                    catch (UsuarioNaoExisteException | SenhaInvalidaException e) {
                        System.out.println(e.getMessage());
                        sairLogin = true;
                    }
                }
            }
            else{
                boolean sairCadastro = false;
                while (!sairCadastro) {
                    try {
                        System.out.println("\n-- CADASTRO USUARIO --");
                        String nome, cpf, email, senha, dataNascimento;
                        System.out.println("Informe o nome: ");
                        nome = input.nextLine();
                        input.next();
                        System.out.println("Informe o cpf: ");
                        cpf = input.nextLine();
                        input.next();
                        System.out.println("Informe a Senha: ");
                        senha = input.nextLine();
                        input.next();
                        System.out.println("Informe o Email: ");
                        email = input.nextLine();
                        input.next();
                        System.out.println("Informe a data de nascimento: ");
                        dataNascimento = input.nextLine();
                        input.next();
                        System.out.println("\nEscolha o tipo de usuario: ");
                        System.out.println("1 - COMUM");
                        System.out.println("2 - ADM");
                        int tipoUsuario = input.nextInt();

                        if(tipoUsuario == 1) {
                            controladorUsuarioComum.cadastrar(cpf, nome, email, senha, LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            sairCadastro = true;
                        }
                        else if(tipoUsuario == 2) {
                            controladorAdm.cadastrar(cpf, nome, email, senha, LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            sairCadastro = true;
                        }
                        else{
                            System.out.println("Opcao invalida!");
                        }
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
