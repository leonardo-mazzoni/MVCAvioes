package com.template.util;

import com.template.model.dto.AviaoDTO;

public class AviaoMapper {

    /**
     * Transforma as Strings recebidas da interface gráfica em um objeto AviaoDTO pronto.
     */
    public static AviaoDTO montarDTO(String id, String modelo, String fabricante, String capacidade, String autonomia, String ano) {
        AviaoDTO dto = new AviaoDTO();

        // Se o ID for passado (caso de alteração), converte para Integer
        if (id != null && !id.trim().isEmpty()) {
            dto.setId(Integer.parseInt(id.trim()));
        }

        dto.setModelo(modelo.trim());
        dto.setFabricante(fabricante.trim());
        dto.setCapacidadePassageiros(Integer.parseInt(capacidade.trim()));
        dto.setAutonomiaKm(Integer.parseInt(autonomia.trim()));
        dto.setAnoFabricacao(Integer.parseInt(ano.trim()));

        return dto;
    }
}