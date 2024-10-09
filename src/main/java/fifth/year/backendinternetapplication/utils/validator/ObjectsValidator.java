package fifth.year.backendinternetapplication.utils.validator;


import fifth.year.backendinternetapplication.utils.exceptions.ObjectNotValidException;
import jakarta.validation.*;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ObjectsValidator<T> {
    private final ValidatorFactory validation= Validation.buildDefaultValidatorFactory();
    private final Validator validator = validation.getValidator();

    public void validate(T object){
        Set<ConstraintViolation<T>> violations= validator.validate(object);
        if(!violations.isEmpty()){
           var errorMessages = violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet());
           throw new ObjectNotValidException("Validations Error!",errorMessages);
        }
    }
}
