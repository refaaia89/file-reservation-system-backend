package fifth.year.backendinternetapplication.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        @NotBlank(message = "The current_password should be present with a value")
        String current_password,
        @NotBlank(message = "The new_password should be present with a value")
        String new_password,
        @NotBlank(message = "The confirmation_password should be present with a value")
        String confirmation_password
) {
}
