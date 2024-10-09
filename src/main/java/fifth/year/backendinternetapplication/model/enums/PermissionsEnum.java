package fifth.year.backendinternetapplication.model.enums;

public enum PermissionsEnum {

    USER_READ("user:read"),
    USER_CREATE("user:create"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete"),
    PERMISSION_READ("permission:read"),
    PERMISSION_CREATE("permission:create"),
    PERMISSION_UPDATE("permission:update"),
    PERMISSION_DELETE("permission:delete"),
    ROLE_READ("role:read"),
    ROLE_CREATE("role:create"),
    ROLE_UPDATE("role:update"),
    ROLE_DELETE("role:delete"),
    FILE_READ("file:read"),
    FILE_CREATE("file:create"),
    FILE_UPDATE("file:update"),
    FILE_DELETE("file:delete"),
    GROUP_READ("group:read"),
    GROUP_CREATE("group:create"),
    GROUP_UPDATE("group:update"),
    GROUP_DELETE("group:delete"),
    LOG_READ("log:read"),
    LOG_CREATE("log:create"),
    LOG_UPDATE("log:update"),
    LOG_DELETE("log:delete"),
    LOG_TYPE_READ("log_type:read"),
    LOG_TYPE_CREATE("log_type:create"),
    LOG_TYPE_UPDATE("log_type:update"),
    LOG_TYPE_DELETE("log_type:delete"),
    ;


    private final String permission;

    PermissionsEnum(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
