package com.template.validator;

import java.util.regex.Pattern;

// Valida se a entrada e um numero inteiro estritamente positivo (> 0)
public class NumeroPositivoValidador implements Validador<String> {

    // Regex aceita apenas digitos de 1 a 9 no inicio, impedindo negativos, zero e letras
    private static final String REGEX_NUMERO_POSITIVO = "^[1-9]\\d*$";
    private final Pattern pattern = Pattern.compile(REGEX_NUMERO_POSITIVO);
    private final String numero;
    private final String nomeCampo;

    public NumeroPositivoValidador(String numero, String nomeCampo) {
        this.numero = numero;
        this.nomeCampo = nomeCampo;
    }

    @Override
    public boolean validar(String valorAtual) {
        // Valida se nao esta vazio e bate exatamente com a expressao regular
        return this.numero != null && !this.numero.trim().isEmpty() && pattern.matcher(this.numero.trim()).matches();
    }

    @Override
    public String getMensagemErro() {
        return "O campo " + nomeCampo + " deve ser um número inteiro positivo maior que zero.";
    }

    @Override
    public String getValor() {
        return numero;
    }
}