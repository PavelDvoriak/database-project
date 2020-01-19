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
import java.util.function.Consumer;

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
    private Consumer onCompleteAdd;
    private Consumer onCompleteDelete;
    private Game gameToEdit;

    public GameController() {
        gameService = new GameService(new GameDao());
    }

    public void initAddition(Consumer<Game> consumer) {
        this.onCompleteAdd = consumer;
    }

    public void initEdit(Game game, Consumer<Game> addConsumer) {
        this.gameToEdit = game;
        this.onCompleteAdd = addConsumer;
        nameOriginal.setText(gameToEdit.getName());
        studioOriginal.setText(gameToEdit.getStudio());
        publishedOriginal.setText(gameToEdit.getPublished().toString());
    }

    public void btnAddGame_click(ActionEvent actionEvent) {
        EntityManager em = App.createEM();
        String gName = gameName.getText();
        String studio = gameStudio.getText();
        LocalDate published = gamePublished.getValue();
        Errors errorList = new Errors();
        Game game = null;

        if(gName.isEmpty()) {
            errorList.addError("Game name can't be empty");
        }

        if(studio.isEmpty()) {
            errorList.addError(("Studio can't be empty"));
        }

        if(published == null) {
            errorList.addError("Release date must be picked");
        }

        if(!errorList.containErrors()) {
             game = gameService.createGame(gName, studio, published, em);
             onCompleteAdd.accept(game);
             Stage currentWindow = (Stage) btnAddGame.getScene().getWindow();
             currentWindow.close();
        } else {
            MessageBox.showError("An error has occured", "Following conditions have to be fullfilled", errorList.getErrors());
        }

        em.close();
    }

    public void cancelBtn_click(ActionEvent actionEvent) {
        onCompleteAdd.accept(gameToEdit);
        Stage currentWindow = (Stage) cancelBtn.getScene().getWindow();
        currentWindow.close();
    }

    public void confirmBtn_click(ActionEvent actionEvent) {
        EntityManager em = App.createEM();

        String nameNew = nameEdited.getText();
        String studioNew = studioEdited.getText();
        LocalDate publishedNew = publishedEdited.getValue();
        Game editedGame = gameService.modifyGame(gameToEdit, nameNew, studioNew, publishedNew, em);

        onCompleteAdd.accept(editedGame);

        Stage currentWindow = (Stage) cancelBtn.getScene().getWindow();
        currentWindow.close();

        em.close();
    }

    public void deleteBtn_click(ActionEvent actionEvent) {
        MessageBox.showConfirm("Game removal alert", "Removing game " + gameToEdit.getName(), "Are you sure you want to delete this game?\n" +
                "This action cannot be reversed.");

        EntityManager em = App.createEM();
        gameService.removeGame(gameToEdit, em);
        onCompleteDelete.accept(gameToEdit);

        Stage currentWindow = (Stage) cancelBtn.getScene().getWindow();
        currentWindow.close();

        em.close();
    }

}
