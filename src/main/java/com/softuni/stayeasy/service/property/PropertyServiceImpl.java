package com.softuni.stayeasy.service.property;

import com.softuni.stayeasy.exception.PropertyNotFoundException;
import com.softuni.stayeasy.model.entity.property.Property;
import com.softuni.stayeasy.model.entity.user.User;
import com.softuni.stayeasy.repository.property.PropertyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public void createProperty(Property property) {
        property.setCreatedOn(LocalDateTime.now());
        property.setUpdatedOn(LocalDateTime.now());
        property.setAvailable(true);
        propertyRepository.save(property);
    }

    @Override
    public void updateProperty(Property property) {
        property.setUpdatedOn(LocalDateTime.now());
        propertyRepository.save(property);
    }

    @Override
    public void deleteProperty(UUID id) {
        if (!propertyRepository.existsById(id)) {
            throw new PropertyNotFoundException("Property with id " + id + " not found");
        }
        propertyRepository.deleteById(id);
    }

    @Override
    public Optional<Property> findById(UUID id) {
        return propertyRepository.findById(id);
    }

    @Override
    public Property findByIdOrThrow(UUID id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException("Property with id " + id + " not found"));
    }

    @Override
    public List<Property> findAllAvailable() {
        return propertyRepository.findAllByAvailableTrue();
    }

    @Override
    public List<Property> findAllByHost(User host) {
        return propertyRepository.findAllByHost(host);
    }
}