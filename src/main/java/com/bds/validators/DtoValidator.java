package com.bds.validators;

import com.bds.exception.ObjectNotValidException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DtoValidator<Object> {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public boolean validate(Object requestToValidate) {
        Set<ConstraintViolation<Object>> violations = validator.validate(requestToValidate);
        if (!violations.isEmpty()) {
            var errorMessages = violations
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
            throw new ObjectNotValidException(errorMessages);
        }
        return true;
    }
}
