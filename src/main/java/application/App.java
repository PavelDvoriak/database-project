package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        VBox root = loader.load();
//        LoginController controller = loader.getController();
//        controller.initialise();

        stage.setScene(new Scene(root));
        stage.setTitle("GEMRVWR");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
