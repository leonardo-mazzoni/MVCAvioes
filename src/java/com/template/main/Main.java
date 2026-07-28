package com.template.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // FXMLLoader loader = new FXMLLoader(Main.class.getResource("main.fxml"));
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/template/main.fxml"));
        Scene scene = new Scene(loader.load(), 850, 700);

        stage.setTitle("Gestão de Frota - Aviação");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}