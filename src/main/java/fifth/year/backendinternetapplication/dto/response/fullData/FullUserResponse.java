package fifth.year.backendinternetapplication.dto.response.fullData;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fifth.year.backendinternetapplication.dto.response.FileResponse;
import fifth.year.backendinternetapplication.dto.response.GroupResponse;
import fifth.year.backendinternetapplication.dto.response.UserResponse;
import fifth.year.backendinternetapplication.model.User;

import java.io.IOException;
import java.util.List;

@JsonSerialize(using = FullUserResponse.UserSerializer.class)
public class FullUserResponse {

    private UserResponse userResponse;
    private List<GroupResponse> groupResponsesList;
    private List<FileResponse> fileResponseList;


    public FullUserResponse(User user) {
        this.userResponse = new UserResponse(user.getId(), user.getName(), user.getEmail(),
                user.getRole() == null ? null : user.getRole().getName(), user.getCreated_at(), user.getUpdated_at());
        this.groupResponsesList = user.getGroups().stream().map(group -> new GroupResponse(
                group.getId(), group.getName(), group.getDescription(), group.getAllowed_extension_file_types(),
                group.isIs_public(), group.getMax_members_count(), group.getMax_files_count(),
                group.getMax_allowed_file_size_in_mb(), group.getUser() == null ? null : group.getUser().getName(), group.getCreated_at(), group.getUpdated_at()
        )).toList();
        this.fileResponseList = user.getFiles().stream().map(file -> new FileResponse(
                file.getId(), file.getName(), file.isIsCheckedIn(), file.getChecked_in_until_time(),
                user.getName(), file.getGroup() == null ? null : file.getGroup().getName(), file.getUpdated_by(), file.getCreated_at(), file.getUpdated_at()
        )).toList();
    }

    public FullUserResponse() {
    }

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }

    public List<GroupResponse> getGroupResponsesList() {
        return groupResponsesList;
    }

    public void setGroupResponsesList(List<GroupResponse> groupResponsesList) {
        this.groupResponsesList = groupResponsesList;
    }

    public List<FileResponse> getFileResponseList() {
        return fileResponseList;
    }

    public void setFileResponseList(List<FileResponse> fileResponseList) {
        this.fileResponseList = fileResponseList;
    }


    static class UserSerializer extends JsonSerializer<FullUserResponse> {

        @Override
        public void serialize(FullUserResponse fullUserResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("userId", fullUserResponse.userResponse.userId());
            jsonGenerator.writeStringField("username", fullUserResponse.userResponse.username());
            jsonGenerator.writeStringField("email", fullUserResponse.userResponse.email());
            jsonGenerator.writeStringField("role", fullUserResponse.userResponse.role());
            jsonGenerator.writeObjectField("created_at", fullUserResponse.userResponse.created_at());
            jsonGenerator.writeObjectField("update_at", fullUserResponse.userResponse.updated_at());
            jsonGenerator.writeObjectField("files", fullUserResponse.fileResponseList);
            jsonGenerator.writeObjectField("groups", fullUserResponse.groupResponsesList);
            jsonGenerator.writeEndObject();
        }
    }
}