package com.template.validator;

import com.template.util.DialogUtil;
import java.util.ArrayList;
import java.util.List;

public class AviaoValidador implements IAviaoValidador {

    @Override
    public boolean validarAviao(String modelo, String fabricante, String capacidade, String autonomia, String ano) {
        // Junta todos os validadores em uma lista polimorfica
        List<Validador<String>> validadores = new ArrayList<>();

        // Campos obrigatorios (nao podem ficar em branco)
        validadores.add(new CamposObrigatoriosValidador("Modelo", modelo));
        validadores.add(new CamposObrigatoriosValidador("Fabricante", fabricante));
        validadores.add(new CamposObrigatoriosValidador("Capacidade", capacidade));
        validadores.add(new CamposObrigatoriosValidador("Autonomia", autonomia));
        validadores.add(new CamposObrigatoriosValidador("Ano de Fabricação", ano));

        // Regras de negocio especificas
        validadores.add(new ApenasTextoValidador(fabricante, "Fabricante"));
        validadores.add(new NumeroPositivoValidador(capacidade, "Capacidade"));
        validadores.add(new NumeroPositivoValidador(autonomia, "Autonomia"));
        validadores.add(new AnoValidador(ano));

        // Loop foreach: se algum falhar, avisa na tela e para na hora (fail-fast)
        for (Validador<String> validador : validadores) {
            if (!validador.validar(validador.getValor())) {
                DialogUtil.showWarning(validador.getMensagemErro());
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validarModelo(String modelo) {
        List<Validador<String>> validadores = new ArrayList<>();
        validadores.add(new CamposObrigatoriosValidador("Modelo", modelo));
        return executarValidacoes(validadores);
    }

    @Override
    public boolean validarFabricante(String fabricante) {
        List<Validador<String>> validadores = new ArrayList<>();
        validadores.add(new CamposObrigatoriosValidador("Fabricante", fabricante));
        validadores.add(new ApenasTextoValidador(fabricante, "Fabricante"));
        return executarValidacoes(validadores);
    }

    @Override
    public boolean validarCapacidade(String capacidade) {
        List<Validador<String>> validadores = new ArrayList<>();
        validadores.add(new CamposObrigatoriosValidador("Capacidade", capacidade));
        validadores.add(new NumeroPositivoValidador(capacidade, "Capacidade"));
        return executarValidacoes(validadores);
    }

    @Override
    public boolean validarAutonomia(String autonomia) {
        List<Validador<String>> validadores = new ArrayList<>();
        validadores.add(new CamposObrigatoriosValidador("Autonomia", autonomia));
        validadores.add(new NumeroPositivoValidador(autonomia, "Autonomia"));
        return executarValidacoes(validadores);
    }

    @Override
    public boolean validarAno(String ano) {
        List<Validador<String>> validadores = new ArrayList<>();
        validadores.add(new CamposObrigatoriosValidador("Ano de Fabricação", ano));
        validadores.add(new AnoValidador(ano));
        return executarValidacoes(validadores);
    }

    private boolean executarValidacoes(List<Validador<String>> validadores) {
        for (Validador<String> validador : validadores) {
            if (!validador.validar(validador.getValor())) {
                DialogUtil.showWarning(validador.getMensagemErro());
                return false;
            }
        }
        return true;
    }
}