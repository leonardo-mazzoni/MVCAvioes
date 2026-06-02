package com.template;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AviaoDAO {

    private static final Logger logger = Logger.getLogger(AviaoDAO.class.getName());

    public void cadastrarAviao(AviaoDTO aviao) {
        String sql = "INSERT INTO avioes (modelo, fabricante, capacidade_passageiros, autonomia_km, ano_fabricacao) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = Conexao.criarConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, aviao.getModelo());
            ps.setString(2, aviao.getFabricante());
            ps.setInt(3, aviao.getCapacidadePassageiros());
            ps.setInt(4, aviao.getAutonomiaKm());
            ps.setInt(5, aviao.getAnoFabricacao());

            ps.execute();
            logger.info("Avião cadastrado com sucesso: " + aviao.getModelo());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro crítico ao cadastrar avião no banco de dados. Modelo: " + aviao.getModelo(), e);
            throw new RuntimeException("Falha na persistência: " + e.getMessage());
        }
    }

    public ArrayList<AviaoDTO> listarTodos() {
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
            logger.info("Consulta realizada: " + lista.size() + " aviões encontrados.");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao recuperar lista de aviões do banco.", e);
            throw new RuntimeException("Erro ao listar aviões.");
        }
        return lista;
    }

    public void atualizarAviao(AviaoDTO aviao) {
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
            logger.info("Dados do avião ID " + aviao.getId() + " atualizados com sucesso.");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Falha ao atualizar o avião ID: " + aviao.getId(), e);
            throw new RuntimeException("Erro ao atualizar registro.");
        }
    }

    public void excluirAviao(int id) {
        String sql = "DELETE FROM avioes WHERE id = ?";

        try (Connection conexao = Conexao.criarConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.execute();
            logger.warning("Avião ID " + id + " removido do sistema.");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao tentar excluir o avião ID: " + id, e);
            throw new RuntimeException("Não foi possível excluir o registro.");
        }
    }
}