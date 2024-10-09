package fifth.year.backendinternetapplication.dto.response.fullData;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fifth.year.backendinternetapplication.dto.response.FileResponse;
import fifth.year.backendinternetapplication.dto.response.GroupResponse;
import fifth.year.backendinternetapplication.dto.response.UserResponse;
import fifth.year.backendinternetapplication.model.File;

import java.io.IOException;

@JsonSerialize(using = FullFileResponse.FileSerializer.class)
public class FullFileResponse {
    private FileResponse fileResponse;
    private UserResponse userResponse;
    private GroupResponse groupResponses;

    public FullFileResponse(File file) {
        this.fileResponse = new FileResponse(file.getId(),file.getName(),file.isIsCheckedIn(),file.getChecked_in_until_time(),
                null,null,file.getUpdated_by(),file.getCreated_at(),file.getUpdated_at());
        if (file.getUser() != null) {
            this.userResponse =
                    new UserResponse(file.getUser().getId(), file.getUser().getName(), file.getUser().getEmail(),
                            file.getUser().getRole() == null ? null : file.getUser().getRole().getName(), file.getUser().getCreated_at(), file.getUser().getUpdated_at());
        }
        if (file.getGroup() != null) {
            this.groupResponses = new GroupResponse(
                    file.getGroup().getId(), file.getGroup().getName(), file.getGroup().getDescription(),
                    file.getGroup().getAllowed_extension_file_types(), file.getGroup().isIs_public(),
                    file.getGroup().getMax_members_count(), file.getGroup().getMax_files_count(),
                    file.getGroup().getMax_allowed_file_size_in_mb(),file.getGroup().getUser() ==null? null : file.getGroup().getUser().getName(),
                    file.getGroup().getCreated_at(), file.getGroup().getUpdated_at());
        }
    }

    public FullFileResponse() {
    }

    public FileResponse getFileResponse() {
        return fileResponse;
    }

    public void setFileResponse(FileResponse fileResponse) {
        this.fileResponse = fileResponse;
    }

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }

    public GroupResponse getGroupResponses() {
        return groupResponses;
    }

    public void setGroupResponses(GroupResponse groupResponses) {
        this.groupResponses = groupResponses;
    }

    static class FileSerializer extends JsonSerializer<FullFileResponse> {

        @Override
        public void serialize(FullFileResponse fullFileResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("fileId", fullFileResponse.fileResponse.fileId());
            jsonGenerator.writeStringField("name", fullFileResponse.fileResponse.name());
            jsonGenerator.writeObjectField("is_checked_in", fullFileResponse.fileResponse.is_checked_in());
            jsonGenerator.writeObjectField("checked_in_until_time", fullFileResponse.fileResponse.checked_in_until_time());
            jsonGenerator.writeObjectField("created_at", fullFileResponse.fileResponse.created_at());
            jsonGenerator.writeObjectField("update_at", fullFileResponse.fileResponse.updated_at());
            jsonGenerator.writeObjectField("user", fullFileResponse.userResponse);
            jsonGenerator.writeObjectField("group", fullFileResponse.groupResponses);
            jsonGenerator.writeEndObject();
        }
    }
}
