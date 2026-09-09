package com.template.factory;

import com.template.controller.MainController;
import com.template.services.IAviaoService;
import com.template.validator.IAviaoValidador;
import javafx.util.Callback;

/**
 * Fábrica de Controladores para o JavaFX.
 * Implementa Callback<Class<?>, Object> e fornece a injeção de dependências
 * necessária para os Controllers ao carregar arquivos FXML.
 * Baseado nos Slides 31 e 32 (Inversão de Dependência e Injeção com FXMLLoader).
 */
public class ControllerFactory implements Callback<Class<?>, Object> {

    private final IAviaoService aviaoService;
    private final IAviaoValidador aviaoValidador;

    public ControllerFactory(IAviaoService aviaoService, IAviaoValidador aviaoValidador) {
        this.aviaoService = aviaoService;
        this.aviaoValidador = aviaoValidador;
    }

    @Override
    public Object call(Class<?> classeController) {
        if (classeController == MainController.class) {
            // Injeção de dependência via construtor com abstrações (interfaces)
            return new MainController(this.aviaoService, this.aviaoValidador);
        }

        try {
            return classeController.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao instanciar o Controller na fábrica: " + classeController.getName(), e);
        }
    }
}
