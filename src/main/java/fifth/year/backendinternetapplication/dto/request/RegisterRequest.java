package fifth.year.backendinternetapplication.dto.request;

import fifth.year.backendinternetapplication.utils.constraints.annotation.UniqueEmailFiled;
import fifth.year.backendinternetapplication.utils.constraints.annotation.UniqueUsernameFiled;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "The Username should be present with a value")
        @UniqueUsernameFiled
        String username,
        @NotBlank(message = "The Email should be present with a value")
        @Email(message = "The Email should be valid email")
        @UniqueEmailFiled
        String email,
        @NotBlank(message = "The Password should be present with a value")
        String password
) {

}
