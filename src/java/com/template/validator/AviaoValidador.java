package com.template.validator;

import com.template.util.DialogUtil;
import java.util.ArrayList;
import java.util.List;

public class AviaoValidador implements IAviaoValidador {

    @Override
    public boolean validarAviao(String modelo, String fabricante, String capacidade, String autonomia, String ano) {
        return validarModelo(modelo) &&
                validarFabricante(fabricante) &&
                validarCapacidade(capacidade) &&
                validarAutonomia(autonomia) &&
                validarAno(ano);
    }

    @Override
    public boolean validarModelo(String modelo) {
        Validador<String> validador = new CampoObrigatorioValidador("Modelo", modelo);
        if (!validador.validar(validador.getValor())) {
            DialogUtil.showWarning(validador.getMensagemErro());
            return false;
        }
        return true;
    }

    @Override
    public boolean validarFabricante(String fabricante) {
        Validador<String> validador = new CampoObrigatorioValidador("Fabricante", fabricante);
        if (!validador.validar(validador.getValor())) {
            DialogUtil.showWarning(validador.getMensagemErro());
            return false;
        }
        return true;
    }

    @Override
    public boolean validarCapacidade(String capacidade) {
        List<Validador<String>> validadores = new ArrayList<>();
        validadores.add(new CampoObrigatorioValidador("Capacidade", capacidade));
        validadores.add(new NumeroPositivoValidador(capacidade, "Capacidade"));

        for (Validador<String> validador : validadores) {
            if (!validador.validar(validador.getValor())) {
                DialogUtil.showWarning(validador.getMensagemErro());
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validarAutonomia(String autonomia) {
        List<Validador<String>> validadores = new ArrayList<>();
        validadores.add(new CampoObrigatorioValidador("Autonomia", autonomia));
        validadores.add(new NumeroPositivoValidador(autonomia, "Autonomia"));

        for (Validador<String> validador : validadores) {
            if (!validador.validar(validador.getValor())) {
                DialogUtil.showWarning(validador.getMensagemErro());
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validarAno(String ano) {
        List<Validador<String>> validadores = new ArrayList<>();
        validadores.add(new CampoObrigatorioValidador("Ano", ano));
        validadores.add(new AnoValidador(ano));

        for (Validador<String> validador : validadores) {
            if (!validador.validar(validador.getValor())) {
                DialogUtil.showWarning(validador.getMensagemErro());
                return false;
            }
        }
        return true;
    }
}