package fifth.year.backendinternetapplication.dto.request.create;

import fifth.year.backendinternetapplication.utils.constraints.annotation.UniquePermissionNameFiled;
import jakarta.validation.constraints.NotBlank;

public record PermissionCreateRequest(
        @UniquePermissionNameFiled
        @NotBlank(message = "The Permission Filed must be present with a value")
        String name,
        String description
) {
}
