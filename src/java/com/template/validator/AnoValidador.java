package com.template.validator;

import java.util.regex.Pattern;

public class AnoValidador implements Validador<String> {

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