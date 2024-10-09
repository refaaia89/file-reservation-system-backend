package fifth.year.backendinternetapplication.dto.response.fullData;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fifth.year.backendinternetapplication.dto.response.PermissionResponse;
import fifth.year.backendinternetapplication.dto.response.RoleResponse;
import fifth.year.backendinternetapplication.dto.response.UserResponse;
import fifth.year.backendinternetapplication.model.Role;

import java.io.IOException;
import java.util.List;

@JsonSerialize(using = FullRoleResponse.RoleSerializer.class)
public class FullRoleResponse {
    private RoleResponse roleResponse;
    private List<PermissionResponse> permissionResponseList;

    private List<UserResponse> userResponseList;

    public FullRoleResponse(Role role) {
        this.roleResponse = new RoleResponse(role.getId(), role.getName(), role.getCreated_at(), role.getUpdated_at());
        this.permissionResponseList = role.getPermissions().stream().map(permission -> new PermissionResponse(permission.getId(),
                permission.getName(), permission.getDescription(), permission.getCreated_at(), permission.getUpdated_at()
        )).toList();
        this.userResponseList = role.getUsers().stream().map(user -> new UserResponse(user.getId(), user.getName(),
                user.getEmail(), user.getRole() == null ? null : user.getRole().getName(), user.getCreated_at(), user.getUpdated_at()
        )).toList();
    }

    public FullRoleResponse() {
    }

    public RoleResponse getRoleResponse() {
        return roleResponse;
    }

    public void setRoleResponse(RoleResponse roleResponse) {
        this.roleResponse = roleResponse;
    }

    public List<PermissionResponse> getPermissionResponseList() {
        return permissionResponseList;
    }

    public void setPermissionResponseList(List<PermissionResponse> permissionResponseList) {
        this.permissionResponseList = permissionResponseList;
    }

    public List<UserResponse> getUserResponseList() {
        return userResponseList;
    }

    public void setUserResponseList(List<UserResponse> userResponseList) {
        this.userResponseList = userResponseList;
    }

    static class RoleSerializer extends JsonSerializer<FullRoleResponse> {

        @Override
        public void serialize(FullRoleResponse fullRoleResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("roleId", fullRoleResponse.roleResponse.roleId());
            jsonGenerator.writeStringField("name", fullRoleResponse.roleResponse.name());
            jsonGenerator.writeObjectField("created_at", fullRoleResponse.roleResponse.created_at());
            jsonGenerator.writeObjectField("update_at", fullRoleResponse.roleResponse.updated_at());
            jsonGenerator.writeObjectField("permissions", fullRoleResponse.permissionResponseList);
            jsonGenerator.writeObjectField("users", fullRoleResponse.userResponseList);
            jsonGenerator.writeEndObject();
        }
    }
}
