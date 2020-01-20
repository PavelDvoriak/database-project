package model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * MessageBox class - class that show different type of messages to the GUI
 * using JavaFX Alert
 * It is used to pass an error message to the User
 * or when User's confirmation is needed.
 *
 * @author Pavel Dvoriak
 * @version 20.01.2020
 */
public class MessageBox {

    /**
     * Method that creates an Alert object, which type is Error.
     * Append given texts to the MessageBox and shows it to the User.
     *
     * @param windowTitle Title of the window shown to the user
     * @param header Header of the window shown to the user
     * @param txt Content of the message shown to the user
     */
    public static final void showError(String windowTitle, String header, String txt) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(windowTitle);
        alert.setHeaderText(header);
        alert.setContentText(txt);
        alert.showAndWait();
    }

    /**
     * Method that creates an Alert object, which type is Confirmation.
     * Append given texts to the MessageBox and shows it to the User.
     * Also waits for the users' confirmation to proceed.
     *
     * @param windowTitle Title of the window shown to the user
     * @param header Header of the window shown to the user
     * @param txt Content of the message shown to the user
     * @return True if user consent the message, false otherwise
     */
    public static final boolean showConfirm(String windowTitle, String header, String txt) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(windowTitle);
        alert.setHeaderText(header);
        alert.setContentText(txt);
        Optional<ButtonType> answer = alert.showAndWait();
        return (answer.get() == ButtonType.OK);
    }
}
