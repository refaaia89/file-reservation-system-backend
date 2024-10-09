package fifth.year.backendinternetapplication.utils.constraints.annotation;

import fifth.year.backendinternetapplication.utils.constraints.UniqueRoleNameFiledConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueRoleNameFiledConstraint.class)
public @interface UniqueRoleNameFiled {
    String message() default "This Role Name is already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
