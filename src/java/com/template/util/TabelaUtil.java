package com.template.util;

import com.template.model.dto.AviaoDTO;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

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
}