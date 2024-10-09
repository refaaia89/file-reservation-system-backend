package fifth.year.backendinternetapplication.utils.constraints;

import fifth.year.backendinternetapplication.repository.GroupRepository;
import fifth.year.backendinternetapplication.utils.constraints.annotation.UniqueGroupNameFiled;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UniqueGroupNameFiledConstraint  implements ConstraintValidator<UniqueGroupNameFiled, String> {

    private GroupRepository repository;

    private static final UniqueGroupNameFiledConstraint holder = new UniqueGroupNameFiledConstraint();

    @Bean
    public static UniqueGroupNameFiledConstraint groupNameBean(GroupRepository repository) {
        holder.repository = repository;
        return holder;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return holder.repository.findByName(value).orElse(null) == null;
    }
}
