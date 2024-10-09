package fifth.year.backendinternetapplication.dto.request.create;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record FileCreateRequest(
        @NotNull(message = "The Group ID must be present with a value")
        Long group_id,
        @NotNull(message = "The File must be present with a value")
        MultipartFile file
) {
}
