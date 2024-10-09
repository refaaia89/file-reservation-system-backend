package fifth.year.backendinternetapplication.dto.response.fullData;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fifth.year.backendinternetapplication.dto.response.PermissionResponse;
import fifth.year.backendinternetapplication.dto.response.RoleResponse;
import fifth.year.backendinternetapplication.model.Permission;
import java.io.IOException;
import java.util.List;

@JsonSerialize(using = FullPermissionResponse.PermissionSerializer.class)
public class FullPermissionResponse {
    private PermissionResponse permissionResponse;
    private List<RoleResponse> roleResponsesList;

    public FullPermissionResponse(Permission permission) {
        this.permissionResponse = new PermissionResponse(permission.getId(), permission.getName(),
                permission.getDescription(), permission.getCreated_at(), permission.getUpdated_at());
        this.roleResponsesList = permission.getRoles().stream().map(role -> new RoleResponse(
                role.getId(), role.getName(), role.getCreated_at(), role.getUpdated_at()
        )).toList();
    }

    public FullPermissionResponse() {
    }

    public PermissionResponse getPermissionResponse() {
        return permissionResponse;
    }

    public void setPermissionResponse(PermissionResponse permissionResponse) {
        this.permissionResponse = permissionResponse;
    }

    public List<RoleResponse> getRoleResponsesList() {
        return roleResponsesList;
    }

    public void setRoleResponsesList(List<RoleResponse> roleResponsesList) {
        this.roleResponsesList = roleResponsesList;
    }

    static class PermissionSerializer extends JsonSerializer<FullPermissionResponse> {

        @Override
        public void serialize(FullPermissionResponse fullPermissionResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("permissionId", fullPermissionResponse.permissionResponse.permissionId());
            jsonGenerator.writeStringField("name", fullPermissionResponse.permissionResponse.name());
            jsonGenerator.writeStringField("description", fullPermissionResponse.permissionResponse.description());
            jsonGenerator.writeObjectField("created_at", fullPermissionResponse.permissionResponse.created_at());
            jsonGenerator.writeObjectField("update_at", fullPermissionResponse.permissionResponse.updated_at());
            jsonGenerator.writeObjectField("roles", fullPermissionResponse.roleResponsesList);
            jsonGenerator.writeEndObject();
        }
    }
}
