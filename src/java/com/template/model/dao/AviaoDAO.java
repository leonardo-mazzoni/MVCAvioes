package com.template.model.dao;

import com.template.model.dto.AviaoDTO;
import com.template.model.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AviaoDAO {

    public void cadastrarAviao(AviaoDTO aviaoDTO) throws SQLException {
        String sqlInserir = "INSERT INTO avioes (modelo, fabricante, capacidade_passageiros, autonomia_km, ano_fabricacao) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexaoBanco = Conexao.obterConexao();
             PreparedStatement declaracaoPreparada = conexaoBanco.prepareStatement(sqlInserir)) {

            declaracaoPreparada.setString(1, aviaoDTO.getModelo());
            declaracaoPreparada.setString(2, aviaoDTO.getFabricante());
            declaracaoPreparada.setInt(3, aviaoDTO.getCapacidadePassageiros());
            declaracaoPreparada.setInt(4, aviaoDTO.getAutonomiaKm());
            declaracaoPreparada.setInt(5, aviaoDTO.getAnoFabricacao());

            declaracaoPreparada.execute();
        }
    }

    public ArrayList<AviaoDTO> listarTodos() throws SQLException {
        String sqlBuscarTodos = "SELECT * FROM avioes ORDER BY id";
        ArrayList<AviaoDTO> listaAvioes = new ArrayList<>();

        try (Connection conexaoBanco = Conexao.obterConexao();
             PreparedStatement declaracaoPreparada = conexaoBanco.prepareStatement(sqlBuscarTodos);
             ResultSet resultadoConsulta = declaracaoPreparada.executeQuery()) {

            while (resultadoConsulta.next()) {
                AviaoDTO aviaoDTO = new AviaoDTO();
                aviaoDTO.setId(resultadoConsulta.getInt("id"));
                aviaoDTO.setModelo(resultadoConsulta.getString("modelo"));
                aviaoDTO.setFabricante(resultadoConsulta.getString("fabricante"));
                aviaoDTO.setCapacidadePassageiros(resultadoConsulta.getInt("capacidade_passageiros"));
                aviaoDTO.setAutonomiaKm(resultadoConsulta.getInt("autonomia_km"));
                aviaoDTO.setAnoFabricacao(resultadoConsulta.getInt("ano_fabricacao"));

                listaAvioes.add(aviaoDTO);
            }
        }
        return listaAvioes;
    }

    public void atualizarAviao(AviaoDTO aviaoDTO) throws SQLException {
        String sqlAtualizar = "UPDATE avioes SET modelo = ?, fabricante = ?, capacidade_passageiros = ?, autonomia_km = ?, ano_fabricacao = ? WHERE id = ?";

        try (Connection conexaoBanco = Conexao.obterConexao();
             PreparedStatement declaracaoPreparada = conexaoBanco.prepareStatement(sqlAtualizar)) {

            declaracaoPreparada.setString(1, aviaoDTO.getModelo());
            declaracaoPreparada.setString(2, aviaoDTO.getFabricante());
            declaracaoPreparada.setInt(3, aviaoDTO.getCapacidadePassageiros());
            declaracaoPreparada.setInt(4, aviaoDTO.getAutonomiaKm());
            declaracaoPreparada.setInt(5, aviaoDTO.getAnoFabricacao());
            declaracaoPreparada.setInt(6, aviaoDTO.getId());

            declaracaoPreparada.executeUpdate();
        }
    }

    public void excluirAviao(int idAviao) throws SQLException {
        String sqlExcluir = "DELETE FROM avioes WHERE id = ?";

        try (Connection conexaoBanco = Conexao.obterConexao();
             PreparedStatement declaracaoPreparada = conexaoBanco.prepareStatement(sqlExcluir)) {

            declaracaoPreparada.setInt(1, idAviao);
            declaracaoPreparada.execute();
        }
    }
}