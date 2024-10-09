package fifth.year.backendinternetapplication.model.enums;

public enum RoleType {

    SUPER_ADMIN("SuperAdmin"),
    USER("User")
    ;
    private final String role;

    RoleType(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
