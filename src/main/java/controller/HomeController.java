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
import service.GameService;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.LocalDate;
import java.util.function.Consumer;


public class HomeController {
    @FXML
    private Button btnWriteReview;
    @FXML
    private Button btnEditGame;
    @FXML
    private TableView<Game> tableGames = new TableView<>();

    private ObservableList<Game> data;
    private GameService gameService;
    private Game selectedGame = null;
    private String signedUser;

    public HomeController() {
        this.gameService = new GameService(new GameDao());
    }

    public void initialise(String userName) {
        signedUser = userName;
        TableColumn<Game, String> columnName = new TableColumn<>("Name");
        columnName.setCellValueFactory(
                new PropertyValueFactory<Game, String>("name"));
        TableColumn<Game, String> columnStudio = new TableColumn<>("Studio");
        columnStudio.setCellValueFactory(
                new PropertyValueFactory<Game, String>("studio"));
        TableColumn<Game, LocalDate> columnPublished = new TableColumn<>("Published");
        columnPublished.setCellValueFactory(
                new PropertyValueFactory<Game, LocalDate>("published"));
        TableColumn<Game, Double> columnRating = new TableColumn<>("Rating");
        columnRating.setCellValueFactory(
                new PropertyValueFactory<Game, Double>("rating"));

        EntityManager em = App.createEM();

        data = FXCollections.observableArrayList(gameService.getAllGames(em));

        tableGames.getColumns().clear();
        tableGames.setItems(data);
        tableGames.getColumns().addAll(columnName, columnStudio, columnPublished, columnRating);

        em.close();
    }


    public void btnWriteReview_click(ActionEvent mouseEvent) {
        System.out.println("You can click buttons. Impressive!");
    }

    public void tableGames_click(MouseEvent mouseEvent) throws IOException {
        if(mouseEvent.getClickCount() == 2) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/review.fxml"));
            VBox root = loader.load();

            showWindow("Reviews", root);
        }

        Game clickedOn = tableGames.getSelectionModel().getSelectedItem();


        if(clickedOn == null) {
            selectedGame = null;
            btnEditGame.setText("Add Game");
            tableGames.getSelectionModel().clearSelection();
        } else if (selectedGame == null) {
            selectedGame = clickedOn;
            btnEditGame.setText("Edit Game");
        } else if (selectedGame.equals(tableGames.getSelectionModel().getSelectedItem())) {
            selectedGame = null;
            btnEditGame.setText("Add Game");
            tableGames.getSelectionModel().clearSelection();
        } else {
            selectedGame = tableGames.getSelectionModel().getSelectedItem();
        }
    }

    Consumer<Game> addConsumer = result -> {
        data.add(result);
    };
    //TODO: make this smarter (enum / boolean)
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
            controller.initAddition(addConsumer);
            showWindow("Add new game", root);

        } else if (buttonType.trim().toLowerCase().equals("edit game")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/edit_game.fxml"));
            GridPane root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                // TODO - add alert
                e.printStackTrace();
            }

            GameController controller = loader.getController();
            Consumer<Game> deleteConsumer = result -> {
                data.remove(result);
            };
            controller.initEdit(selectedGame, addConsumer);
            data.remove(selectedGame);
            showWindow("Edit game " + selectedGame.getName(), root);
        }
    }

    public void showWindow(String title, Parent root) {
        Stage owner = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        Stage addGameWindow = new Stage();

        addGameWindow.setScene(new Scene(root));
        addGameWindow.setTitle(title);
        addGameWindow.initModality(Modality.WINDOW_MODAL);
        addGameWindow.initOwner(owner);
        addGameWindow.show();
    }


}

