package fifth.year.backendinternetapplication.dto.request.update;

import fifth.year.backendinternetapplication.utils.constraints.annotation.UniqueGroupNameFiled;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GroupUpdateRequest(
        @UniqueGroupNameFiled
        String name,
        String description,
        String allowed_extension_file_types,
        Boolean is_public,
        Integer max_members_count,
        Integer max_files_count,
        Integer max_allowed_file_size_in_mb,
        Long administrator_id
) {
}
