package com.softuni.stayeasy.model.dto.property;

import com.softuni.stayeasy.model.entity.property.PropertyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class PropertyBindingModel {

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotBlank(message = "Location is required")
    @Size(min = 2, max = 100, message = "Location must be between 2 and 100 characters")
    private String location;

    @NotNull(message = "Price per night is required")
    @DecimalMin(value = "1.0", message = "Price per night must be at least 1")
    private BigDecimal pricePerNight;

    @Min(value = 1, message = "Max guests must be at least 1")
    @Max(value = 50, message = "Max guests cannot exceed 50")
    private int maxGuest;

    @Min(value = 0, message = "Bedrooms cannot be negative")
    @Max(value = 50, message = "Bedrooms cannot exceed 50")
    private int bedrooms;

    @Min(value = 0, message = "Bathrooms cannot be negative")
    @Max(value = 50, message = "Bathrooms cannot exceed 50")
    private int bathrooms;

    private String imageUrl;

    @NotNull(message = "Property type is required")
    private PropertyType type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public int getMaxGuest() {
        return maxGuest;
    }

    public void setMaxGuest(int maxGuest) {
        this.maxGuest = maxGuest;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public PropertyType getType() {
        return type;
    }

    public void setType(PropertyType type) {
        this.type = type;
    }
}