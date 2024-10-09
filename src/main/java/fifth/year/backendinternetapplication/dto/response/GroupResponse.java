package fifth.year.backendinternetapplication.dto.response;

import fifth.year.backendinternetapplication.model.enums.TokenType;

import java.time.LocalDateTime;

public record GroupResponse(
        long groupId,
        String name,
        String description,
        String allowed_extension_file_types,
        Boolean is_public,
        Integer max_members_count,
        Integer max_files_count,
        Integer max_allowed_file_size_in_mb,
        String administrator,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
