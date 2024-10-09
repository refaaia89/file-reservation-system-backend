package fifth.year.backendinternetapplication.dto.request;

import jakarta.validation.constraints.NotNull;

public record CopyFileFromGroupToAnother(
        @NotNull(message = "File Id is required!")
        Long fileId,
        @NotNull(message = "Group Id is required!")
        Long groupId
) {
}
