package fifth.year.backendinternetapplication.utils.constraints;

import fifth.year.backendinternetapplication.repository.UserRepository;
import fifth.year.backendinternetapplication.utils.constraints.annotation.UniqueUsernameFiled;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UniqueUsernameFiledConstraint implements ConstraintValidator<UniqueUsernameFiled, String> {

    private UserRepository repository;

    private static final UniqueUsernameFiledConstraint holder = new UniqueUsernameFiledConstraint();

    @Bean
    public static UniqueUsernameFiledConstraint usernameBean(UserRepository repository) {
        holder.repository = repository;
        return holder;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return holder.repository.findByName(value).orElse(null) == null;
    }
}
