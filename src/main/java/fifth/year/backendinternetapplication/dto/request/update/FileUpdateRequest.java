package fifth.year.backendinternetapplication.dto.request.update;

import org.springframework.web.multipart.MultipartFile;

public record FileUpdateRequest(
        String name,
        Long owner_id,
        Long group_id,
        MultipartFile file
) {
}
