package com.template.validator;

// Valida se o texto nao esta nulo e nem vazio (com apenas espacos)
public class CamposObrigatoriosValidador implements Validador<String> {

    private final String nomeCampo;
    private final String valor;

    public CamposObrigatoriosValidador(String nomeCampo, String valor) {
        this.nomeCampo = nomeCampo;
        this.valor = valor;
    }

    @Override
    public boolean validar(String valorAtual) {
        // Retorna true se tiver pelo menos um caractere valido digitado
        return this.valor != null && !this.valor.trim().isEmpty();
    }

    @Override
    public String getMensagemErro() {
        return "O campo " + nomeCampo + " deve ser preenchido.";
    }

    @Override
    public String getValor() {
        return valor;
    }
}
