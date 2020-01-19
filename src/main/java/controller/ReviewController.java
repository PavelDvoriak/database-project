package controller;

import application.App;
import dao.ReviewDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Game;
import model.Review;
import model.User;
import service.GameService;
import service.ReviewService;

import javax.persistence.EntityManager;
import java.util.List;

public class ReviewController {
    @FXML
    ListView<String> listReviews;
    @FXML
    private Label lblAuthor;
    @FXML
    private Label lblGameName;
    @FXML
    private Label reviewRating;
    @FXML
    private TextArea reviewContent;
    @FXML
    private Slider reviewRatingSlider;
    @FXML
    private Button backBtn;
    @FXML
    private Button btnAddReview;

    private User signedUser;
    private Game ratedGame;
    private ReviewService reviewService;
    private GameService gameService;
    private ObservableList<Review> items;

    public ReviewController() {
        this.reviewService = new ReviewService(new ReviewDao());
    }

    public void initialise(User user, Game game) {
        this.signedUser = user;
        this.ratedGame = game;

        lblAuthor.setText(signedUser.getUsername());
        lblGameName.setText(ratedGame.getName());

        reviewRating.setText(String.format("%.1f", reviewRatingSlider.getValue()));

        reviewRatingSlider.valueProperty().addListener((ov, old_val, new_val) -> reviewRating.setText(String.format("%.1f", new_val)));

        EntityManager entityManager = App.createEM();

        List<Review> data = reviewService.getAllReviewsByGame(ratedGame, entityManager);
        items = FXCollections.observableArrayList(data);
        items.forEach(item -> {
            listReviews.getItems().add(item.toString());
        });


        entityManager.close();
    }

    public void backBtn_click(ActionEvent actionEvent) {
        Stage currentWindow = (Stage) backBtn.getScene().getWindow();
        currentWindow.close();
    }

    public void btnAddReview_click(ActionEvent actionEvent) {
        EntityManager em = App.createEM();
        double rating = reviewRatingSlider.getValue();
        String content = reviewContent.getText();
        Review review = reviewService.createNewReview(ratedGame, signedUser, rating, content, em);
        reviewService.calculateAvgRating(ratedGame, em);
        em.close();
        listReviews.getItems().add(review.toString());
    }

}
