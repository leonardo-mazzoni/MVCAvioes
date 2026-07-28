package com.template.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL_CONEXAO = "jdbc:postgresql://localhost:5432/MVCAvioes";
    private static final String USUARIO_BANCO = "postgres";
    private static final String SENHA_BANCO = "postgres";

    public static Connection obterConexao() {
        try {
            return DriverManager.getConnection(URL_CONEXAO, USUARIO_BANCO, SENHA_BANCO);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }
}