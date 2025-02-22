package br.ufrpe.timeshare.dados;

import br.ufrpe.timeshare.negocio.beans.Venda;

public interface IRepositorioVenda extends IRepositorio<Venda> {
    Venda buscarVendaUsuario (long cpfUsuario);
}
