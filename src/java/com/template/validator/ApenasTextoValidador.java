package com.template.validator;

import java.util.regex.Pattern;

// Valida se o campo contem apenas letras e espacos, rejeitando numeros
public class ApenasTextoValidador implements Validador<String> {

    // Regex que aceita letras maiusculas, minusculas, acentos e espacos
    private static final String REGEX_APENAS_TEXTO = "^[a-zA-ZÀ-ÿ\\s]+$";
    private final Pattern pattern = Pattern.compile(REGEX_APENAS_TEXTO);
    private final String valor;
    private final String nomeCampo;

    public ApenasTextoValidador(String valor, String nomeCampo) {
        this.valor = valor;
        this.nomeCampo = nomeCampo;
    }

    @Override
    public boolean validar(String valorAtual) {
        return this.valor != null && !this.valor.trim().isEmpty() && pattern.matcher(this.valor.trim()).matches();
    }

    @Override
    public String getMensagemErro() {
        return "O campo " + nomeCampo + " deve conter apenas letras (números não são permitidos).";
    }

    @Override
    public String getValor() {
        return valor;
    }
}
