package com.template.factory;

import com.template.controller.MainController;
import com.template.services.IAviaoService;
import com.template.validator.IAviaoValidador;
import javafx.util.Callback;

// Fabrica de Controllers do JavaFX: injeta as dependencias ao carregar o FXML
public class ControllerFactory implements Callback<Class<?>, Object> {

    private final IAviaoService aviaoService;
    private final IAviaoValidador aviaoValidador;

    // Recebe as instancias concretas criadas no Main
    public ControllerFactory(IAviaoService aviaoService, IAviaoValidador aviaoValidador) {
        this.aviaoService = aviaoService;
        this.aviaoValidador = aviaoValidador;
    }

    @Override
    public Object call(Class<?> classeController) {
        if (classeController == MainController.class) {
            // Injeta as interfaces de servico e validacao no construtor do controller
            return new MainController(this.aviaoService, this.aviaoValidador);
        }

        try {
            return classeController.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao instanciar o Controller na fábrica: " + classeController.getName(), e);
        }
    }
}
