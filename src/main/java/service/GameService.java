package service;

import dao.GameDao;
import model.Game;
import model.MessageBox;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class GameService {
    private GameDao gameDao;

    public GameService(GameDao gd) {
        this.gameDao = gd;
    }

    public List<Game> getAllGames(EntityManager em) {
        return gameDao.readAllGames(em);
    }

    public Game createGame(String name, String studio, LocalDate published, EntityManager em) {
        Game game = new Game(name, studio, published);

        em.getTransaction().begin();

        try {
            game = gameDao.saveGame(game, em);
        } catch (JdbcSQLIntegrityConstraintViolationException e) {
            MessageBox.showError("Error", "Duplicate alert", "This game is already created");
            return null;
        }
        em.getTransaction().commit();
        return game;
    }

    public void removeGame(Game game, EntityManager em) {
        em.getTransaction().begin();

        gameDao.deleteGame(game, em);

        em.getTransaction().commit();
    }

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
