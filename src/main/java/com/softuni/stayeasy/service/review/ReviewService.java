package com.softuni.stayeasy.service.review;

import com.softuni.stayeasy.model.entity.property.Property;
import com.softuni.stayeasy.model.entity.review.Review;
import com.softuni.stayeasy.model.entity.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewService {

    void createReview(Review review);

    void deleteReview(UUID id);

    Optional<Review> findById(UUID id);

    List<Review> findAllByProperty(Property property);

    List<Review> findAllByAuthor(User author);

    boolean hasUserReviewedProperty(User author, Property property);
}
