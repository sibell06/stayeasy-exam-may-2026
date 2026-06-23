package com.softuni.stayeasy.web;

import com.softuni.stayeasy.model.dto.review.ReviewBindingModel;
import com.softuni.stayeasy.model.entity.property.Property;
import com.softuni.stayeasy.model.entity.review.Review;
import com.softuni.stayeasy.model.entity.user.User;
import com.softuni.stayeasy.service.property.PropertyService;
import com.softuni.stayeasy.service.review.ReviewService;
import com.softuni.stayeasy.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final PropertyService propertyService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService,
                            PropertyService propertyService,
                            UserService userService) {
        this.reviewService = reviewService;
        this.propertyService = propertyService;
        this.userService = userService;
    }

    // --- CREATE ---

    @PostMapping("/create/{propertyId}")
    public String create(@PathVariable UUID propertyId,
                         @Valid @ModelAttribute("reviewData") ReviewBindingModel reviewData,
                         BindingResult bindingResult,
                         Model model,
                         HttpSession session) {

        if (session.getAttribute("userId") == null) {
            return "redirect:/auth/login";
        }

        Optional<Property> propertyOpt = propertyService.findById(propertyId);
        if (propertyOpt.isEmpty()) {
            return "redirect:/properties";
        }

        Property property = propertyOpt.get();
        UUID userId = UUID.fromString((String) session.getAttribute("userId"));
        Optional<User> authorOpt = userService.findById(userId);
        if (authorOpt.isEmpty()) {
            return "redirect:/auth/login";
        }

        User author = authorOpt.get();

        if (bindingResult.hasErrors()) {
            model.addAttribute("property", property);
            model.addAttribute("reviews", reviewService.findAllByProperty(property));
            model.addAttribute("reviewData", reviewData);
            model.addAttribute("userAlreadyReviewed", reviewService.hasUserReviewedProperty(author, property));
            return "property/details";
        }

        // Prevent duplicate reviews
        if (reviewService.hasUserReviewedProperty(author, property)) {
            return "redirect:/properties/" + propertyId + "?alreadyReviewed=true";
        }

        Review review = Review.builder()
                .content(reviewData.getContent())
                .rating(reviewData.getRating())
                .author(author)
                .property(property)
                .build();

        reviewService.createReview(review);
        return "redirect:/properties/" + propertyId;
    }

    // --- DELETE ---

    @PostMapping("/delete/{reviewId}")
    public String delete(@PathVariable UUID reviewId,
                         HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/auth/login";
        }

        Optional<Review> reviewOpt = reviewService.findById(reviewId);
        if (reviewOpt.isEmpty()) {
            return "redirect:/properties";
        }

        UUID userId = UUID.fromString((String) session.getAttribute("userId"));

        if (!reviewOpt.get().getAuthor().getId().equals(userId)) {
            return "redirect:/properties/" + reviewOpt.get().getProperty().getId();
        }

        UUID propertyId = reviewOpt.get().getProperty().getId();
        reviewService.deleteReview(reviewId);
        return "redirect:/properties/" + propertyId;
    }
}