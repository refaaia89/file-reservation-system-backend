package fifth.year.backendinternetapplication.utils.constraints.annotation;


import fifth.year.backendinternetapplication.utils.constraints.UniqueEmailFiledConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailFiledConstraint.class)
public @interface UniqueEmailFiled {
    String message() default "This Email Address is already registered";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
