package service;

import dao.ReviewDao;

public class ReviewService {
    private ReviewDao reviewDao;

    public ReviewService(ReviewDao rd) {
        this.reviewDao = rd;
    }
}
