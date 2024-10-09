package fifth.year.backendinternetapplication.utils.constraints;

import fifth.year.backendinternetapplication.repository.RoleRepository;
import fifth.year.backendinternetapplication.utils.constraints.annotation.UniqueRoleNameFiled;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UniqueRoleNameFiledConstraint implements ConstraintValidator<UniqueRoleNameFiled, String> {

    private RoleRepository repository;

    private static final UniqueRoleNameFiledConstraint holder = new UniqueRoleNameFiledConstraint();

    @Bean
    public static UniqueRoleNameFiledConstraint roleNameBean(RoleRepository repository) {
        holder.repository = repository;
        return holder;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return holder.repository.findByName(value).orElse(null) == null;
    }
}
