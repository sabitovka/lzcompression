package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class MainView {

    @FXML
    public TextArea sourceText;

    private Stage stage;

    private MainView(Stage stage) {
        this.stage = stage;
    }

    final FileChooser fileChooser = new FileChooser();

    @FXML
    void onOpenMenuItemSelected(ActionEvent event) {
        File file = fileChooser.showOpenDialog(this.stage);
        if (file != null) {
            StringBuilder resultStringBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line).append('\n');
                }
                sourceText.setText(resultStringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Stage newInstance(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainView.class.getResource("/view/fxml/main.fxml"));
        try {
            loader.setController(new MainView(primaryStage));
            Parent root = loader.load();
            primaryStage.setTitle("Курсовой проект по дисциплине МСКИТ");
            primaryStage.setScene(new Scene(root, 710, 450));
            return primaryStage;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return primaryStage;
    }

}
