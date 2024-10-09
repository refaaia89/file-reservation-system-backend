package fifth.year.backendinternetapplication.utils.constraints;

import fifth.year.backendinternetapplication.repository.PermissionRepository;
import fifth.year.backendinternetapplication.utils.constraints.annotation.UniquePermissionNameFiled;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UniquePermissionNameFiledConstraint implements ConstraintValidator<UniquePermissionNameFiled, String> {

    private PermissionRepository repository;

    private static final UniquePermissionNameFiledConstraint holder = new UniquePermissionNameFiledConstraint();

    @Bean
    public static UniquePermissionNameFiledConstraint permissionNameBean(PermissionRepository repository) {
        holder.repository = repository;
        return holder;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return holder.repository.findByName(value).orElse(null) == null;
    }
}
