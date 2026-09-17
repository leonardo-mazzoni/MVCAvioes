package com.template.validator;

import java.util.regex.Pattern;

// Valida se o ano de fabricacao e valido (formato de 4 digitos entre 1900 e 2099)
public class AnoValidador implements Validador<String> {

    // Regex: comeca com 19 ou 20 seguido de dois digitos (1900 ate 2099)
    private static final String REGEX_ANO = "^(19|20)\\d{2}$";
    private final Pattern pattern = Pattern.compile(REGEX_ANO);
    private final String ano;

    public AnoValidador(String ano) {
        this.ano = ano;
    }

    @Override
    public boolean validar(String valorAtual) {
        return this.ano != null && !this.ano.trim().isEmpty() && pattern.matcher(this.ano.trim()).matches();
    }

    @Override
    public String getMensagemErro() {
        return "Digite um ano válido (exemplo: 2023).";
    }

    @Override
    public String getValor() {
        return ano;
    }
}