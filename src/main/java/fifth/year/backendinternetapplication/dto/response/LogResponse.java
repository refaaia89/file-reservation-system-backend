package fifth.year.backendinternetapplication.dto.response;

import java.time.LocalDateTime;

public record LogResponse(
        long logId,
        String type,
        String data,
        Long userId,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
