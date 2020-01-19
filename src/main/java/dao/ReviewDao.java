package dao;

import model.Game;
import model.Review;

import javax.persistence.EntityManager;
import java.util.List;

public class ReviewDao {

    public Review saveReview(Review review, EntityManager entityManager) {
        if (review.getId() == null) {
            entityManager.persist(review);
        } else if (!entityManager.contains(review)) {
            review = entityManager.merge(review);
        }
        return review;
    }

    public List<Review> readReviewsByGame(Game gameToFind, EntityManager entityManager) {
        List<Review> reviews = entityManager.createQuery("from Review r where r.game = ?1", Review.class)
                .setParameter(1, gameToFind)
                .getResultList();
        return reviews;

    }

    public Game updateAverageRating(Game game, Double rating, EntityManager entityManager) {
        Game gameToUpdate = entityManager.find(Game.class, game.getId());
        gameToUpdate.setRating(rating);
        return gameToUpdate;
    }
}
