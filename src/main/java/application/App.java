package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;

public class App extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    private static EntityManagerFactory EMF;

    public static EntityManager createEM() {
        return EMF.createEntityManager();
    }

    public static void main(String[] args) {
        LOG.info("Application started");
        EMF = Persistence.createEntityManagerFactory("punit");

        launch();

        EMF.close();
        LOG.info("Application terminated");
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/login.fxml"));
        VBox root = loader.load();
//        LoginController controller = loader.getController();
//        controller.initialise();

        stage.setScene(new Scene(root));
        stage.setTitle("GEMRVWR");
        stage.show();
    }

}
