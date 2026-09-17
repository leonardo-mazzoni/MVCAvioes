package com.template.main;

import com.template.factory.ControllerFactory;
import com.template.services.AviaoService;
import com.template.services.IAviaoService;
import com.template.validator.AviaoValidador;
import com.template.validator.IAviaoValidador;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // 1. Instancia as dependencias concretas aqui fora (bootstrap da aplicacao)
        IAviaoService aviaoService = new AviaoService();
        IAviaoValidador aviaoValidador = new AviaoValidador();

        // 2. Cria a fabrica de controllers com as dependencias
        ControllerFactory controllerFactory = new ControllerFactory(aviaoService, aviaoValidador);

        // 3. Diz ao FXMLLoader para usar a nossa fabrica ao carregar o FXML
        FXMLLoader loader = new FXMLLoader();
        URL fxmlLocation = getClass().getResource("/com/template/main.fxml");
        if (fxmlLocation == null) {
            System.err.println("Erro: main.fxml não encontrado. Verifique o caminho.");
            return;
        }
        loader.setLocation(fxmlLocation);
        loader.setControllerFactory(controllerFactory);

        Scene scene = new Scene(loader.load(), 850, 700);

        stage.setTitle("Gestão de Frota - Aviação");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}