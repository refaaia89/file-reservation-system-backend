package fifth.year.backendinternetapplication.dto.response;

import java.time.LocalDateTime;

public record UserResponse(
        long userId,
        String username,
        String email,
        String role,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
