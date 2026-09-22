package com.fashionstore.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.fashionstore.dao.impl.ReviewDAOImpl;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.Review;
import com.fashionstore.model.User;
import com.fashionstore.test.TestDatabase;
import com.fashionstore.util.DBConnection;

public class ReviewDAOTest {

    private ReviewDAOImpl reviewDAO;
    private UserDAOImpl userDAO;

    @BeforeEach
    public void setUp() {
        reviewDAO = new ReviewDAOImpl();
        userDAO = new UserDAOImpl();
    }

    @Test
    public void testAddAndGetReviews() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            User user = new User();
            user.setName("Reviewer");
            user.setEmail("rev_" + System.currentTimeMillis() + "@example.com");
            user.setPassword("pass");
            userDAO.registerUser(user);

            Review review = new Review();
            review.setUserId(user.getId());
            review.setProductId(1);
            review.setRating(5);
            review.setReviewText("Excellent tee!");

            boolean added = reviewDAO.addReview(review);
            assertTrue(added);

            List<Review> reviews = reviewDAO.getReviewsByProductId(1);
            assertNotNull(reviews);
            assertFalse(reviews.isEmpty());

            double avg = reviewDAO.getAverageRating(1);
            assertTrue(avg > 0);
        }
    }

    @Test
    public void testGetAverageRatingNoReviews() {
        try (MockedStatic<DBConnection> dbMock = Mockito.mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenAnswer(inv -> TestDatabase.getConnection());

            double avg = reviewDAO.getAverageRating(99999);
            assertEquals(0.0, avg, 0.001);
        }
    }
}
