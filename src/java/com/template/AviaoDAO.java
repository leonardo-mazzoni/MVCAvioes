package com.template;
//packages e model que estava no java

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.connection.Conexao;
import model.dto.AviaoDTO;

public class AviaoDAO {

    public void cadastrarAviao(AviaoDTO aviao) {
        String sql = "INSERT INTO avioes (modelo, fabricante, capacidade_passageiros, autonomia_km, ano_fabricacao) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = Conexao.criarConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            // preparamos statements para deixar mais coerente
            // usado para enviar as informações para o banco de dados
            // eu preencho e mando para o banco
            ps.setString(1, aviao.getModelo());
            ps.setString(2, aviao.getFabricante());
            ps.setInt(3, aviao.getCapacidadePassageiros());
            ps.setInt(4, aviao.getAutonomiaKm());
            ps.setInt(5, aviao.getAnoFabricacao());
            ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar avião: " + e.getMessage());
        }
    }

    public List<AviaoDTO> listarTodos() {
        String sql = "SELECT * FROM avioes ORDER BY id";
        List<AviaoDTO> lista = new ArrayList<>();

        try (Connection conexao = Conexao.criarConexao();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            //aqui o branco preenche e me manda
            //vai preenchendo a lista de acordo com o que tem no banco
            while (rs.next()) {
                AviaoDTO aviao = new AviaoDTO();
                aviao.setId(rs.getInt("id"));
                aviao.setModelo(rs.getString("modelo"));
                aviao.setFabricante(rs.getString("fabricante"));
                aviao.setCapacidadePassageiros(rs.getInt("capacidade_passageiros"));
                aviao.setAutonomiaKm(rs.getInt("autonomia_km"));
                aviao.setAnoFabricacao(rs.getInt("ano_fabricacao"));
                lista.add(aviao); //volta para onde chamou, ou seja, listar
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar aviões: " + e.getMessage());
        }
        return lista;
    }

    public void atualizarAviao(AviaoDTO aviao) {
        String sql = "UPDATE avioes SET modelo = ?, fabricante = ?, capacidade_passageiros = ?, autonomia_km = ?, ano_fabricacao = ? WHERE id = ?";

        try (Connection conexao = Conexao.criarConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            // aqui a gente set os parametros
            //atualiza
            ps.setString(1, aviao.getModelo());
            ps.setString(2, aviao.getFabricante());
            ps.setInt(3, aviao.getCapacidadePassageiros());
            ps.setInt(4, aviao.getAutonomiaKm());
            ps.setInt(5, aviao.getAnoFabricacao());
            ps.setInt(6, aviao.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar avião: " + e.getMessage());
        }
    }

    public void excluirAviao(int id) {
        String sql = "DELETE FROM avioes WHERE id = ?";

        try (Connection conexao = Conexao.criarConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir avião: " + e.getMessage());
        }
    }
}
