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
import service.UserService;

import javax.persistence.EntityManager;

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

        String userName = txtUserName.getText().trim().toLowerCase();
        String password = txtPassword.getText();
        String passwordCheck = txtPasswordCheck.getText();
        String email = txtEmail.getText().toLowerCase();

        Errors errors = userService.checkRegistration(userName, password, passwordCheck, email, entityManager);

        if(!errors.containErrors()) {
            userService.createNewUser(userName, password, email, entityManager);
        } else {
            MessageBox.showError("Registration failed", "Given attributes are invalid", errors.getErrors());
        }
    }


}
