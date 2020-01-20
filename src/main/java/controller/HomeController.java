package controller;

import application.App;
import dao.GameDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Game;
import model.User;
import service.GameService;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * HomeController class - class that controls the home GUI of the application.
 * It contains, shows and manages a table of games.
 * It is further implemented by game service, that is responsible for business rules
 *
 * @author Pavel Dvoriak
 * @version 20.01.2020
 */
public class HomeController {
    @FXML
    private Button btnShowReviews;
    @FXML
    private Button btnEditGame;
    @FXML
    private TableView<Game> tableGames = new TableView<>();

    private ObservableList<Game> data;
    private GameService gameService;
    private Game selectedGame = null;
    private User signedUser;

    /**
     * Constructor that creates an instance of this controller.
     * It then assigns needed service for self
     */
    public HomeController() {
        this.gameService = new GameService(new GameDao());
    }

    /**
     * Method used for initialising data.
     * It creates the table of games and reads persisted data using other application layers.
     *
     * @param user User that is signed in to the application
     */
    public void initialise(User user) {
        signedUser = user;
        TableColumn<Game, String> columnName = new TableColumn<>("Name");
        columnName.setCellValueFactory(
                new PropertyValueFactory<Game, String>("name"));
        TableColumn<Game, String> columnStudio = new TableColumn<>("Studio");
        columnStudio.setCellValueFactory(
                new PropertyValueFactory<Game, String>("studio"));
        TableColumn<Game, LocalDate> columnPublished = new TableColumn<>("Published");
        columnPublished.setCellValueFactory(
                new PropertyValueFactory<Game, LocalDate>("published"));
        TableColumn<Game, Double> columnRating = new TableColumn<>("Average Rating");
        columnRating.setCellValueFactory(
                new PropertyValueFactory<Game, Double>("rating"));

        EntityManager em = App.createEM();

        data = FXCollections.observableArrayList(gameService.getAllGames(em));

        tableGames.getColumns().clear();
        tableGames.setItems(data);
        tableGames.getColumns().addAll(columnName, columnStudio, columnPublished, columnRating);

        btnShowReviews.setDisable(true);

        em.close();
    }

    /**
     * Method follows mouse clicks in the games table and customizes the GUI
     *
     * @param mouseEvent
     */
    public void tableGames_click(MouseEvent mouseEvent) {

        Game clickedOn = tableGames.getSelectionModel().getSelectedItem();

        if (clickedOn == null) {
            selectedGame = null;
            btnEditGame.setText("Add Game");
            btnShowReviews.setDisable(true);
            tableGames.getSelectionModel().clearSelection();
        } else if (selectedGame == null) {
            selectedGame = clickedOn;
            btnShowReviews.setDisable(false);
            btnEditGame.setText("Edit Game");
        } else if (selectedGame.equals(tableGames.getSelectionModel().getSelectedItem())) {
            selectedGame = null;
            btnEditGame.setText("Add Game");
            btnShowReviews.setDisable(true);
            tableGames.getSelectionModel().clearSelection();
        } else {
            selectedGame = tableGames.getSelectionModel().getSelectedItem();
            btnShowReviews.setDisable(false);
        }
    }

    /**
     * Method that follows trigger of the add / edit game button
     * Reads the state of the button and loads respective GUI
     *
     * @param actionEvent
     */
    public void btnEditGame_click(ActionEvent actionEvent) {
        String buttonType = btnEditGame.getText().toLowerCase();
        System.out.println(buttonType);
        if (buttonType.equals("add game")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/add_game.fxml"));

            VBox root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                // TODO - add alert
                e.printStackTrace();
            }

            GameController controller = loader.getController();
            controller.initialise(this);
            showWindow("Add new game", root);

        } else if (buttonType.trim().toLowerCase().equals("edit game")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/edit_game.fxml"));
            GridPane root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            GameController controller = loader.getController();
            controller.initEdit(selectedGame, this);
            showWindow("Edit game " + selectedGame.getName(), root);
        }
    }

    /**
     * Function that takes a window title and root container as parameters
     * and shows a Stage of that. It also inits modality
     *
     * @param title Text that is passed as a title to the shown window
     * @param root Root container of the fxml file
     */
    public void showWindow(String title, Parent root) {
        Stage owner = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        Stage addGameWindow = new Stage();

        addGameWindow.setScene(new Scene(root));
        addGameWindow.setTitle(title);
        addGameWindow.initModality(Modality.WINDOW_MODAL);
        addGameWindow.initOwner(owner);
        addGameWindow.show();
    }


    /**
     * Method that implements the function of ShowReviews button.
     * If the button is clicked it tries to load the Reviews GUI
     * It also calls the initialise method of corresponding controller
     * and passes signed user and selected game to it.
     *
     * @param actionEvent
     */
    public void btnShowReviews_click(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/review.fxml"));
        VBox root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ReviewController controller = loader.getController();
        controller.initialise(signedUser, selectedGame, this);

        showWindow("Reviews", root);
    }

    /**
     * Method that clears the data in the table
     * and calls functions of below layers to reload it.
     */
    public void refreshTable() {
        EntityManager em = App.createEM();

        data.removeAll(data);
        List<Game> games = gameService.getAllGames(em);
        games.forEach(game -> {
            data.add(game);
        });

        em.close();
    }
}

