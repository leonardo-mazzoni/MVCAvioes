package com.template.util;

import com.template.model.dto.AviaoDTO;
import com.template.services.IAviaoService;
import com.template.services.LayoutServices;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

// Utilitario da TableView: isola a configuracao das colunas e o carregamento dos dados
public class TabelaUtil {

    /**
     * Configura as fábricas de valores para cada coluna da Tabela de Aviões.
     */
    public static void configurarColunasAviao(
            TableColumn<AviaoDTO, Integer> colId,
            TableColumn<AviaoDTO, String> colModelo,
            TableColumn<AviaoDTO, String> colFabricante,
            TableColumn<AviaoDTO, Integer> colCapacidade,
            TableColumn<AviaoDTO, Integer> colAutonomia,
            TableColumn<AviaoDTO, Integer> colAno) {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colFabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
        colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidadePassageiros"));
        colAutonomia.setCellValueFactory(new PropertyValueFactory<>("autonomiaKm"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("anoFabricacao"));
    }

    /**
     * Carrega ou atualiza os dados da tabela buscando do serviço e tratando possíveis erros.
     */
    public static void carregarTabelaAvioes(TableView<AviaoDTO> tblAvioes, IAviaoService aviaoService, Label lblMensagem) {
        try {
            ArrayList<AviaoDTO> listaAvioes = aviaoService.listarTodos();
            tblAvioes.setItems(FXCollections.observableArrayList(listaAvioes));
        } catch (Exception e) {
            LayoutServices.exibirMensagemFeedback(lblMensagem, "Erro ao carregar dados do banco.", false);
            DialogUtil.showError("Falha crítica ao tentar conectar com o banco de dados.");
        }
    }
}