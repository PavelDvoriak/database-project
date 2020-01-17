package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;


public class HomeController {
    @FXML
    Button btnWriteReview;

    public void initialise() {

    }

    public void btnWriteReview_click(MouseEvent mouseEvent) {
        System.out.println("You can click buttons. Impressive!");
    }

    public void tableGames_click(MouseEvent mouseEvent) {
        System.out.println("Even THIS button, YOU ROCK!");
    }
}
