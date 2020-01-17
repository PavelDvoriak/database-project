package model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class MessageBox {

    public static final void showError(String windowTitle, String header, String txt) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(windowTitle);
        alert.setHeaderText(header);
        alert.setContentText(txt);
        alert.showAndWait();
    }

    public static final boolean showConfirm(String windowTitle, String header, String txt) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(windowTitle);
        alert.setHeaderText(header);
        alert.setContentText(txt);
        Optional<ButtonType> answer = alert.showAndWait();
        return (answer.get() == ButtonType.OK);
    }
}
