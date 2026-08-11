package com.template.services;

import com.template.model.dto.AviaoDTO;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.paint.Color;

public class LayoutServices {

    /**
     * Aplica filtro nos TextFields informados para permitirem apenas dígitos de 0 a 9.
     */
    public static void aplicarFiltrosEntradaNumerica(TextField... campos) {
        for (TextField campo : campos) {
            campo.setTextFormatter(new TextFormatter<>(mudanca ->
                    mudanca.getText().matches("[0-9]*") ? mudanca : null));
        }
    }

    /**
     * Atualiza o texto e a cor da Label de feedback visual do usuário.
     */
    public static void exibirMensagemFeedback(Label lblMensagem, String mensagem, boolean isSucesso) {
        lblMensagem.setText(mensagem);
        lblMensagem.setTextFill(isSucesso ? Color.GREEN : Color.RED);
    }

    /**
     * Alterna o estado dos botões entre o modo de novo cadastro e o modo de alteração/exclusão.
     */
    public static void configurarEstadoBotoes(Button btnSalvar, Button btnAlterar, Button btnExcluir, boolean modoEdicao) {
        btnSalvar.setDisable(modoEdicao);
        btnAlterar.setDisable(!modoEdicao);
        btnExcluir.setDisable(!modoEdicao);
    }

    /**
     * Povoa os campos da tela com os dados do DTO selecionado na TableView e ajusta os botões.
     */
    public static void preencherCampos(AviaoDTO aviao,
                                       TextField txtId, TextField txtModelo, TextField txtFabricante,
                                       TextField txtCapacidade, TextField txtAutonomia, TextField txtAno,
                                       Button btnSalvar, Button btnAlterar, Button btnExcluir,
                                       Label lblMensagem) {
        if (aviao == null) return;

        txtId.setText(String.valueOf(aviao.getId()));
        txtModelo.setText(aviao.getModelo());
        txtFabricante.setText(aviao.getFabricante());
        txtCapacidade.setText(String.valueOf(aviao.getCapacidadePassageiros()));
        txtAutonomia.setText(String.valueOf(aviao.getAutonomiaKm()));
        txtAno.setText(String.valueOf(aviao.getAnoFabricacao()));

        configurarEstadoBotoes(btnSalvar, btnAlterar, btnExcluir, true);
        lblMensagem.setText("");
    }

    /**
     * Limpa todos os campos informados, reseta o estado da tela, limpa a seleção da tabela e foca no campo inicial.
     */
    public static void limparFormulario(Label lblMensagem, TableView<?> tblAvioes,
                                        Button btnSalvar, Button btnAlterar, Button btnExcluir,
                                        TextField txtCampoFoco, TextField... camposParaLimpar) {
        for (TextField campo : camposParaLimpar) {
            campo.clear();
        }

        lblMensagem.setText("");
        configurarEstadoBotoes(btnSalvar, btnAlterar, btnExcluir, false);

        if (tblAvioes != null) {
            tblAvioes.getSelectionModel().clearSelection();
        }
        if (txtCampoFoco != null) {
            txtCampoFoco.requestFocus();
        }
    }
}