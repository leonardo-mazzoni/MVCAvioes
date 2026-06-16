package com.template;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent; // Importação essencial para a tabela funcionar

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {

    private static final Logger logger = Logger.getLogger(MainController.class.getName());

    @FXML private Button btnSalvar;
    @FXML private Button btnAlterar;
    @FXML private Button btnExcluir;
    @FXML private Button btnLimpar;
    // ---- ----- ---- ---- --- --- //

    @FXML private TextField txtId;
    @FXML private TextField txtModelo;
    @FXML private TextField txtFabricante;
    @FXML private TextField txtCapacidade;
    @FXML private TextField txtAutonomia;
    @FXML private TextField txtAno;

    // ---- ----- ---- ---- --- --- //

    @FXML private TableView<AviaoDTO> tblAvioes;
    @FXML private TableColumn<AviaoDTO, Integer> colId;
    @FXML private TableColumn<AviaoDTO, String> colModelo;
    @FXML private TableColumn<AviaoDTO, String> colFabricante;
    @FXML private TableColumn<AviaoDTO, Integer> colCapacidade;
    @FXML private TableColumn<AviaoDTO, Integer> colAutonomia;
    @FXML private TableColumn<AviaoDTO, Integer> colAno;
    // ---- ----- ---- ---- --- --- //

    @FXML
    private void initialize() { // Aqui inicia todos os componentes e correlaciona com o código (DTO)
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colFabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
        colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidadePassageiros"));
        colAutonomia.setCellValueFactory(new PropertyValueFactory<>("autonomiaKm"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("anoFabricacao"));

        carregarAvioes();
    }

    @FXML
    private void carregarAvioes() { // Essa função consulta o BD, cria uma lista com os avioes do BD e carrega eles na tabela
        AviaoDAO aviaoDao = new AviaoDAO();
        ArrayList<AviaoDTO> listaAvioes = aviaoDao.listarTodos();
        tblAvioes.setItems(FXCollections.observableArrayList(listaAvioes));
    }

    @FXML
    private void carregarCampos(MouseEvent event) { // Função para carregar o aviao selecionado na tabela para os textfields
        AviaoDTO aviaoDto = tblAvioes.getSelectionModel().getSelectedItem();

        if (aviaoDto != null) {
            txtId.setText(String.valueOf(aviaoDto.getId()));
            txtModelo.setText(aviaoDto.getModelo());
            txtFabricante.setText(aviaoDto.getFabricante());
            txtCapacidade.setText(String.valueOf(aviaoDto.getCapacidadePassageiros()));
            txtAutonomia.setText(String.valueOf(aviaoDto.getAutonomiaKm()));
            txtAno.setText(String.valueOf(aviaoDto.getAnoFabricacao()));
        }
    }

    @FXML
    private void btnLimparAction(ActionEvent event) { // Função para limpar os textfields
        txtId.clear();
        txtModelo.clear();
        txtFabricante.clear();
        txtCapacidade.clear();
        txtAutonomia.clear();
        txtAno.clear();
    }

    @FXML
    private void btnSalvarAction(ActionEvent event) { // Aqui realiza toda a ação após salvar
        try {
            // Verificação de segurança: impede que o código quebre se os campos numéricos estiverem vazios
            if (txtCapacidade.getText().isEmpty() || txtAutonomia.getText().isEmpty() || txtAno.getText().isEmpty()) {
                logger.warning("Por favor, preencha Capacidade, Autonomia e Ano antes de salvar.");
                return; // Interrompe a execução aqui, sem tentar salvar
            }

            AviaoDTO aviaoDto = new AviaoDTO();
            aviaoDto.setModelo(txtModelo.getText());
            aviaoDto.setFabricante(txtFabricante.getText());
            aviaoDto.setCapacidadePassageiros(Integer.parseInt(txtCapacidade.getText()));
            aviaoDto.setAutonomiaKm(Integer.parseInt(txtAutonomia.getText()));
            aviaoDto.setAnoFabricacao(Integer.parseInt(txtAno.getText()));

            AviaoDAO aviaoDao = new AviaoDAO();
            aviaoDao.cadastrarAviao(aviaoDto);

            // Atualiza a tabela na tela e limpa os campos
            carregarAvioes();
            btnLimparAction(null);

        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Erro: Letras inseridas onde deveriam ser números.", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao salvar avião.", e);
        }
    }

    @FXML
    private void btnAlterarAction(ActionEvent event) {
        try {
            // Se o campo ID está vazio, significa que nenhum avião foi selecionado na tabela
            if (txtId.getText().isEmpty()) {
                logger.warning("Selecione um avião na tabela primeiro para poder alterá-lo.");
                return;
            }

            AviaoDTO aviaoDto = new AviaoDTO();

            // Agora é seguro pegar o ID, pois sabemos que ele tem um valor
            aviaoDto.setId(Integer.parseInt(txtId.getText()));
            aviaoDto.setModelo(txtModelo.getText());
            aviaoDto.setFabricante(txtFabricante.getText());
            aviaoDto.setCapacidadePassageiros(Integer.parseInt(txtCapacidade.getText()));
            aviaoDto.setAutonomiaKm(Integer.parseInt(txtAutonomia.getText()));
            aviaoDto.setAnoFabricacao(Integer.parseInt(txtAno.getText()));

            AviaoDAO aviaoDao = new AviaoDAO();
            aviaoDao.atualizarAviao(aviaoDto);

            // Atualiza a tabela na tela e limpa os campos
            carregarAvioes();
            btnLimparAction(null);

        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Erro de conversão: Campos numéricos inválidos no formulário.", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Falha crítica ao tentar alterar avião.", e);
        }
    }

    @FXML
    private void btnExcluirAction(ActionEvent event) {
        try {
            if (txtId.getText().isEmpty()) { // Se não foi clicado em um avião e clicou no botão excluir
                logger.warning("Selecione um avião na tabela primeiro para poder excluí-lo.");
                return;
            }

            int id = Integer.parseInt(txtId.getText());

            AviaoDAO aviaoDao = new AviaoDAO();
            aviaoDao.excluirAviao(id);

            // Atualiza a tabela na tela e limpa os campos
            carregarAvioes();
            btnLimparAction(null);

        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Tentativa de exclusão com ID inválido.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao processar exclusão de avião.", e);
        }
    }
}