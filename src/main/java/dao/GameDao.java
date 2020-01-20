package dao;

import model.Game;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

/**
 * GameDao class - Data access objects are the lowest layer of the application.
 * It has straight access to the physical data and can manipulate it.
 * This particular DAO serves the Game objects.
 *
 * @author Pavel Dvoriak
 * @version 20.01.2020
 */
public class GameDao {

    /**
     * Method that queries the Game table for all Games stored there.
     *
     * @param em Entity Manager is used for creating queries in ORM
     * @return An empty list if there are no records in database or contains Game objects
     */
    public List readAllGames(EntityManager em) {
        return em.createQuery("from Game").getResultList();
    }

    /**
     * Method that stores a Game object in the database, if not
     * already there.
     *
     * @param game Game object to be stored
     * @param em Entity Manager to perform ORM database operations
     * @return Game object that had been stored
     */
    public Game saveGame(Game game, EntityManager em) {
        if (game.getId() == null) {
            em.persist(game);
        } else if (!em.contains(game)) {
            game = em.merge(game);
        }
        return game;
    }

    /**
     * Finds Game object in the database and deletes it.
     *
     * @param game Game object to be deleted
     * @param entityManager Entity Manager to perform ORM database operations
     */
    public void deleteGame(Game game, EntityManager entityManager) {
        Game game1 = entityManager.merge(game);
        entityManager.remove(game1);
    }

    /**
     * Finds Game object in the database and update its attributes.
     *
     * @param id Id attribute of the Game object
     * @param nameNew Updated name attribute of the Game object
     * @param studioNew Updated studio attribute of the Game object
     * @param publishedNew Updated published attribute of the Game object
     * @param em Entity Manager to perform ORM database operations
     * @return Updated Game object
     */
    public Game updateGame(Long id, String nameNew, String studioNew, LocalDate publishedNew, EntityManager em) {
        Game game = em.find(Game.class, id);
        game.setName(nameNew);
        game.setStudio(studioNew);
        game.setPublished(publishedNew);
        return game;
    }
}
