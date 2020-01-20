package service;

import dao.ReviewDao;
import model.Game;
import model.Review;
import model.User;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ReviewService class - Class that holds the business rules of the application.
 * A layer between DAO and Controller - Database and GUI.
 * Primarily handles logic of Review objects.
 *
 * @author Pavel Dvoriak
 * @version 20.01.2020
 */
public class ReviewService {
    private ReviewDao reviewDao;

    /**
     * A constructor to create an instance of ReviewService object.
     * It assigns a reviewDao to connect the business and database layers.
     *
     * @param rd ReviewDao that handles database operations connected with Review objects
     */
    public ReviewService(ReviewDao rd) {
        this.reviewDao = rd;
    }

    /**
     * Method that creates an instance of Review
     * and passes it to DAO to store in database
     *
     * @param ratedGame  Game object that is being rated by particular Review
     * @param signedUser User that created the Review
     * @param rating     Numeric rating of a Game object
     * @param content    Written rating of a Game object
     * @param em         Entity Manager to create transaction
     * @return Created instance of a Review object
     */
    public Review createNewReview(Game ratedGame, User signedUser, double rating, String content, EntityManager em) {
        Review review = new Review(rating, content, ratedGame, signedUser);

            em.getTransaction().begin();

            review = reviewDao.saveReview(review, em);
            if (review == null) {
                throw new NullPointerException();

            }

            em.getTransaction().commit();

            return review;
        }

        /**
         * Method to request all Reviews from DAO bind to given Game object.
         *
         *
         * @param game Game object that owns the Reviews
         * @param entityManager Entity Manager to pass to the DAO
         * @return List of the Reviews belonging to the Game object, or empty
         */
        public List<Review> getAllReviewsByGame (Game game, EntityManager entityManager){

            return reviewDao.readReviewsByGame(game, entityManager);
        }

        /**
         * Method that calculates middle value of all ratings bind to
         * given Game object.
         *
         * @param game Game object that owns the Reviews
         * @param entityManager Entity Manager to create a transaction
         * @return Game with updated rating value
         */
        public Game calculateAvgRating (Game game, EntityManager entityManager){
            ArrayList<Review> reviews = (ArrayList<Review>) getAllReviewsByGame(game, entityManager);
            List<Double> ratings = new ArrayList<>();
            reviews.forEach(review1 -> {
                ratings.add(review1.getRating());
            });
            Double newAvgR = findMedian(ratings);

            entityManager.getTransaction().begin();
            Game result = reviewDao.updateAverageRating(game, newAvgR, entityManager);
            entityManager.getTransaction().commit();
            return result;
        }

        /**
         * Method that takes a List of Doubles and calculates its median value
         *
         * @param list List of doubles
         * @return Median of the given list, or null if not enough values
         */
        public Double findMedian (List < Double > list) {
            Double median;

            Collections.sort(list);
            if (list.size() % 2 == 0) {
                median = ((list.get(list.size() / 2)) + ((list.get((list.size() / 2) + 1)))) / 2;
            } else {
                median = list.get(list.size() / 2);
            }
            return median;
        }
    }
