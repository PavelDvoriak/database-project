package service;

import dao.GameDao;
import model.Game;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

/**
 * GameService class - Class that holds the business rules of the application.
 * A layer between DAO and Controller - Database and GUI.
 * Primarily handles logic of Game objects.
 *
 * @author Pavel Dvoriak
 * @version 20.01.2020
 */
public class GameService {
    private GameDao gameDao;

    /**
     * A contructor to create an instance of GameService object.
     * It assigns a gameDao to connect the business and database layers
     *
     * @param gd GameDao that handles database operations connected with Game objects
     */
    public GameService(GameDao gd) {
        this.gameDao = gd;
    }

    /**
     * Method that passes a request to GameDao
     * to read all Game objects stored in database.
     *
     * @param em Entity Manager to pass to gameDao
     * @return List of Game objects, or empty if no Game objects in database
     */
    public List getAllGames(EntityManager em) {
        return gameDao.readAllGames(em);
    }

    /**
     * Method that creates an instance of Game
     * and passes it to DAO to store in database
     *
     * @param name Name of the Game
     * @param studio Studio that developed the Game
     * @param published Release date of the Game
     * @param em Entity Manager to create a transaction
     * @return Created Game instance
     */
    public Game createGame(String name, String studio, LocalDate published, EntityManager em) {
        Game game = new Game(name, studio, published);

        List<Game> games = getAllGames(em);
        Game finalGame = game;
        games.forEach(game1 -> {
            if(game1.equals(finalGame)) {
                throw new NullPointerException();
            }
        });

        em.getTransaction().begin();

        game = gameDao.saveGame(game, em);
        em.getTransaction().commit();
        return game;
    }

    /**
     * Method that creates an transaction on DAO in order
     * to delete this entry from the database.
     *
     * @param em Entity Manager
     */
    public void removeGame(Game game, EntityManager em) {
        em.getTransaction().begin();

        gameDao.deleteGame(game, em);

        em.getTransaction().commit();
    }

    /**
     * Method that requests update of a Game object stored in a database.
     * It performs several checks if the object has changed and then passes
     * the attributes to the DAO in orded to modify the record.
     *
     * @param gameToEdit Game object stored in a database that should be modified
     * @param nameNew Updated name of the Game object
     * @param studioNew Updated studio of the Game object
     * @param publishedNew Updated release date of the Game object
     * @param em Entity Manager to create a transaction
     * @return Modified Game object
     */
    public Game modifyGame(Game gameToEdit, String nameNew, String studioNew, LocalDate publishedNew, EntityManager em) {

        boolean nameChanged = false;
        boolean studioChanged = false;
        boolean publishedChanged = false;

        if (!nameNew.isEmpty() && !nameNew.equals(gameToEdit.getName())) {
            nameChanged = true;
        } else {
            nameNew = gameToEdit.getName();
        }
        if (!studioNew.isEmpty() && !studioNew.equals(gameToEdit.getStudio())) {
            studioChanged = true;
        } else {
            studioNew = gameToEdit.getStudio();
        }
        if (publishedNew != null && !publishedNew.equals(gameToEdit.getPublished())) {
            publishedChanged = true;
        } else {
            publishedNew = gameToEdit.getPublished();
        }

        if (!nameChanged && !studioChanged && !publishedChanged) {
            return null;
        }

        em.getTransaction().begin();

        Game updatedGame = gameDao.updateGame(gameToEdit.getId(), nameNew, studioNew, publishedNew, em);

        em.getTransaction().commit();
        return updatedGame;
    }
}
