package fifth.year.backendinternetapplication.dto.response;

import java.time.LocalDateTime;

public record RoleResponse(
        long roleId,
        String name,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
