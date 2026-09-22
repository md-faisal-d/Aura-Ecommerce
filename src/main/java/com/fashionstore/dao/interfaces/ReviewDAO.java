// ReviewDAO.java

package com.fashionstore.dao.interfaces;

import java.util.List;

import com.fashionstore.model.Review;

public interface ReviewDAO {

    boolean addReview(Review review);

    List<Review> getReviewsByProductId(int productId);

    double getAverageRating(int productId);

    boolean deleteReview(int reviewId);
}