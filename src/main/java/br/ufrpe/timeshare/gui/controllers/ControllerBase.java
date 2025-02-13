package br.ufrpe.timeshare.gui.controllers;

import br.ufrpe.timeshare.excecoes.DadosInsuficientesException;

public interface ControllerBase {
    void receiveData(Object data) throws DadosInsuficientesException;
}
