package fifth.year.backendinternetapplication.dto.request.create;

import fifth.year.backendinternetapplication.utils.constraints.annotation.UniqueGroupNameFiled;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GroupCreateRequest(
        @NotBlank(message = "The name is required!")
        @UniqueGroupNameFiled
        String name,
        String description,
        String allowed_extension_file_types,
        @NotNull(message = "The administrator_id is required!")
        Boolean is_public,
        Integer max_members_count,
        Integer max_files_count,
        Integer max_allowed_file_size_in_mb,
        @NotNull(message = "The administrator_id is required!")
        Long administrator_id
) {
}
