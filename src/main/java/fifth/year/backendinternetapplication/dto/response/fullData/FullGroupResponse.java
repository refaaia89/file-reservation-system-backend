package fifth.year.backendinternetapplication.dto.response.fullData;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fifth.year.backendinternetapplication.dto.response.FileResponse;
import fifth.year.backendinternetapplication.dto.response.GroupResponse;
import fifth.year.backendinternetapplication.dto.response.UserResponse;
import fifth.year.backendinternetapplication.model.Group;
import java.io.IOException;
import java.util.List;

@JsonSerialize(using = FullGroupResponse.GroupSerializer.class)
public class FullGroupResponse {
    private GroupResponse groupResponse;
    private UserResponse userResponse;
    private List<UserResponse> userResponseList;
    private List<FileResponse> fileResponseList;


    public FullGroupResponse(Group group) {
        groupResponse = new GroupResponse(
                group.getId(), group.getName(), group.getDescription(), group.getAllowed_extension_file_types(),
                group.isIs_public(), group.getMax_members_count(), group.getMax_files_count(),
                group.getMax_allowed_file_size_in_mb(),group.getUser() == null? null : group.getUser().getName(), group.getCreated_at(), group.getUpdated_at());
        if (group.getUser() != null) {
            this.userResponse = new UserResponse(group.getUser().getId(), group.getUser().getName(), group.getUser().getEmail(),
                    group.getUser().getRole() == null ? null : group.getUser().getRole().getName(), group.getUser().getCreated_at(), group.getUser().getUpdated_at());
        }
        this.userResponseList = group.getUsers().stream().map(user -> new UserResponse(user.getId(), user.getName(), user.getEmail(),
                user.getRole() == null ? null : user.getRole().getName(), user.getCreated_at(), user.getUpdated_at())
        ).toList();
        this.fileResponseList = group.getFiles().stream().map(file -> new FileResponse(
                file.getId(), file.getName(), file.isIsCheckedIn(), file.getChecked_in_until_time(),
                file.getUser() ==null? null:file.getUser().getName(),group.getName(), file.getUpdated_by(), file.getCreated_at(), file.getUpdated_at()
        )).toList();
    }

    public FullGroupResponse() {
    }

    public GroupResponse getGroupResponse() {
        return groupResponse;
    }

    public void setGroupResponse(GroupResponse groupResponse) {
        this.groupResponse = groupResponse;
    }

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }

    public List<UserResponse> getUserResponseList() {
        return userResponseList;
    }

    public void setUserResponseList(List<UserResponse> userResponseList) {
        this.userResponseList = userResponseList;
    }

    public List<FileResponse> getFileResponseList() {
        return fileResponseList;
    }

    public void setFileResponseList(List<FileResponse> fileResponseList) {
        this.fileResponseList = fileResponseList;
    }

    static class GroupSerializer extends JsonSerializer<FullGroupResponse> {

        @Override
        public void serialize(FullGroupResponse fullGroupResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("groupId", fullGroupResponse.groupResponse.groupId());
            jsonGenerator.writeStringField("name", fullGroupResponse.groupResponse.name());
            jsonGenerator.writeStringField("description", fullGroupResponse.groupResponse.description());
            jsonGenerator.writeStringField("allowed_extension_file_types", fullGroupResponse.groupResponse.allowed_extension_file_types());
            jsonGenerator.writeBooleanField("is_public", fullGroupResponse.groupResponse.is_public());
            jsonGenerator.writeObjectField("max_members_count", fullGroupResponse.groupResponse.max_members_count());
            jsonGenerator.writeObjectField("max_files_count", fullGroupResponse.groupResponse.max_files_count());
            jsonGenerator.writeObjectField("max_allowed_file_size_in_mb", fullGroupResponse.groupResponse.max_allowed_file_size_in_mb());
            jsonGenerator.writeObjectField("created_at", fullGroupResponse.groupResponse.created_at());
            jsonGenerator.writeObjectField("update_at", fullGroupResponse.groupResponse.updated_at());
            jsonGenerator.writeObjectField("admin", fullGroupResponse.userResponse);
            jsonGenerator.writeObjectField("members", fullGroupResponse.userResponseList);
            jsonGenerator.writeObjectField("files", fullGroupResponse.fileResponseList);
            jsonGenerator.writeEndObject();
        }
    }
}
