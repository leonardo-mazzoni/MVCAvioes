package com.template;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController
{

    private static final Logger logger = Logger.getLogger(MainController.class.getName());

    @FXML private Button btnSalvar;
    @FXML private Button btnAlterar;
    @FXML private Button btnExcluir;
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
    private void btnSalvarAction (ActionEvent event){
        String id = txtId.getText();
        String modelo = txtModelo.getText();
        String fabricante = txtFabricante.getText();
        String capacidade = txtCapacidade.getText();
        String autonomia = txtAutonomia.getText();
        String ano = txtAno.getText();

        AviaoDTO aviaodto = new AviaoDTO();

        aviaodto.setId(Integer.parseInt(id));
        aviaodto.setModelo(modelo);
        aviaodto.setFabricante(fabricante);
        aviaodto.setCapacidadePassageiros(Integer.parseInt(capacidade));
        aviaodto.setAutonomiaKm(Integer.parseInt(autonomia));
        aviaodto.setAnoFabricacao(Integer.parseInt(ano));

        AviaoDAO aviaoUmdao = new AviaoDAO();
        aviaoUmdao.cadastrarAviao(aviaodto);


    }

    @FXML
    private void btnAlterarAction (ActionEvent event){
        try {
            AviaoDTO aviaodto = new AviaoDTO();

            aviaodto.setId(Integer.parseInt(txtId.getText()));
            aviaodto.setModelo(txtModelo.getText());
            aviaodto.setFabricante(txtFabricante.getText());
            aviaodto.setCapacidadePassageiros(Integer.parseInt(txtCapacidade.getText()));
            aviaodto.setAutonomiaKm(Integer.parseInt(txtAutonomia.getText()));
            aviaodto.setAnoFabricacao(Integer.parseInt(txtAno.getText()));

            AviaoDAO dao = new AviaoDAO();
            dao.atualizarAviao(aviaodto);

            logger.info("Avião ID " + aviaodto.getId() + " atualizado com sucesso no sistema.");

        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Erro de conversão: Campos numéricos inválidos no formulário.", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Falha crítica ao tentar alterar avião.", e);
        }
    }

    @FXML
    private void btnExcluirAction (ActionEvent event){
        try {
            int id = Integer.parseInt(txtId.getText());

            AviaoDAO dao = new AviaoDAO();
            dao.excluirAviao(id);

            logger.info("Avião ID " + id + " removido com sucesso pelo controlador.");

        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Tentativa de exclusão com ID inválido: " + txtId.getText());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao processar exclusão de avião.", e);
        }
    }

    @FXML
    private void initialize()
    {
        System.out.println("FXML loaded successfully!");
    }
}