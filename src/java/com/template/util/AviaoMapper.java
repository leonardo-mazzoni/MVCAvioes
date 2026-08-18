/* EXPLICAÇÃO DA CLASSE AviaoMapper:

O Problema: Nos métodos btnSalvarAction e btnAlterarAction,
tem várias linhas fazendo Integer.parseInt(txtCapacidade.getText().trim())
para montar o objeto AviaoDTO. Agora tem o AviaoMapper para nao poluir o Controller.

A Solução: Uma classe especializada apenas em converter os dados dos TextFields da tela para um objeto AviaoDTO. */

package com.template.util;

import com.template.model.dto.AviaoDTO;

public class AviaoMapper {

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