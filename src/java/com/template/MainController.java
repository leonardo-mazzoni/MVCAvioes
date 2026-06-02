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
    private void btnLimparAction(ActionEvent event) {
        txtId.clear();
        txtModelo.clear();
        txtFabricante.clear();
        txtCapacidade.clear();
        txtAutonomia.clear();
        txtAno.clear();
    }

    @FXML
    private void btnSalvarAction(ActionEvent event) {
        try {
            // Verificação de segurança: impede que o código quebre se os campos numéricos estiverem vazios
            if (txtCapacidade.getText().isEmpty() || txtAutonomia.getText().isEmpty() || txtAno.getText().isEmpty()) {
                logger.warning("Por favor, preencha Capacidade, Autonomia e Ano antes de salvar.");
                return; // Interrompe a execução aqui, sem tentar salvar
            }

            AviaoDTO aviaodto = new AviaoDTO();

            // NÃO lemos o txtId.getText() aqui. O DTO assumirá o valor padrão (0),
            // que será ignorado pelo DAO durante o INSERT.
            aviaodto.setModelo(txtModelo.getText());
            aviaodto.setFabricante(txtFabricante.getText());
            aviaodto.setCapacidadePassageiros(Integer.parseInt(txtCapacidade.getText()));
            aviaodto.setAutonomiaKm(Integer.parseInt(txtAutonomia.getText()));
            aviaodto.setAnoFabricacao(Integer.parseInt(txtAno.getText()));

            AviaoDAO dao = new AviaoDAO();
            dao.cadastrarAviao(aviaodto);

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

            AviaoDTO aviaodto = new AviaoDTO();

            // Agora é seguro pegar o ID, pois sabemos que ele tem um valor
            aviaodto.setId(Integer.parseInt(txtId.getText()));
            aviaodto.setModelo(txtModelo.getText());
            aviaodto.setFabricante(txtFabricante.getText());
            aviaodto.setCapacidadePassageiros(Integer.parseInt(txtCapacidade.getText()));
            aviaodto.setAutonomiaKm(Integer.parseInt(txtAutonomia.getText()));
            aviaodto.setAnoFabricacao(Integer.parseInt(txtAno.getText()));

            AviaoDAO dao = new AviaoDAO();
            dao.atualizarAviao(aviaodto);

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
            // Evita a quebra do programa se o botão for clicado sem um avião selecionado
            if (txtId.getText().isEmpty()) {
                logger.warning("Selecione um avião na tabela primeiro para poder excluí-lo.");
                return;
            }

            int id = Integer.parseInt(txtId.getText());

            AviaoDAO dao = new AviaoDAO();
            dao.excluirAviao(id);

            // Atualiza a tabela na tela e limpa os campos
            carregarAvioes();
            btnLimparAction(null);

        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Tentativa de exclusão com ID inválido.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao processar exclusão de avião.", e);
        }
    }

    @FXML
    private void carregarAvioes() {
        AviaoDAO objetoAviaoDao = new AviaoDAO();
        ArrayList<AviaoDTO> listaAvioes = objetoAviaoDao.listarTodos();
        tblAvioes.setItems(FXCollections.observableArrayList(listaAvioes));
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colFabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
        colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidadePassageiros"));
        colAutonomia.setCellValueFactory(new PropertyValueFactory<>("autonomiaKm"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("anoFabricacao"));

        carregarAvioes();
    }

    @FXML
    private void carregarCampos(MouseEvent event) {
        AviaoDTO aviaoDTO = tblAvioes.getSelectionModel().getSelectedItem();

        if(aviaoDTO != null){
            txtId.setText(String.valueOf(aviaoDTO.getId()));
            txtModelo.setText(aviaoDTO.getModelo());
            txtFabricante.setText(aviaoDTO.getFabricante());
            txtCapacidade.setText(String.valueOf(aviaoDTO.getCapacidadePassageiros()));
            txtAutonomia.setText(String.valueOf(aviaoDTO.getAutonomiaKm()));
            txtAno.setText(String.valueOf(aviaoDTO.getAnoFabricacao()));
        }
    }
}
