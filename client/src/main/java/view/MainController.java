package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class MainController {

    @FXML
    private TextArea textArea;

    @FXML
    private Label fileNameLabel;

    @FXML
    private ChoiceBox<String> methodChoiceBox;

    private Stage stage;

    private MainController(Stage stage) {
        this.stage = stage;
    }

    private final FileChooser fileChooser = new FileChooser();

    private boolean isCanProcess = false;
    private File file;

    @FXML
    void initialize() {
        methodChoiceBox.getItems().addAll("LZ77", "LZSS", "LZ78", "LZW");
        methodChoiceBox.setValue("LZ77");
    }

    @FXML
    void openButtonOnAction(ActionEvent event) {
        file = fileChooser.showOpenDialog(this.stage);
        if (file != null) {
            fileNameLabel.setText(file.getPath());
            StringBuilder resultStringBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line).append('\n');
                }
                textArea.setText(resultStringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void compressButtonOnAction(ActionEvent event) {
        if (file == null) {
            showNoFileAlertDialog();
            return;
        }

    }

    @FXML
    void decodeButtonOnAction(ActionEvent event) {
        if (file == null) {
            showNoFileAlertDialog();
            return;
        }

    }

    private void showNoFileAlertDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Файл не выбран");
        alert.showAndWait();
    }

    public static Stage createView(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainController.class.getResource("/view/fxml/main.fxml"));
        try {
            loader.setController(new MainController(primaryStage));
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
