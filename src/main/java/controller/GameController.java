package controller;

import application.App;
import dao.GameDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Errors;
import model.Game;
import model.MessageBox;
import service.GameService;

import javax.persistence.EntityManager;
import java.time.LocalDate;

/**
 * HomeController class - class that controls the game GUI of the application.
 * It manages an edit, addition, deletion of a Game object on GUI level.
 * It is further implemented by game service, that is responsible for business rules
 *
 * @author Pavel Dvoriak
 * @version 20.01.2020
 */
public class GameController {
    @FXML
    private TextField nameEdited;
    @FXML
    private TextField studioEdited;
    @FXML
    private DatePicker publishedEdited;
    @FXML
    private TextField nameOriginal;
    @FXML
    private TextField studioOriginal;
    @FXML
    private TextField publishedOriginal;
    @FXML
    private TextField gameName;
    @FXML
    private TextField gameStudio;
    @FXML
    private DatePicker gamePublished;
    @FXML
    private Button btnAddGame;
    @FXML
    private Button cancelBtn;

    private GameService gameService;
    private Game gameToEdit;
    private HomeController parentController;

    /**
     * Constructor that creates an instance of this controller.
     * It then assigns needed service for self
     */
    public GameController() {
        gameService = new GameService(new GameDao());
    }

    /**
     * Method that assigns given controller from the parent window
     * in order to apply changes to the table of games on home GUI
     *
     * @param controller Controller of the home GUI, that contains the table of Games
     */
    public void initialise(HomeController controller) {
        this.parentController = controller;
    }

    /**
     * Method that initialises data and controls necessary for editing a Game record
     *
     * @param game Game object that shoul be edited
     * @param controller Controller of the home GUI, that contains the table of Games
     */
    public void initEdit(Game game, HomeController controller) {
        initialise(controller);
        this.gameToEdit = game;
        nameOriginal.setText(gameToEdit.getName());
        studioOriginal.setText(gameToEdit.getStudio());
        publishedOriginal.setText(gameToEdit.getPublished().toString());
    }

    /**
     * Method that follows the trigger of Add button.
     * When the event is fired it performs basic checks
     * and callin a Service-level method to create new Game
     * and persist in on a DAO-level
     * Also adding the game to the Game table in home GUI.
     *
     * @param actionEvent
     */
    public void btnAddGame_click(ActionEvent actionEvent) {
        EntityManager em = App.createEM();
        String gName = gameName.getText();
        String studio = gameStudio.getText();
        LocalDate published = gamePublished.getValue();
        Errors errorList = new Errors();
        Game game = null;

        if (gName.isEmpty()) {
            errorList.addError("Game name can't be empty");
        }

        if (studio.isEmpty()) {
            errorList.addError(("Studio can't be empty"));
        }

        if (published == null) {
            errorList.addError("Release date must be picked");
        }

        if (!errorList.containErrors()) {
            try {
                gameService.createGame(gName, studio, published, em);
                parentController.refreshTable();
                Stage currentWindow = (Stage) btnAddGame.getScene().getWindow();
                currentWindow.close();
            } catch (NullPointerException e) {
                errorList.addError("Game already exists");
            }
        } else {
            MessageBox.showError("An error has occured", "Following conditions have to be fullfilled", errorList.getErrors());
        }

        em.close();
    }

    /**
     * Method that closes current window and returns to
     * the home GUI after Cancel button is clicked
     *
     * @param actionEvent
     */
    public void cancelBtn_click(ActionEvent actionEvent) {
        Stage currentWindow = (Stage) cancelBtn.getScene().getWindow();
        currentWindow.close();
    }

    /**
     * Method that follows the trigger of Confirmation button.
     * When the event is fired it calls a lower level methods
     * to apply requested changes on a Game object
     * Also applying changes to the Game table in home GUI.
     *
     * @param actionEvent
     */
    public void confirmBtn_click(ActionEvent actionEvent) {
        EntityManager em = App.createEM();

        String nameNew = nameEdited.getText();
        String studioNew = studioEdited.getText();
        LocalDate publishedNew = publishedEdited.getValue();
        gameService.modifyGame(gameToEdit, nameNew, studioNew, publishedNew, em);
        parentController.refreshTable();

        Stage currentWindow = (Stage) cancelBtn.getScene().getWindow();
        currentWindow.close();

        em.close();
    }

    /**
     * Method that follows the trigger of Delete button.
     * When the event is fired it requests a confirmation of the User
     * and calling a lower level methods to delete a  Game object
     *
     * @param actionEvent
     */
    public void deleteBtn_click(ActionEvent actionEvent) {
        MessageBox.showConfirm("Game removal alert", "Removing game " + gameToEdit.getName(), "Are you sure you want to delete this game?\n" +
                "This action cannot be reversed.");

        EntityManager em = App.createEM();
        gameService.removeGame(gameToEdit, em);
        parentController.refreshTable();

        Stage currentWindow = (Stage) cancelBtn.getScene().getWindow();
        currentWindow.close();

        em.close();
    }


}
