package com.softuni.stayeasy.model.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewBindingModel {

    @NotNull(message = "Review content is required")
    @Size(min = 10, max = 500, message = "Review must be between 10 and 500 characters")
    private String content;
    private int rating;

    @Min(value = 1, message = "Rate must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
