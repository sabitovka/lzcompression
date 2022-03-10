package io.github.sabkar.lzcompression.client;

import io.github.sabkar.lzcompression.client.controllers.MainController;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MainController.createView(stage).show();
    }

}
