package controller;

import application.App;
import dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.MessageBox;
import model.User;
import service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.io.IOException;

/* TODO:
    Add pasword hashing
 */

/**
 * LoginController class - class that controls part of GUI - login.
 * It contains objects used for controlling the scene.
 * It is further implemented by user service, that is responsible for business rules
 *
 * @author Pavel Dvoriak
 * @version 20.01.2020
 */
public class LoginController {
    @FXML
    private Label lblInfo;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnRegister;
    @FXML
    private TextField txtUserName;
    @FXML
    private PasswordField txtPassword;

    private UserService userService;
    private User userToSign;

    /**
     * Constructor that creates an instance of this controller.
     * It then assigns needed service for self
     */
    public LoginController() {
        this.userService = new UserService(new UserDao());
    }

    /**
     * Method that implements functionality of login button, if it is clicked.
     * Performs basic checks if given attributes are valid.
     * If they are it calls function to log the user in.
     *
     * @param mouseEvent
     */
    public void btnLogin_click(MouseEvent mouseEvent) {
        EntityManager em = App.createEM();
        String uName = txtUserName.getText().trim().toLowerCase();
        String pass = txtPassword.getText();

        try {
            userToSign = userService.checkLogin(uName, pass, em);
            if (userToSign != null) {
                login(uName);
            } else {
                MessageBox.showError("Login failed", "Wrong username/password combination",
                        "Try again or create free account\nTo do that just click the register button");
                txtUserName.clear();
                txtPassword.clear();
            }
        } catch (NoResultException e) {
            MessageBox.showError("Login failed", "No existing user with given username",
                    "You can create free account by clicking the register button");
        } catch (IOException e) {
            e.printStackTrace();
        }
        em.close();
    }

    /**
     * Method that implements functionality of register button, if it is clicked.
     * Loads the registration GUI and inits modality.
     *
     * @param actionEvent
     */
    public void btnRegister_click(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/register.fxml"));

        VBox root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            // TODO - add alert
            e.printStackTrace();
        }

        Stage owner = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        Stage registerWindow = new Stage();

        registerWindow.setScene(new Scene(root));
        registerWindow.setTitle("Registration");
        registerWindow.initModality(Modality.WINDOW_MODAL);
        registerWindow.initOwner(owner);
        registerWindow.show();
    }

    /**
     * Method is used for user login. It takes username as parameter and loads corresponding GUI.
     * It also creates mentioned GUI controller and passes signed user object to it.
     *
     * @param uName Username of the user that is logging in
     * @throws IOException Exception is thrown when the respective fxml file cannot be found
     */
    private void login(String uName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/home.fxml"));
        VBox root = loader.load();

        HomeController controller = loader.getController();
        controller.initialise(userToSign);

        Stage homeWindow = new Stage();
        Stage currentWindow = (Stage) btnLogin.getScene().getWindow();

        homeWindow.setScene(new Scene(root));
        homeWindow.setTitle("GEMRVWR - " + uName + " logged in");
        homeWindow.show();
        currentWindow.hide();
    }
}
