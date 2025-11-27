package com.jiocoders.java.jiofamily.validator;

import com.jiocoders.java.jiofamily.entity.Interest;
import com.jiocoders.java.jiofamily.repository.InterestRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class InterestIdsValidator implements ConstraintValidator<ValidInterestIds, List<Integer>> {

    @Autowired
    private InterestRepository interestRepository;

    @Override
    public boolean isValid(List<Integer> ids, ConstraintValidatorContext context) {

        if (ids == null || ids.isEmpty()) return false;

        // Detect duplicates
        List<Integer> duplicates = ids.stream()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();

        if (!duplicates.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "Duplicate interest IDs: " + duplicates
            ).addConstraintViolation();
            return false;
        }

        // Validate IDs exist in DB
        List<Integer> existingIds = interestRepository.findAllById(ids)
                .stream()
                .map(Interest::getId)
                .toList();

        List<Integer> invalid = ids.stream()
                .filter(id -> !existingIds.contains(id))
                .toList();

        if (!invalid.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "Invalid interest IDs: " + invalid
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
