package com.template.services;

import com.template.model.dao.AviaoDAO;
import com.template.model.dto.AviaoDTO;

import java.util.ArrayList;

public class AviaoService {

    private final AviaoDAO aviaoDAO = new AviaoDAO();

    public ArrayList<AviaoDTO> listarTodos() throws Exception {
        return aviaoDAO.listarTodos();
    }

    public void salvar(AviaoDTO aviao) throws Exception {
        aviaoDAO.cadastrarAviao(aviao);
    }

    public void atualizar(AviaoDTO aviao) throws Exception {
        aviaoDAO.atualizarAviao(aviao);
    }

    public void excluir(int id) throws Exception {
        aviaoDAO.excluirAviao(id);
    }
}