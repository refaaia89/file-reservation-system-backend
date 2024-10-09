package fifth.year.backendinternetapplication.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "permissions")
public class Permission extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100,unique = true)
    @NotNull
    private String name;

    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;

    public Permission(){
        created_at=LocalDateTime.now();
    }

    public Permission(String name, String description) {
        created_at = LocalDateTime.now();
        this.name = name;
        if (!description.isEmpty())
            this.description = description;
    }

    public Permission(Long id, String name, String description, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Role> getRoles() {
        if (roles == null) {
            roles = new HashSet<>();
        }
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", roles=" + roles +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
