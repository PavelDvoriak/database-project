package service;

import dao.ReviewDao;
import model.Game;
import model.Review;
import model.User;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReviewService {
    private ReviewDao reviewDao;

    public ReviewService(ReviewDao rd) {
        this.reviewDao = rd;
    }

    public Review createNewReview(Game ratedGame, User signedUser, double rating, String content, EntityManager em) {
        Review review = new Review(rating, content, ratedGame, signedUser);

        em.getTransaction().begin();

        review = reviewDao.saveReview(review, em);

        em.getTransaction().commit();

        return review;
    }

    public List<Review> getAllReviewsByGame(Game game, EntityManager entityManager) {

        return reviewDao.readReviewsByGame(game, entityManager);
    }

    public Game calculateAvgRating(Game game, EntityManager entityManager) {
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

    public Double findMedian(List<Double> list) {
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
