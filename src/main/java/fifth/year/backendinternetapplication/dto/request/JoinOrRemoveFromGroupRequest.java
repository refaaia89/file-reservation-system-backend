package fifth.year.backendinternetapplication.dto.request;

import jakarta.validation.constraints.NotNull;

public record JoinOrRemoveFromGroupRequest(
    @NotNull(message = "The group/s ID/s is required")
    Object group,
    Object user
){

}
