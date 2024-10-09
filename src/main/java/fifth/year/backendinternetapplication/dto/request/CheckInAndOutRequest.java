package fifth.year.backendinternetapplication.dto.request;

import jakarta.validation.constraints.NotNull;

public record CheckInAndOutRequest(
        @NotNull(message = "The file/s ID/s is required")
        Object files
) {
}
