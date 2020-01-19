package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.Game;

public class ReviewController {
    @FXML
    private Label lblAuthor;
    @FXML
    private Label lblGameName;
    @FXML
    private Label reviewRating;
    @FXML
    private TextArea reviewContent;

    private String signedUser;
    private Game ratedGame;

    public void initAddition(String username, Game game) {
        this.signedUser = username;
        this.ratedGame = game;
    }

    public void backBtn_click(ActionEvent actionEvent) {
    }

    public void btnAddReview_click(ActionEvent actionEvent) {
    }
}
