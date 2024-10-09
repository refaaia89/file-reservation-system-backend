package fifth.year.backendinternetapplication.dto.response;


import java.time.LocalDateTime;

public record FileResponse(
        long fileId,
        String name,
        Boolean is_checked_in,
        LocalDateTime checked_in_until_time,
        String owner,
        String group,
        Long updated_by,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
