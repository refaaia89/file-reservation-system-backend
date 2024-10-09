package fifth.year.backendinternetapplication.model;

import fifth.year.backendinternetapplication.model.enums.PermissionsEnum;
import jakarta.persistence.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;


@Entity
@Table(
        name = "roles",
        uniqueConstraints = {
                @UniqueConstraint(name = "roles_name_unique", columnNames = "name"),
        }
)
public class Role extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50,nullable = false)
    private String name;

    @OneToMany(mappedBy = "role", cascade = {CascadeType.MERGE})
    private Set<User> users;

    @ManyToMany(cascade = { CascadeType.MERGE },fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_permissions",
            joinColumns = { @JoinColumn(name = "role_id") },
            inverseJoinColumns = { @JoinColumn(name = "permission_id") }
    )
    private Set<Permission> permissions;

    @Transient
    private Set<PermissionsEnum> permissionsEnums = new HashSet<>();


    public Set<PermissionsEnum> getPermissionsEnums() {
        if (permissionsEnums.isEmpty()) {
            for (Permission permission :
                    permissions) {
                try {
                    permissionsEnums.add(
                            PermissionsEnum.valueOf(
                                    permission.getName().toUpperCase().replace(":", "_")
                            )
                    );
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return permissionsEnums;
    }

    public List<SimpleGrantedAuthority> grantedAuthorities() {
        var authoritiesList = getPermissionsEnums().stream().map(permissionsEnum -> new SimpleGrantedAuthority(permissionsEnum.getPermission())).collect(Collectors.toCollection(ArrayList::new));
        authoritiesList.add(new SimpleGrantedAuthority(this.name));
        return authoritiesList;
    }

    public Role(){
    }

    public Role(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        if (users == null) {
            users=new HashSet<>();
        }
        return users;
    }

    public Set<Permission> getPermissions() {
        if (permissions == null) {
            permissions=new HashSet<>();
        }
        return permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id == role.id && Objects.equals(name, role.name) && Objects.equals(users, role.users) && Objects.equals(permissions, role.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", users=" + users +
                ", permissions=" + permissions +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
