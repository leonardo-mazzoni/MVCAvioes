package com.template.validator;

import java.util.regex.Pattern;

public class AviaoValidator {

    // Constantes para Expressões Regulares (Convenção SNAKE_CASE)
    private static final String REGEX_ANO = "^(19|20)\\d{2}$"; // Anos de 1900 a 2099
    private static final String REGEX_NUMERO_POSITIVO = "^[1-9]\\d*$"; // Números inteiros maiores que zero

    /**
     * Verifica se um texto está nulo ou contém apenas espaços em branco.
     */
    public static boolean isTextoVazio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    /**
     * Valida se a string representa um ano de 4 dígitos válido.
     */
    public static boolean isAnoValido(String ano) {
        return !isTextoVazio(ano) && Pattern.matches(REGEX_ANO, ano.trim());
    }

    /**
     * Valida se a string representa um número inteiro positivo (> 0).
     */
    public static boolean isNumeroPositivoValido(String numero) {
        return !isTextoVazio(numero) && Pattern.matches(REGEX_NUMERO_POSITIVO, numero.trim());
    }

    /**
     * Valida se o ID informado é válido para operações de alteração e exclusão.
     */
    public static boolean isIdValido(String id) {
        return isNumeroPositivoValido(id);
    }

    /**
     * Executa a validação completa do formulário de cadastro/edição.
     */
    public static boolean validarCamposFormulario(String modelo, String fabricante, String capacidade, String autonomia, String ano) {
        // 1. Checa se algum campo obrigatório está em branco
        if (isTextoVazio(modelo) || isTextoVazio(fabricante) ||
                isTextoVazio(capacidade) || isTextoVazio(autonomia) || isTextoVazio(ano)) {
            return false;
        }

        // 2. Checa as regras de formato e regras de negócio com Regex
        if (!isNumeroPositivoValido(capacidade) ||
                !isNumeroPositivoValido(autonomia) ||
                !isAnoValido(ano)) {
            return false;
        }

        return true;
    }
}