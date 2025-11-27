package com.jiocoders.java.jiofamily.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = InterestIdsValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidInterestIds {

    String message() default "Invalid interest IDs";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}