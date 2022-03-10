package io.github.sabkar.datacompression;

import io.github.sabkar.datacompression.controllers.MainController;
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
