package com.softuni.stayeasy.model.dto.review;

public class ReviewBindingModel {

    private String content;
    private int rating;

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
