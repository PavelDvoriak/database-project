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

/**
 * App class - Main Application class.
 * It sets up needed functions and initializes / launches the GUI
 */
public class App extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    private static EntityManagerFactory EMF;

    /**
     * Method that creates EntityManager from EntityManagerFactory
     *
     * @return Returns entity manager, that is used for data persistence
     */
    public static EntityManager createEM() {
        return EMF.createEntityManager();
    }

    /**
     * Main method of the application, that starts it.
     * It also binds the application with persistence configuration.
     *
     * @param args Array of arguments, that application can be started with
     */
    public static void main(String[] args) {
        LOG.info("Application started");
        EMF = Persistence.createEntityManagerFactory("punit");

        launch();

        EMF.close();
        LOG.info("Application terminated");
    }

    /**
     * Method that launches the GUI.
     *
     * @param stage Stage that is shown when the app starts
     * @throws IOException Exception is thrown when the respective fxml file cannot be found
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/login.fxml"));
        VBox root = loader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("GEMRVWR");
        stage.show();
    }

}
