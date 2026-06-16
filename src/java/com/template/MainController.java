package com.template;

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
        // Vincula as colunas aos atributos do DTO
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colFabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
        colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidadePassageiros"));
        colAutonomia.setCellValueFactory(new PropertyValueFactory<>("autonomiaKm"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("anoFabricacao"));

        aplicarFiltrosNumericos();
        estadoInicialBotoes();
        carregarAvioes();
    }

    // Impede que o usuário digite letras em campos numéricos
    private void aplicarFiltrosNumericos() {
        txtCapacidade.setTextFormatter(new TextFormatter<>(change -> change.getText().matches("[0-9]*") ? change : null));
        txtAutonomia.setTextFormatter(new TextFormatter<>(change -> change.getText().matches("[0-9]*") ? change : null));
        txtAno.setTextFormatter(new TextFormatter<>(change -> change.getText().matches("[0-9]*") ? change : null));
    }

    // Habilitar ou desabilitar campos de acordo com a ação
    private void estadoInicialBotoes() {
        btnSalvar.setDisable(false);
        btnAlterar.setDisable(true);
        btnExcluir.setDisable(true);
    }

    private void exibirMensagem(String mensagem, boolean sucesso) {
        lblMensagem.setText(mensagem);
        lblMensagem.setTextFill(sucesso ? Color.GREEN : Color.RED);
    }

    private boolean validarCampos() {
        if (txtModelo.getText().trim().isEmpty() || txtFabricante.getText().trim().isEmpty() ||
                txtCapacidade.getText().trim().isEmpty() || txtAutonomia.getText().trim().isEmpty() ||
                txtAno.getText().trim().isEmpty()) {
            exibirMensagem("Erro: Preencha todos os campos obrigatórios (*).", false);
            return false;
        }
        return true;
    }

    @FXML
    private void carregarAvioes() {
        try {
            AviaoDAO aviaoDao = new AviaoDAO();
            ArrayList<AviaoDTO> listaAvioes = aviaoDao.listarTodos();
            tblAvioes.setItems(FXCollections.observableArrayList(listaAvioes));
        } catch (Exception e) {
            exibirMensagem("Erro ao carregar dados do banco.", false);
        }
    }

    @FXML
    private void carregarCampos(MouseEvent event) {
        AviaoDTO aviaoDto = tblAvioes.getSelectionModel().getSelectedItem();

        if (aviaoDto != null) {
            txtId.setText(String.valueOf(aviaoDto.getId()));
            txtModelo.setText(aviaoDto.getModelo());
            txtFabricante.setText(aviaoDto.getFabricante());
            txtCapacidade.setText(String.valueOf(aviaoDto.getCapacidadePassageiros()));
            txtAutonomia.setText(String.valueOf(aviaoDto.getAutonomiaKm()));
            txtAno.setText(String.valueOf(aviaoDto.getAnoFabricacao()));

            // Ao selecionar registro, bloqueia Salvar e libera Alterar/Excluir
            btnSalvar.setDisable(true);
            btnAlterar.setDisable(false);
            btnExcluir.setDisable(false);
            lblMensagem.setText(""); // Limpa mensagens anteriores
        }
    }

    @FXML
    private void btnLimparAction(ActionEvent event) {
        txtId.clear();
        txtModelo.clear();
        txtFabricante.clear();
        txtCapacidade.clear();
        txtAutonomia.clear();
        txtAno.clear();

        lblMensagem.setText("");
        estadoInicialBotoes();
        tblAvioes.getSelectionModel().clearSelection();
        txtModelo.requestFocus(); // Retorna o cursor pro primeiro campo
    }

    @FXML
    private void btnSalvarAction(ActionEvent event) {
        if (!validarCampos()) return;

        try {
            AviaoDTO aviaoDto = new AviaoDTO();
            aviaoDto.setModelo(txtModelo.getText());
            aviaoDto.setFabricante(txtFabricante.getText());
            aviaoDto.setCapacidadePassageiros(Integer.parseInt(txtCapacidade.getText()));
            aviaoDto.setAutonomiaKm(Integer.parseInt(txtAutonomia.getText()));
            aviaoDto.setAnoFabricacao(Integer.parseInt(txtAno.getText()));

            AviaoDAO aviaoDao = new AviaoDAO();
            aviaoDao.cadastrarAviao(aviaoDto);

            carregarAvioes();
            btnLimparAction(null);
            exibirMensagem("Avião cadastrado com sucesso!", true);
        } catch (Exception e) {
            exibirMensagem("Erro ao salvar no banco de dados.", false);
        }
    }

    @FXML
    private void btnAlterarAction(ActionEvent event) {
        if (txtId.getText().isEmpty()) return;
        if (!validarCampos()) return;

        try {
            AviaoDTO aviaoDto = new AviaoDTO();
            aviaoDto.setId(Integer.parseInt(txtId.getText()));
            aviaoDto.setModelo(txtModelo.getText());
            aviaoDto.setFabricante(txtFabricante.getText());
            aviaoDto.setCapacidadePassageiros(Integer.parseInt(txtCapacidade.getText()));
            aviaoDto.setAutonomiaKm(Integer.parseInt(txtAutonomia.getText()));
            aviaoDto.setAnoFabricacao(Integer.parseInt(txtAno.getText()));

            AviaoDAO aviaoDao = new AviaoDAO();
            aviaoDao.atualizarAviao(aviaoDto);

            carregarAvioes();
            btnLimparAction(null);
            exibirMensagem("Dados atualizados com sucesso!", true);
        } catch (Exception e) {
            exibirMensagem("Erro ao atualizar aeronave.", false);
        }
    }

    @FXML
    private void btnExcluirAction(ActionEvent event) {
        if (txtId.getText().isEmpty()) return;

        try {
            int id = Integer.parseInt(txtId.getText());
            AviaoDAO aviaoDao = new AviaoDAO();
            aviaoDao.excluirAviao(id);

            carregarAvioes();
            btnLimparAction(null);
            exibirMensagem("Aeronave excluída com sucesso!", true);
        } catch (Exception e) {
            exibirMensagem("Erro ao excluir aeronave.", false);
        }
    }
}