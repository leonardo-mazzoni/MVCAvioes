package com.template.validator;

import com.template.util.DialogUtil;
import java.util.ArrayList;
import java.util.List;

public class AviaoValidador implements IAviaoValidador {

    @Override
    public boolean validarAviao(String modelo, String fabricante, String capacidade, String autonomia, String ano) {
        // Lista genérica de validadores conforme Slide 18 da aula
        List<Validador<String>> validadores = new ArrayList<>();

        // Adicionando validadores de campos obrigatórios
        validadores.add(new CamposObrigatoriosValidador("Modelo", modelo));
        validadores.add(new CamposObrigatoriosValidador("Fabricante", fabricante));
        validadores.add(new CamposObrigatoriosValidador("Capacidade", capacidade));
        validadores.add(new CamposObrigatoriosValidador("Autonomia", autonomia));
        validadores.add(new CamposObrigatoriosValidador("Ano de Fabricação", ano));

        // Adicionando validadores de regras de negócio específicas
        validadores.add(new NumeroPositivoValidador(capacidade, "Capacidade"));
        validadores.add(new NumeroPositivoValidador(autonomia, "Autonomia"));
        validadores.add(new AnoValidador(ano));

        // Estrutura foreach obrigatória percorrendo a lista de validadores
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