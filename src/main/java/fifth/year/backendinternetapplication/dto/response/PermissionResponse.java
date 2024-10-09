package fifth.year.backendinternetapplication.dto.response;

import java.time.LocalDateTime;

public record PermissionResponse(
        long permissionId,
        String name,
        String description,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
