package com.softuni.stayeasy.web;

import com.softuni.stayeasy.model.dto.property.PropertyBindingModel;
import com.softuni.stayeasy.model.dto.review.ReviewBindingModel;
import com.softuni.stayeasy.model.entity.property.Property;
import com.softuni.stayeasy.model.entity.property.PropertyType;
import com.softuni.stayeasy.model.entity.user.User;
import com.softuni.stayeasy.service.property.PropertyService;
import com.softuni.stayeasy.service.review.ReviewService;
import com.softuni.stayeasy.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final UserService userService;
    private final ReviewService reviewService;

    public PropertyController(PropertyService propertyService, UserService userService, ReviewService reviewService) {
        this.propertyService = propertyService;
        this.userService = userService;
        this.reviewService = reviewService;
    }

    // --- BROWSE ALL ---

    @GetMapping
    public String browse(Model model) {
        model.addAttribute("properties", propertyService.findAllAvailable());
        return "property/browse";
    }

    // --- DETAILS ---

    @GetMapping("/{id}")
    public String details(@PathVariable UUID id,
                          @RequestParam(required = false) String alreadyReviewed,
                          @RequestParam(required = false) String ratingError,
                          Model model,
                          HttpSession session) {
        Optional<Property> propertyOpt = propertyService.findById(id);

        if(propertyOpt.isEmpty()){
            return "redirect:/properties";
        }

        Property property = propertyOpt.get();
        model.addAttribute("property", property);
        model.addAttribute("reviews", reviewService.findAllByProperty(property));
        model.addAttribute("reviewData", new ReviewBindingModel());
        model.addAttribute("alreadyReviewed", alreadyReviewed != null);
        model.addAttribute("ratingError", ratingError != null);

        // Check if current user already reviewed this property
        if (session.getAttribute("userId") != null) {
            UUID userId = (UUID) session.getAttribute("userId");
            userService.findById(userId).ifPresent(user ->
                    model.addAttribute("userAlreadyReviewed",
                            reviewService.hasUserReviewedProperty(user, property))
            );
        }

        return "property/details";
    }

    // --- ADD ---

    @GetMapping("/add")
    public String addPage(Model model, HttpSession session) {
        if(session.getAttribute("userId") == null){
            return "redirect:/auth/login";

        }
        model.addAttribute("propertyData", new PropertyBindingModel());
        model.addAttribute("propertyType", PropertyType.values());
        return "property/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute PropertyBindingModel propertyData,
                      Model model,
                      HttpSession session) {

        if(session.getAttribute("userId") == null){
            return "redirect:/auth/login";
        }

        UUID userId = (UUID) session.getAttribute("userId");
        Optional<User> hostOpt = userService.findById(userId);

        if (hostOpt.isEmpty()){
            return "redirect:/auth/login";

        }

        Property property = Property.builder()
                .title(propertyData.getTitle())
                .description(propertyData.getDescription())
                .location(propertyData.getLocation())
                .pricePerNight(propertyData.getPricePerNight())
                .maxGuest(propertyData.getMaxGuests())
                .bedrooms(propertyData.getBedrooms())
                .bathrooms(propertyData.getBathrooms())
                .imageUrl(propertyData.getImageUrl())
                .type(propertyData.getType())
                .host(hostOpt.get())
                .build();

        propertyService.createProperty(property);
        return "redirect:/properties";

    }

    // --- EDIT ---

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable UUID id,
                           Model model,
                           HttpSession session) {

        if (session.getAttribute("userId") == null){
            return "redirect:/auth/login";
        }

        Optional<Property> propertyOpt = propertyService.findById(id);

        if(propertyOpt.isEmpty()){
            return "redirect:/properties";
        }

        Property property = propertyOpt.get();
        UUID userId = (UUID) session.getAttribute("userId");

        //Only the host can edit their own property

        if (!property.getHost().getId().equals(userId)){
            return "redirect:/properties";
        }

        PropertyBindingModel propertyData = new PropertyBindingModel();
        propertyData.setTitle(property.getTitle());
        propertyData.setDescription(property.getDescription());
        propertyData.setLocation(property.getLocation());
        propertyData.setPricePerNight(propertyData.getPricePerNight());
        propertyData.setMaxGuests(propertyData.getMaxGuests());
        propertyData.setBedrooms(propertyData.getBedrooms());
        propertyData.setBathrooms(propertyData.getBathrooms());
        propertyData.setImageUrl(propertyData.getImageUrl());
        propertyData.setType(propertyData.getType());

        model.addAttribute("propertyData", propertyData);
        model.addAttribute("propertyTypes", PropertyType.values());
        model.addAttribute("propertyId", id);
        return "property/edit";

    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable UUID id,
                       @ModelAttribute PropertyBindingModel propertyData,
                       HttpSession session) {

        if (session.getAttribute("userId") == null){
            return "redirect:/auth/login";
        }

        Optional<Property> propertyOpt = propertyService.findById(id);
        if(propertyOpt.isEmpty()){
            return "redirect:/properties";
        }

        Property property = propertyOpt.get();
        UUID userId = (UUID) session.getAttribute("userId");

        if (!property.getHost().getId().equals(userId)){
            return "redirect:/properties";
        }

        property.setTitle(propertyData.getTitle());
        property.setDescription(propertyData.getDescription());
        property.setLocation(property.getLocation());
        property.setPricePerNight(propertyData.getPricePerNight());
        property.setMaxGuest(propertyData.getMaxGuests());
        property.setBedrooms(propertyData.getBedrooms());
        property.setBathrooms(propertyData.getBathrooms());
        property.setImageUrl(propertyData.getImageUrl());
        property.setType(propertyData.getType());

        propertyService.updateProperty(property);
        return "redirect:/properties" + id;

    }

    // --- DELETE ---

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable UUID id,
                         HttpSession session) {
        if (session.getAttribute("userId") == null){
            return "redirect:/auth/login";

        }
        Optional<Property> propertyOpt = propertyService.findById(id);
        if(propertyOpt.isEmpty()){
            return "redirect:/properties";

        }

        UUID userId = (UUID) session.getAttribute("userId");
        if (!propertyOpt.get().getHost().getId().equals(userId)){
            return "redirect:/properties";
        }
        propertyService.deleteProperty(id);
        return "redirect:/properties";
    }


}
