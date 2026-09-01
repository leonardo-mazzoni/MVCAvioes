package com.template.services;

import com.template.model.dto.AviaoDTO;
import java.util.ArrayList;

public interface IAviaoService {
    ArrayList<AviaoDTO> listarTodos() throws Exception;
    void salvar(AviaoDTO aviao) throws Exception;
    void atualizar(AviaoDTO aviao) throws Exception;
    void excluir(int id) throws Exception;
}