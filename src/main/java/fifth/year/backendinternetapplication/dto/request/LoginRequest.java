package fifth.year.backendinternetapplication.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "The Email should be present with a value")
        @Email(message = "The Email should be a valid email")
        String email,
        @NotBlank(message = "The Password should be present with a value")
        String password
) {

}
