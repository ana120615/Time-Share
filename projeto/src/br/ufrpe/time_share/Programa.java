package br.ufrpe.time_share;

import br.ufrpe.time_share.dados.IRepositorioUsuario;
import br.ufrpe.time_share.dados.RepositorioUsuarios;
import br.ufrpe.time_share.excecoes.DadosInsuficientesException;
import br.ufrpe.time_share.excecoes.UsuarioJaExisteException;
import br.ufrpe.time_share.excecoes.UsuarioNaoPermitidoException;
import br.ufrpe.time_share.negocio.ControladorUsuarioGeral;
import br.ufrpe.time_share.negocio.beans.*;

import java.time.LocalDate;


public class Programa {
    public static void main(String[] args) throws UsuarioNaoPermitidoException, UsuarioJaExisteException, DadosInsuficientesException {
        IRepositorioUsuario repositorioUsuario = RepositorioUsuarios.getInstance();
        ControladorUsuarioGeral controlador = new ControladorUsuarioGeral(repositorioUsuario);

        controlador.cadastrar("11111111111", "Samara", "samara@gmail.com", "ss", LocalDate.of(2005,02,01), TipoUsuario.fromValor(1));
        controlador.cadastrar("11111191111", "Samara", "samar@gmail.com", "ss", LocalDate.of(2005,02,01), TipoUsuario.fromValor(2));

        Usuario usuario = new Usuario("132644541", "Senha1", TipoUsuario.COMUM);
        Usuario usuario2 = new Usuario("65156155", "Senha9542", TipoUsuario.COMUM);
        Usuario usuario3 = new Usuario("45165206", "S@NHASUF", TipoUsuario.COMUM);
        Usuario usuario4 = new Usuario("511006515", "#afjnja", TipoUsuario.ADMINISTRADOR);
        Usuario usuario5 = new Usuario("471652206", "fafeof", TipoUsuario.ADMINISTRADOR);



//        IRepositorioUsuario repo = new RepositorioUsuarios();
//        repo.cadastrar(usuario);
//        repo.cadastrar(usuario2);
//        repo.cadastrar(usuario3);
//        repo.cadastrar(usuario4);
//        repo.cadastrar(usuario5);
//
//        System.out.println("LISTANDO USUÁRIOS COMUMS");
//        System.out.println(repo.listarUsuarioComum());
//
//        System.out.println("\n\nLISTANDO USUÁROS ADMINISTRADORES");
//        System.out.println(repo.listarUsuarioAdm());

    }
}