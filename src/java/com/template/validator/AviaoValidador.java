package com.template.validator;

import com.template.util.DialogUtil;
import java.util.ArrayList;
import java.util.List;

public class AviaoValidador {


    public static  boolean validarCamposFormulario(String modelo, String fabricante, String capacidade, String autonomia, String ano) {

        // Lista de validadores que serão aplicados sequencialmente
        List<Validador<String>> validadores = new ArrayList<>();

        // Adicionando os validadores de campos obrigatórios
        validadores.add(new CampoObrigatorioValidador(modelo, "Modelo"));
        validadores.add(new CampoObrigatorioValidador(fabricante, "Fabricante"));
        validadores.add(new CampoObrigatorioValidador(capacidade, "Capacidade"));
        validadores.add(new CampoObrigatorioValidador(autonomia, "Autonomia"));
        validadores.add(new CampoObrigatorioValidador(ano, "Ano"));

        // Adicionando os validadores específicos de formato/negócio
        validadores.add(new NumeroPositivoValidador(capacidade, "Capacidade"));
        validadores.add(new NumeroPositivoValidador(autonomia, "Autonomia"));
        validadores.add(new AnoValidador(ano)); //esse seria o "email" validator do slide da debora

        // Itera sobre a lista de validadores
        for (Validador<String> validador : validadores) {

            // Cada validador testa seu valor específico
            if (!validador.validar(validador.getValor())) {

                // Exibe a mensagem de erro direto na tela usando a sua classe utilitária
                DialogUtil.showWarning(validador.getMensagemErro());

                return false; // Retorna falso na primeira falha de validação e para o loop
            }
        }

        return true; // Todos os validadores passaram
    }

    /**
     * Valida se o ID informado é válido para operações de alteração e exclusão.
     */
    public static boolean isIdValido(String id) {
        NumeroPositivoValidador validadorId = new NumeroPositivoValidador(id, "ID");

        if (!validadorId.validar(validadorId.getValor())) {
            DialogUtil.showWarning(validadorId.getMensagemErro());
            return false;
        }

        return true;
    }
}