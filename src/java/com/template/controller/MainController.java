package com.template.controller;

import com.template.model.dao.AviaoDAO;
import com.template.model.dto.AviaoDTO;
import com.template.util.DialogUtil; // Importação da sua classe utilitária
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

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

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colFabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
        colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidadePassageiros"));
        colAutonomia.setCellValueFactory(new PropertyValueFactory<>("autonomiaKm"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("anoFabricacao"));

        aplicarFiltrosEntradaNumerica();
        definirEstadoInicialBotoes();
        carregarTabelaAvioes();
    }

    private void aplicarFiltrosEntradaNumerica() {
        txtCapacidade.setTextFormatter(new TextFormatter<>(mudanca -> mudanca.getText().matches("[0-9]*") ? mudanca : null));
        txtAutonomia.setTextFormatter(new TextFormatter<>(mudanca -> mudanca.getText().matches("[0-9]*") ? mudanca : null));
        txtAno.setTextFormatter(new TextFormatter<>(mudanca -> mudanca.getText().matches("[0-9]*") ? mudanca : null));
    }

    private void definirEstadoInicialBotoes() {
        btnSalvar.setDisable(false);
        btnAlterar.setDisable(true);
        btnExcluir.setDisable(true);
    }

    private void exibirMensagemFeedback(String mensagem, boolean isSucesso) {
        lblMensagem.setText(mensagem);
        lblMensagem.setTextFill(isSucesso ? Color.GREEN : Color.RED);
    }

    private boolean saoCamposValidos() {
        if (txtModelo.getText().trim().isEmpty() ||
                txtFabricante.getText().trim().isEmpty() ||
                txtCapacidade.getText().trim().isEmpty() ||
                txtAutonomia.getText().trim().isEmpty() ||
                txtAno.getText().trim().isEmpty()) {

            exibirMensagemFeedback("Erro: Preencha todos os campos obrigatórios (*).", false);
            return false;
        }
        return true;
    }

    private void carregarTabelaAvioes() {
        try {
            AviaoDAO aviaoDAO = new AviaoDAO();
            ArrayList<AviaoDTO> listaAvioes = aviaoDAO.listarTodos();
            tblAvioes.setItems(FXCollections.observableArrayList(listaAvioes));
        } catch (Exception e) {
            exibirMensagemFeedback("Erro ao carregar dados do banco.", false);
            // Reforço em caso de erro crítico no banco
            DialogUtil.showError("Falha crítica ao tentar conectar com o banco de dados para carregar a tabela.");
        }
    }

    @FXML
    private void carregarCampos(MouseEvent evento) {
        AviaoDTO aviaoSelecionadoDTO = tblAvioes.getSelectionModel().getSelectedItem();

        if (aviaoSelecionadoDTO != null) {
            txtId.setText(String.valueOf(aviaoSelecionadoDTO.getId()));
            txtModelo.setText(aviaoSelecionadoDTO.getModelo());
            txtFabricante.setText(aviaoSelecionadoDTO.getFabricante());
            txtCapacidade.setText(String.valueOf(aviaoSelecionadoDTO.getCapacidadePassageiros()));
            txtAutonomia.setText(String.valueOf(aviaoSelecionadoDTO.getAutonomiaKm()));
            txtAno.setText(String.valueOf(aviaoSelecionadoDTO.getAnoFabricacao()));

            btnSalvar.setDisable(true);
            btnAlterar.setDisable(false);
            btnExcluir.setDisable(false);
            lblMensagem.setText("");
        }
    }

    @FXML
    private void btnLimparAction(ActionEvent evento) {
        txtId.clear();
        txtModelo.clear();
        txtFabricante.clear();
        txtCapacidade.clear();
        txtAutonomia.clear();
        txtAno.clear();

        lblMensagem.setText("");
        definirEstadoInicialBotoes();
        tblAvioes.getSelectionModel().clearSelection();
        txtModelo.requestFocus();
    }

    @FXML
    private void btnSalvarAction(ActionEvent evento) {
        if (!saoCamposValidos()) return;

        try {
            AviaoDTO aviaoDTO = new AviaoDTO();
            aviaoDTO.setModelo(txtModelo.getText());
            aviaoDTO.setFabricante(txtFabricante.getText());
            aviaoDTO.setCapacidadePassageiros(Integer.parseInt(txtCapacidade.getText()));
            aviaoDTO.setAutonomiaKm(Integer.parseInt(txtAutonomia.getText()));
            aviaoDTO.setAnoFabricacao(Integer.parseInt(txtAno.getText()));

            AviaoDAO aviaoDAO = new AviaoDAO();
            aviaoDAO.cadastrarAviao(aviaoDTO);

            carregarTabelaAvioes();
            btnLimparAction(null);
            exibirMensagemFeedback("Avião cadastrado com sucesso!", true);
        } catch (Exception e) {
            exibirMensagemFeedback("Erro ao salvar no banco de dados.", false);
            // Reforço em caso de erro crítico no banco
            DialogUtil.showError("Ocorreu um erro inesperado ao tentar salvar a aeronave no banco de dados.");
        }
    }

    @FXML
    private void btnAlterarAction(ActionEvent evento) {
        if (txtId.getText().isEmpty()) return;
        if (!saoCamposValidos()) return;

        try {
            AviaoDTO aviaoDTO = new AviaoDTO();
            aviaoDTO.setId(Integer.parseInt(txtId.getText()));
            aviaoDTO.setModelo(txtModelo.getText());
            aviaoDTO.setFabricante(txtFabricante.getText());
            aviaoDTO.setCapacidadePassageiros(Integer.parseInt(txtCapacidade.getText()));
            aviaoDTO.setAutonomiaKm(Integer.parseInt(txtAutonomia.getText()));
            aviaoDTO.setAnoFabricacao(Integer.parseInt(txtAno.getText()));

            AviaoDAO aviaoDAO = new AviaoDAO();
            aviaoDAO.atualizarAviao(aviaoDTO);

            carregarTabelaAvioes();
            btnLimparAction(null);
            exibirMensagemFeedback("Dados atualizados com sucesso!", true);
        } catch (Exception e) {
            exibirMensagemFeedback("Erro ao atualizar aeronave.", false);
            // Reforço em caso de erro crítico no banco
            DialogUtil.showError("Ocorreu um erro inesperado ao tentar atualizar os dados da aeronave.");
        }
    }

    @FXML
    private void btnExcluirAction(ActionEvent evento) {
        if (txtId.getText().isEmpty()) return;

        // MOMENTO CRÍTICO - Confirmação antes de ação irreversivel
        boolean confirmado = DialogUtil.showConfirmation("Atenção: Tem certeza que deseja excluir esta aeronave? Esta ação não pode ser desfeita.");
        if (!confirmado) return; // Se o usuário cancelar, a execução para aqui

        try {
            int idAviao = Integer.parseInt(txtId.getText());
            AviaoDAO aviaoDAO = new AviaoDAO();
            aviaoDAO.excluirAviao(idAviao);

            carregarTabelaAvioes();
            btnLimparAction(null);
            exibirMensagemFeedback("Aeronave excluída com sucesso!", true);
        } catch (Exception e) {
            exibirMensagemFeedback("Erro ao excluir aeronave.", false);
            // MOMENTO CRÍTICO Erro grave de infraestrutura
            DialogUtil.showError("Falha crítica ao tentar excluir a aeronave do banco de dados.");
        }
    }
}