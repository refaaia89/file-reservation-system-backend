package fifth.year.backendinternetapplication.dto.request.update;

import fifth.year.backendinternetapplication.utils.constraints.annotation.UniqueEmailFiled;
import fifth.year.backendinternetapplication.utils.constraints.annotation.UniqueUsernameFiled;
import jakarta.validation.constraints.Email;

public record UserUpdateRequest(

        @UniqueUsernameFiled
        String username,
        @Email(message = "The Email should be valid email")
        @UniqueEmailFiled
        String email,
        String password,
        Long role_id
) {
}
