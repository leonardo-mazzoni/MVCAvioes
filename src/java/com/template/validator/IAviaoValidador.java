package com.template.validator;

// Contrato de validacao para o Controller (Inversao de Dependencia)
public interface IAviaoValidador {
    // Valida todos os campos juntos no cadastro/alteracao
    boolean validarAviao(String modelo, String fabricante, String capacidade, String autonomia, String ano);

    // Metodos auxiliares por campo individual
    boolean validarModelo(String modelo);
    boolean validarFabricante(String fabricante);
    boolean validarCapacidade(String capacidade);
    boolean validarAutonomia(String autonomia);
    boolean validarAno(String ano);
}