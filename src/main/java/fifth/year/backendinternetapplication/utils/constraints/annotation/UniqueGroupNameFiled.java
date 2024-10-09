package fifth.year.backendinternetapplication.utils.constraints.annotation;

import fifth.year.backendinternetapplication.utils.constraints.UniqueGroupNameFiledConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueGroupNameFiledConstraint.class)
public @interface UniqueGroupNameFiled {
    String message() default "This Group Name is already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
