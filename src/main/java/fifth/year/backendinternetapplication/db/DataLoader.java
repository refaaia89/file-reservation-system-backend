package fifth.year.backendinternetapplication.db;

import fifth.year.backendinternetapplication.model.*;
import fifth.year.backendinternetapplication.repository.PermissionRepository;
import fifth.year.backendinternetapplication.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataLoader {

    public static void seed(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            PasswordEncoder passwordEncoder
    ) {
        if (roleRepository.count() == 0) {
            List<Role> roleList = getRoles(passwordEncoder);
            List<Permission> permissionList = getPermissions(permissionRepository);
            roleRepository.saveAll(roleList);
            permissionRepository.saveAll(permissionList);
        }
    }

    private static List<Role> getRoles(PasswordEncoder passwordEncoder) {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("User");

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("SuperAdmin");

        var user_read = new Permission("user:read", "");
        var user_create = new Permission("user:create", "");
        var user_update = new Permission("user:update", "");
        var user_delete = new Permission("user:delete", "");
        var permission_read = new Permission("permission:read", "");
        var permission_create = new Permission("permission:create", "");
        var permission_update = new Permission("permission:update", "");
        var permission_delete = new Permission("permission:delete", "");
        var role_read = new Permission("role:read", "");
        var role_create = new Permission("role:create", "");
        var role_update = new Permission("role:update", "");
        var role_delete = new Permission("role:delete", "");
        var file_read = new Permission("file:read", "");
        var file_create = new Permission("file:create", "");
        var file_update = new Permission("file:update", "");
        var file_delete = new Permission("file:delete", "");
        var group_read = new Permission("group:read", "");
        var group_create = new Permission("group:create", "");
        var group_update = new Permission("group:update", "");
        var group_delete = new Permission("group:delete", "");
        var log_read = new Permission("log:read", "");
        var log_type_read = new Permission("log_type:read", "");
        var log_type_create = new Permission("log_type:create", "");
        var log_type_update = new Permission("log_type:update", "");
        var log_type_delete = new Permission("log_type:delete", "");


        var user = new User("User24", "user.user@gmail.com",
                passwordEncoder.encode("user123"), role1);
        user.setId(1);
        var admin = new User("Admin24", "admin.admin@gmail.com",
                passwordEncoder.encode("admin123"), role2);
        admin.setId(2);

        var group0 = new Group();
        group0.setId(1);
        group0.setName("Public");
        group0.setDescription("This Group for All Users");
        group0.setUser(user);
        group0.setIs_public(true);
        var group1 = new Group();
        group1.setId(2);
        group1.setName("ITE");
        group1.setDescription("This Group for ITE Damascus Students");
        group1.setUser(user);
        group1.setIs_public(false);
        var group2 = new Group();
        group2.setId(3);
        group2.setName("TCC");
        group2.setDescription("This Group for TCC Damascus Students");
        group2.setUser(admin);
        group2.setIs_public(false);


        group0.setUsers(
                Set.of(
                       user, admin
                )
        );

        group1.setUsers(
                Set.of(
                        admin
                )
        );

        group2.setUsers(
                Set.of(
                        user, admin
                )
        );

        user.setGroups(Set.of(group0,group2));
        admin.setGroups(Set.of(group0,group1,group2));


        role1.setUsers(Set.of(user));
        role2.setUsers(Set.of(admin));

        role1.setPermissions(
                Set.of(
                        user_read,
                        file_read, file_create, file_update, file_delete,
                        group_read, group_create, group_update, group_delete
                )
        );
        role2.setPermissions(
                Set.of(
                        user_read, user_create, user_update, user_delete,
                        permission_read, permission_create, permission_update, permission_delete,
                        role_read, role_update, role_create, role_delete,
                        file_read, file_create, file_update, file_delete,
                        group_read, group_create, group_update, group_delete,
                        log_read, log_type_read, log_type_create, log_type_update, log_type_delete
                )
        );

        return new ArrayList<>(List.of(role1, role2));
    }

    private static List<Permission> getPermissions(PermissionRepository permissionRepository) {
        var log_create = new Permission("log:create", "");
        var log_update = new Permission("log:update", "");
        var log_delete = new Permission("log:delete", "");
        return new ArrayList<>(List.of(
                log_create, log_delete, log_update
        ));
    }
}