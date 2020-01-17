package controller;

import application.App;
import dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Errors;
import model.MessageBox;
import model.User;
import service.UserService;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController {
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtPasswordCheck;
    @FXML
    private TextField txtEmail;
    @FXML
    private Button btnRegister;
    @FXML
    private TextField txtUserName;

    private UserService userService;

    public RegisterController() {
        userService = new UserService(new UserDao());
    }

    public void btnRegister_click(ActionEvent actionEvent) {
        EntityManager entityManager = App.createEM();

        String userName = txtUserName.getText();
        String password = txtPassword.getText();
        String passwordCheck = txtPasswordCheck.getText();
        String email = txtEmail.getText().toLowerCase();

        Errors errors = userService.registerCheck(userName, password, passwordCheck, email, entityManager);

        if(!errors.isErrors()) {
            userService.createNewUser(userName, password, email, entityManager);
        } else {
            MessageBox.showError("Registration failed", "Given attributes are invalid", errors.getErrors());
        }
    }


}
