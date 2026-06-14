package com.softuni.stayeasy.service.property;

import com.softuni.stayeasy.model.entity.property.Property;
import com.softuni.stayeasy.model.entity.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PropertyService {

    void createProperty(Property property);

    void updateProperty(Property property);

    void deleteProperty(UUID id);

    Optional<Property> findById(UUID id);

    Property findByIdOrThrow(UUID id);

    List<Property> findAllAvailable();

    List<Property> findAllByHost(User host);

}
