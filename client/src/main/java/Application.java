import javafx.stage.Stage;
import view.MainView;

public class Application extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MainView.newInstance(stage).show();
    }

}
