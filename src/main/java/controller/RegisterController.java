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

/**
 * RegistrationController class - class that controls part of GUI - registration.
 * It contains objects used for controlling the scene.
 * It is further implemented by user service, that is responsible for bussines rules
 *
 * @author Pavel Dvoriak
 * @version 20.01.2020
 */
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

    /**
     * Constructor that creates an instance of this controller.
     * It then assigns needed service for self
     */
    public RegisterController() {
        userService = new UserService(new UserDao());
    }

    /**
     * Method that implements functionality of register confirmation button, if it is clicked.
     * Performs basic checks if given attributes are valid.
     * If they are it calls function to register a new user using the business and data layers.
     *
     * @param actionEvent
     */
    public void btnRegister_click(ActionEvent actionEvent) {
        EntityManager entityManager = App.createEM();

        String userName = txtUserName.getText().trim().toLowerCase();
        String password = txtPassword.getText();
        String passwordCheck = txtPasswordCheck.getText();
        String email = txtEmail.getText().toLowerCase();

        Errors errors = userService.checkRegistration(userName, password, passwordCheck, email, entityManager);

        if (!errors.containErrors()) {
            userService.createNewUser(userName, password, email, entityManager);
        } else {
            MessageBox.showError("Registration failed", "Given attributes are invalid", errors.getErrors());
        }
    }


}
