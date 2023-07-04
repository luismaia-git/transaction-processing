package com.scheduler.sgbdtrab2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main.fxml"));

        Scene SceneMain = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setTitle("Gerenciador de transações");
        stage.setScene(SceneMain);
        stage.show();

        MainController mainController = fxmlLoader.getController();
        mainController.setParentStage(stage);

    }

    public static void main(String[] args) {
        launch();
    }
}