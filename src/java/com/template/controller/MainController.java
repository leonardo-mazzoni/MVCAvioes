package com.template.controller;

import com.template.model.dto.AviaoDTO;
import com.template.services.AviaoService;
import com.template.services.LayoutServices;
import com.template.util.AviaoMapper;
import com.template.util.DialogUtil;
import com.template.util.TabelaUtil;
import com.template.validator.AviaoValidator;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class MainController {

    @FXML private Button btnSalvar;
    @FXML private Button btnAlterar;
    @FXML private Button btnExcluir;
    @FXML private Button btnLimpar;

    @FXML private TextField txtId;
    @FXML private TextField txtModelo;
    @FXML private TextField txtFabricante;
    @FXML private TextField txtCapacidade;
    @FXML private TextField txtAutonomia;
    @FXML private TextField txtAno;

    @FXML private Label lblMensagem;

    @FXML private TableView<AviaoDTO> tblAvioes;
    @FXML private TableColumn<AviaoDTO, Integer> colId;
    @FXML private TableColumn<AviaoDTO, String> colModelo;
    @FXML private TableColumn<AviaoDTO, String> colFabricante;
    @FXML private TableColumn<AviaoDTO, Integer> colCapacidade;
    @FXML private TableColumn<AviaoDTO, Integer> colAutonomia;
    @FXML private TableColumn<AviaoDTO, Integer> colAno;

    // A dependência agora é do Service, e não mais do DAO
    private final AviaoService aviaoService = new AviaoService();

    @FXML
    private void initialize() {
        // Delegação para a classe utilitária de Tabela
        TabelaUtil.configurarColunasAviao(colId, colModelo, colFabricante, colCapacidade, colAutonomia, colAno);

        // Delegação para a classe LayoutServices
        LayoutServices.aplicarFiltrosEntradaNumerica(txtCapacidade, txtAutonomia, txtAno);
        LayoutServices.configurarEstadoBotoes(btnSalvar, btnAlterar, btnExcluir, false);

        carregarTabelaAvioes();
    }

    private boolean validarEntradas() {
        boolean isValido = AviaoValidator.validarCamposFormulario(
                txtModelo.getText(),
                txtFabricante.getText(),
                txtCapacidade.getText(),
                txtAutonomia.getText(),
                txtAno.getText()
        );

        if (!isValido) {
            LayoutServices.exibirMensagemFeedback(lblMensagem, "Erro: Preencha todos os campos corretamente (ano de 4 dígitos e valores > 0).", false);
        }

        return isValido;
    }

    private void carregarTabelaAvioes() {
        try {
            ArrayList<AviaoDTO> listaAvioes = aviaoService.listarTodos();
            tblAvioes.setItems(FXCollections.observableArrayList(listaAvioes));
        } catch (Exception e) {
            LayoutServices.exibirMensagemFeedback(lblMensagem, "Erro ao carregar dados do banco.", false);
            DialogUtil.showError("Falha crítica ao tentar conectar com o banco de dados para carregar a tabela.");
        }
    }

    @FXML
    private void carregarCampos(MouseEvent evento) {
        AviaoDTO aviaoSelecionadoDTO = tblAvioes.getSelectionModel().getSelectedItem();

        LayoutServices.preencherCampos(
                aviaoSelecionadoDTO,
                txtId, txtModelo, txtFabricante, txtCapacidade, txtAutonomia, txtAno,
                btnSalvar, btnAlterar, btnExcluir,
                lblMensagem
        );
    }

    @FXML
    private void btnLimparAction(ActionEvent evento) {
        LayoutServices.limparFormulario(
                lblMensagem,
                tblAvioes,
                btnSalvar, btnAlterar, btnExcluir,
                txtModelo,
                txtId, txtModelo, txtFabricante, txtCapacidade, txtAutonomia, txtAno
        );
    }

    @FXML
    private void btnSalvarAction(ActionEvent evento) {
        if (!validarEntradas()) return;

        try {
            // Delega a criação do objeto para o Mapper
            AviaoDTO aviaoDTO = AviaoMapper.montarDTO(
                    null, txtModelo.getText(), txtFabricante.getText(),
                    txtCapacidade.getText(), txtAutonomia.getText(), txtAno.getText()
            );

            // Delega a ação de salvar para o Service
            aviaoService.salvar(aviaoDTO);

            carregarTabelaAvioes();
            btnLimparAction(null);
            LayoutServices.exibirMensagemFeedback(lblMensagem, "Avião cadastrado com sucesso!", true);
        } catch (Exception e) {
            LayoutServices.exibirMensagemFeedback(lblMensagem, "Erro ao salvar no banco de dados.", false);
            DialogUtil.showError("Ocorreu um erro inesperado ao tentar salvar a aeronave no banco de dados.");
        }
    }

    @FXML
    private void btnAlterarAction(ActionEvent evento) {
        if (!AviaoValidator.isIdValido(txtId.getText())) return;
        if (!validarEntradas()) return;

        try {
            // Delega a criação do objeto para o Mapper
            AviaoDTO aviaoDTO = AviaoMapper.montarDTO(
                    txtId.getText(), txtModelo.getText(), txtFabricante.getText(),
                    txtCapacidade.getText(), txtAutonomia.getText(), txtAno.getText()
            );

            // Delega a ação de atualizar para o Service
            aviaoService.atualizar(aviaoDTO);

            carregarTabelaAvioes();
            btnLimparAction(null);
            LayoutServices.exibirMensagemFeedback(lblMensagem, "Dados atualizados com sucesso!", true);
        } catch (Exception e) {
            LayoutServices.exibirMensagemFeedback(lblMensagem, "Erro ao atualizar aeronave.", false);
            DialogUtil.showError("Ocorreu um erro inesperado ao tentar atualizar os dados da aeronave.");
        }
    }

    @FXML
    private void btnExcluirAction(ActionEvent evento) {
        if (!AviaoValidator.isIdValido(txtId.getText())) return;

        boolean confirmado = DialogUtil.showConfirmation("Atenção: Tem certeza que deseja excluir esta aeronave? Esta ação não pode ser desfeita.");
        if (!confirmado) return;

        try {
            int idAviao = Integer.parseInt(txtId.getText().trim());

            // Delega a ação de exclusão para o Service
            aviaoService.excluir(idAviao);

            carregarTabelaAvioes();
            btnLimparAction(null);
            LayoutServices.exibirMensagemFeedback(lblMensagem, "Aeronave excluída com sucesso!", true);
        } catch (Exception e) {
            LayoutServices.exibirMensagemFeedback(lblMensagem, "Erro ao excluir aeronave.", false);
            DialogUtil.showError("Falha crítica ao tentar excluir a aeronave do banco de dados.");
        }
    }
}