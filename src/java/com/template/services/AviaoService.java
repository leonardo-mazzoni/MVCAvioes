/*
EXPLICAÇÃO:
O Problema: Seu Controller chama o aviaoDAO diretamente e lida com os blocos try/catch.
A Solução: Criar uma camada intermediária. O Controller chama o Service, e o Service é quem "conversa" com o DAO.
Se der erro, o Service avisa.
 */

package com.template.services;

import com.template.model.dao.AviaoDAO;
import com.template.model.dto.AviaoDTO;

import java.util.ArrayList;

public class AviaoService implements IAviaoService {

    private final AviaoDAO aviaoDAO = new AviaoDAO();

    // throws é a batata quente
    @Override
    public ArrayList<AviaoDTO> listarTodos() throws Exception {
        return aviaoDAO.listarTodos();
    }

    @Override
    public void salvar(AviaoDTO aviao) throws Exception {
        aviaoDAO.cadastrarAviao(aviao);
    }

    @Override
    public void atualizar(AviaoDTO aviao) throws Exception {
        aviaoDAO.atualizarAviao(aviao);
    }

    @Override
    public void excluir(int id) throws Exception {
        aviaoDAO.excluirAviao(id);
    }
}