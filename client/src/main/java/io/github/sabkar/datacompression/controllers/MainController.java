package io.github.sabkar.datacompression.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.*;
import java.util.function.Consumer;

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
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Текстовые", "*.txt"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );
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
        compressionAction(ioStreamPair -> {
            try {
                while (ioStreamPair.getKey().available() > 0) {
                    ioStreamPair.getValue().write(ioStreamPair.getKey().read());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @FXML
    void decodeButtonOnAction(ActionEvent event) {
        compressionAction(ioStreamPair -> {
            try {
                while (ioStreamPair.getKey().available() > 0) {
                    ioStreamPair.getValue().write(ioStreamPair.getKey().read());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void compressionAction(Consumer<Pair<InputStream, OutputStream>> action) {
        if (file == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Файл не выбран");
            alert.showAndWait();
            return;
        }

        fileChooser.setInitialDirectory(file.getParentFile());
        fileChooser.setInitialFileName(file.getName() + "-" + methodChoiceBox.getValue().toLowerCase());
        File fileToSave = fileChooser.showSaveDialog(this.stage);
        if (fileToSave != null) {
            try (FileOutputStream fos = new FileOutputStream(fileToSave);
                 FileInputStream fis = new FileInputStream(file)) {

                action.accept(new Pair<>(fis, fos));

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
                alert.setTitle("Открыть файл?");
                alert.setHeaderText("Файл успешно создан. Открыть для чтения?");
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    Runtime.getRuntime().exec("notepad " + fileToSave.getPath());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Stage createView(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainController.class.getResource("/view/fxml/main.fxml"));
        try {
            loader.setController(new MainController(primaryStage));
            Parent root = loader.load();
            primaryStage.setTitle("Курсовой проект по дисциплине МСКИТ");
            primaryStage.setScene(new Scene(root, 710, 450));
            Image icon = new Image(MainController.class.getResourceAsStream("/img/compress.png"));
            primaryStage.getIcons().add(icon);
            return primaryStage;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return primaryStage;
    }

}
