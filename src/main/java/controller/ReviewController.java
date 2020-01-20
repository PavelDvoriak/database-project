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
import model.MessageBox;
import model.Review;
import model.User;
import service.GameService;
import service.ReviewService;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * ReviewController class - class that controls the Review GUI of the application.
 * It manages an addition or browsing of a Review objects on GUI level.
 * It is further implemented by review service, that is responsible for business rules
 *
 * @author Pavel Dvoriak
 * @version 20.01.2020
 */
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
    private HomeController parentController;

    /**
     * Constructor that creates an instance of this controller.
     * It then assigns needed service for self
     */
    public ReviewController() {
        this.reviewService = new ReviewService(new ReviewDao());
    }

    /**
     * Method that initialise needed containers and data for
     * browsing or adding a new Review
     *
     * @param user Signed User
     * @param game Reviewed Game
     * @param controller Controller of a home GUI to apply rating changes in the Game table
     */
    public void initialise(User user, Game game, HomeController controller) {
        this.signedUser = user;
        this.ratedGame = game;
        this.parentController = controller;

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

    /**
     * Method that closes current window and returns to
     * the home GUI after Back button is clicked
     *
     * @param actionEvent
     */
    public void backBtn_click(ActionEvent actionEvent) {
        Stage currentWindow = (Stage) backBtn.getScene().getWindow();
        currentWindow.close();
    }

    /**
     * Method that follows the trigger of Add button.
     * When the event is fired it requests creation of a Review on a
     * Service-level method
     * and persist on a DAO-level
     * Also applying changes to the Game rating in Game table in home GUI.
     *
     * @param actionEvent
     */
    public void btnAddReview_click(ActionEvent actionEvent) {
        EntityManager em = App.createEM();
        double rating = reviewRatingSlider.getValue();
        String content = reviewContent.getText();
        Review review;
        try {
            review = reviewService.createNewReview(ratedGame, signedUser, rating, content, em);
        } catch (NullPointerException e) {
            MessageBox.showError("Error", "Couldn't create the review", "An error has occured while storing the review\nTry again or contact support");
            return;
        }

        reviewService.calculateAvgRating(ratedGame, em);
        em.close();
        parentController.refreshTable();
        listReviews.getItems().add(review.toString());

        Stage currentWindow = (Stage) btnAddReview.getScene().getWindow();
        currentWindow.close();
    }

}
