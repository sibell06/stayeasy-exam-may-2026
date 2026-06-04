package com.softuni.stayeasy.repository.review;

import com.softuni.stayeasy.model.entity.property.Property;
import com.softuni.stayeasy.model.entity.review.Review;
import com.softuni.stayeasy.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository  extends JpaRepository<Review, UUID> {

    List<Review> findAllByProperty(Property property);

    List<Review> findAllByAuthor(User author);

    boolean existsByAuthorAndProperty(User author, Property property);

}
