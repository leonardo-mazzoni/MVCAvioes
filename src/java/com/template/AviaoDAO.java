package com.template;

import java.sql.*;
import java.util.ArrayList;

public class AviaoDAO {

    public void cadastrarAviao(AviaoDTO aviao) throws SQLException {
        String sql = "INSERT INTO avioes (modelo, fabricante, capacidade_passageiros, autonomia_km, ano_fabricacao) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = Conexao.criarConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, aviao.getModelo());
            ps.setString(2, aviao.getFabricante());
            ps.setInt(3, aviao.getCapacidadePassageiros());
            ps.setInt(4, aviao.getAutonomiaKm());
            ps.setInt(5, aviao.getAnoFabricacao());

            ps.execute();
        }
    }

    public ArrayList<AviaoDTO> listarTodos() throws SQLException {
        String sql = "SELECT * FROM avioes ORDER BY id";
        ArrayList<AviaoDTO> lista = new ArrayList<>();

        try (Connection conexao = Conexao.criarConexao();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AviaoDTO aviao = new AviaoDTO();
                aviao.setId(rs.getInt("id"));
                aviao.setModelo(rs.getString("modelo"));
                aviao.setFabricante(rs.getString("fabricante"));
                aviao.setCapacidadePassageiros(rs.getInt("capacidade_passageiros"));
                aviao.setAutonomiaKm(rs.getInt("autonomia_km"));
                aviao.setAnoFabricacao(rs.getInt("ano_fabricacao"));
                lista.add(aviao);
            }
        }
        return lista;
    }

    public void atualizarAviao(AviaoDTO aviao) throws SQLException {
        String sql = "UPDATE avioes SET modelo = ?, fabricante = ?, capacidade_passageiros = ?, autonomia_km = ?, ano_fabricacao = ? WHERE id = ?";

        try (Connection conexao = Conexao.criarConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, aviao.getModelo());
            ps.setString(2, aviao.getFabricante());
            ps.setInt(3, aviao.getCapacidadePassageiros());
            ps.setInt(4, aviao.getAutonomiaKm());
            ps.setInt(5, aviao.getAnoFabricacao());
            ps.setInt(6, aviao.getId());

            ps.executeUpdate();
        }
    }

    public void excluirAviao(int id) throws SQLException {
        String sql = "DELETE FROM avioes WHERE id = ?";

        try (Connection conexao = Conexao.criarConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.execute();
        }
    }
}