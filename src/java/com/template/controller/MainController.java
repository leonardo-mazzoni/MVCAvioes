package com.template.controller;

import com.template.model.dto.AviaoDTO;
import com.template.services.IAviaoService;
import com.template.services.LayoutServices;
import com.template.util.AviaoMapper;
import com.template.util.DialogUtil;
import com.template.util.TabelaUtil;
import com.template.validator.IAviaoValidador;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

/**
 * Controller principal do padrão MVC (Enxuto).
 * Responsabilidade: Orquestrar eventos de UI e delegar regras de negócio,
 * validação, mapeamento de dados e operações visuais para classes especializadas (SRP).
 * Depende exclusivamente de abstrações (DIP).
 */
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

    // Dependências injetadas exclusivamente como interfaces (DIP)
    private final IAviaoService aviaoService;
    private final IAviaoValidador aviaoValidador;

    // Injeção de dependência via construtor (Slide 30)
    public MainController(IAviaoService aviaoService, IAviaoValidador aviaoValidador) {
        this.aviaoService = aviaoService;
        this.aviaoValidador = aviaoValidador;
    }

    @FXML
    private void initialize() {
        TabelaUtil.configurarColunasAviao(colId, colModelo, colFabricante, colCapacidade, colAutonomia, colAno);
        LayoutServices.aplicarFiltrosEntradaNumerica(txtCapacidade, txtAutonomia, txtAno);
        LayoutServices.configurarEstadoBotoes(btnSalvar, btnAlterar, btnExcluir, false);
        carregarTabelaAvioes();
    }

    private boolean validarEntradas() {
        return aviaoValidador.validarAviao(
                txtModelo.getText(),
                txtFabricante.getText(),
                txtCapacidade.getText(),
                txtAutonomia.getText(),
                txtAno.getText()
        );
    }

    private void carregarTabelaAvioes() {
        try {
            ArrayList<AviaoDTO> listaAvioes = aviaoService.listarTodos();
            tblAvioes.setItems(FXCollections.observableArrayList(listaAvioes));
        } catch (Exception e) {
            LayoutServices.exibirMensagemFeedback(lblMensagem, "Erro ao carregar dados do banco.", false);
            DialogUtil.showError("Falha crítica ao tentar conectar com o banco de dados.");
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
        // Delega toda a validação para o validador (Slide 11)
        if (!validarEntradas()) return;

        try {
            AviaoDTO aviaoDTO = AviaoMapper.montarDTO(
                    null, txtModelo.getText(), txtFabricante.getText(),
                    txtCapacidade.getText(), txtAutonomia.getText(), txtAno.getText()
            );

            aviaoService.salvar(aviaoDTO);
            carregarTabelaAvioes();
            btnLimparAction(null);
            LayoutServices.exibirMensagemFeedback(lblMensagem, "Avião cadastrado com sucesso!", true);
            DialogUtil.showInformation("Avião cadastrado com sucesso!");
        } catch (Exception e) {
            LayoutServices.exibirMensagemFeedback(lblMensagem, "Erro ao salvar no banco de dados.", false);
            DialogUtil.showError("Ocorreu um erro inesperado ao salvar a aeronave.");
        }
    }

    @FXML
    private void btnAlterarAction(ActionEvent evento) {
        if (!validarEntradas()) return;

        try {
            AviaoDTO aviaoDTO = AviaoMapper.montarDTO(
                    txtId.getText(), txtModelo.getText(), txtFabricante.getText(),
                    txtCapacidade.getText(), txtAutonomia.getText(), txtAno.getText()
            );

            aviaoService.atualizar(aviaoDTO);
            carregarTabelaAvioes();
            btnLimparAction(null);
            LayoutServices.exibirMensagemFeedback(lblMensagem, "Dados atualizados com sucesso!", true);
            DialogUtil.showInformation("Dados atualizados com sucesso!");
        } catch (Exception e) {
            LayoutServices.exibirMensagemFeedback(lblMensagem, "Erro ao atualizar aeronave.", false);
            DialogUtil.showError("Ocorreu um erro inesperado ao atualizar os dados.");
        }
    }

    @FXML
    private void btnExcluirAction(ActionEvent evento) {
        if (txtId.getText() == null || txtId.getText().trim().isEmpty()) {
            DialogUtil.showWarning("Selecione um avião na tabela para poder excluir.");
            return;
        }

        if (!DialogUtil.showConfirmation("Atenção: Deseja realmente excluir esta aeronave?")) {
            return;
        }

        try {
            int idAviao = Integer.parseInt(txtId.getText().trim());
            aviaoService.excluir(idAviao);
            carregarTabelaAvioes();
            btnLimparAction(null);
            LayoutServices.exibirMensagemFeedback(lblMensagem, "Aeronave excluída com sucesso!", true);
            DialogUtil.showInformation("Aeronave excluída com sucesso!");
        } catch (Exception e) {
            LayoutServices.exibirMensagemFeedback(lblMensagem, "Erro ao excluir aeronave.", false);
            DialogUtil.showError("Falha crítica ao tentar excluir a aeronave.");
        }
    }
}