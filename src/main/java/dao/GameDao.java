package dao;

import model.Game;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import java.time.LocalDate;
import java.util.List;

public class GameDao {

    public List readAllGames(EntityManager em) {
        return em.createQuery("from Game").getResultList();
    }

    public Game saveGame(Game game, EntityManager em) throws JdbcSQLIntegrityConstraintViolationException {
        if(game.getId() == null) {
            em.persist(game);
        } else if (!em.contains(game)) {
            game = em.merge(game);
        }
        return game;
    }

    public void deleteGame(Game game, EntityManager entityManager) {
        Game game1 = entityManager.merge(game);
        entityManager.remove(game1);
    }

    public Game updateGame(Long id, String nameNew, String studioNew, LocalDate publishedNew, EntityManager em) {
        Game game = em.find(Game.class, id);
        game.setName(nameNew);
        game.setStudio(studioNew);
        game.setPublished(publishedNew);
        return game;
    }
}
