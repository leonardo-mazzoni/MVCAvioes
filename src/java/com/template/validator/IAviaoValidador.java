package com.template.validator;

public interface IAviaoValidador {
    boolean validarAviao(String modelo, String fabricante, String capacidade, String autonomia, String ano);
    boolean validarModelo(String modelo);
    boolean validarFabricante(String fabricante);
    boolean validarCapacidade(String capacidade);
    boolean validarAutonomia(String autonomia);
    boolean validarAno(String ano);
}