package fifth.year.backendinternetapplication.utils.constraints.annotation;

import fifth.year.backendinternetapplication.utils.constraints.UniquePermissionNameFiledConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniquePermissionNameFiledConstraint.class)
public @interface UniquePermissionNameFiled {
    String message() default "This Permission Name is already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
