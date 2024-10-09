package fifth.year.backendinternetapplication.dto.request.create;

import fifth.year.backendinternetapplication.utils.constraints.annotation.UniqueRoleNameFiled;
import jakarta.validation.constraints.NotBlank;

public record RoleCreateRequest(
        @NotBlank(message = "The name parameter should be present with a value")
        @UniqueRoleNameFiled
        String name
) {
}
