package dao;

import model.Game;
import model.Review;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * GameDao class - Data access objects are the lowest layer of the application.
 * It has straight access to the physical data and can manipulate it.
 * This particular DAO serves the Review objects.
 *
 * @author Pavel Dvoriak
 * @version 20.01.2020
 */
public class ReviewDao {

    /**
     * Method that stores a Review object in the database, if not
     * already there. Retrieves it in that case.
     *
     * @param review Review object to be stored
     * @param entityManager Entity Manager to perform ORM database operations
     * @return Review object that had been stored
     */
    public Review saveReview(Review review, EntityManager entityManager) {
        if (review.getId() == null) {
            entityManager.persist(review);
        } else if (!entityManager.contains(review)) {
            review = entityManager.merge(review);
        }
        return review;
    }

    /**
     * Method that queries the Review table for all Reviews stored there, that belongs to the given Game object.
     *
     * @param gameToFind Game object that owns the requested Reviews
     * @param entityManager Entity Manager is used for creating queries in ORM
     * @return An empty list if there are no records in database or contains Review objects
     */
    public List<Review> readReviewsByGame(Game gameToFind, EntityManager entityManager) {
        return entityManager.createQuery("from Review r where r.game = ?1", Review.class)
                .setParameter(1, gameToFind)
                .getResultList();
    }

    /**
     * Method that updates a Game object stored in the database,
     * based on the last rating provided.
     *
     * @param game Game object, which attribute is updated
     * @param rating Rating provided by User that belongs to the Game object
     * @param entityManager Entity Manager to perform ORM database operations
     * @return Game object with updated Average Rating attribute
     */
    public Game updateAverageRating(Game game, Double rating, EntityManager entityManager) {
        Game gameToUpdate = entityManager.find(Game.class, game.getId());
        gameToUpdate.setRating(rating);
        return gameToUpdate;
    }
}
