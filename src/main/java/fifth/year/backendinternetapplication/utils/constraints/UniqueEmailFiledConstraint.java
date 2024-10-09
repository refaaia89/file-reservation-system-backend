package fifth.year.backendinternetapplication.utils.constraints;

import fifth.year.backendinternetapplication.repository.UserRepository;
import fifth.year.backendinternetapplication.utils.constraints.annotation.UniqueEmailFiled;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UniqueEmailFiledConstraint implements ConstraintValidator<UniqueEmailFiled, String> {


    private UserRepository repository;

    private static final UniqueEmailFiledConstraint holder = new UniqueEmailFiledConstraint();

    @Bean
    public static UniqueEmailFiledConstraint emailBean(UserRepository repository) {
        holder.repository = repository;
        return holder;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return holder.repository.findByEmail(value).orElse(null) == null;
    }
}
