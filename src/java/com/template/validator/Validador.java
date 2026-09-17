package com.template.validator;

// Contrato generico com <T>: valida qualquer tipo com seguranca (type-safety)
public interface Validador<T> {
    boolean validar(T valor);      // Aplica a regra de validacao
    String getMensagemErro();      // Mensagem exibida no alerta em caso de erro
    T getValor();                  // Devolve o dado testado
}

