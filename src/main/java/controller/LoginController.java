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
    on login shutdown former window
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

    public LoginController() {
        this.userService = new UserService(new UserDao());
    }

    public void btnLogin_click(MouseEvent mouseEvent) {
        EntityManager em = App.createEM();
        String uName = txtUserName.getText().trim().toLowerCase();
        String pass = txtPassword.getText();

        try {
            userToSign = userService.checkLogin(uName, pass, em);
            if (userToSign != null) {
                //get logged user interface here
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

    public void btnRegister_click(ActionEvent actionEvent) {
        //load register.fxml
        System.out.println("U clicked register! Cool");
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
