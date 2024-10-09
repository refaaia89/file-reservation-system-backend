package fifth.year.backendinternetapplication.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(
        name = "groups",
        uniqueConstraints = {
                @UniqueConstraint(name = "groups_name_unique", columnNames = "name"),
        }
)
public class Group extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50, nullable = false)
    private String name;

    private String description;

    private String allowed_extension_file_types;

    @Column(nullable = false)
    private boolean is_public;

    private Integer max_members_count;
    private Integer max_files_count;
    private Integer max_allowed_file_size_in_mb;

    @ManyToOne(fetch = FetchType.EAGER,cascade = {CascadeType.MERGE})
    @JoinColumn(name = "administrator_id")
    private User user;

    @OneToMany(mappedBy = "group", cascade = {CascadeType.ALL})
    private Set<File> files;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "group_members",
            joinColumns = { @JoinColumn(name = "group_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private Set<User> users;


    public Group() {
    }

    public Group(long id, String name, String description, String allowed_extension_file_types, boolean is_public, Integer max_members_count, Integer max_files_count, Integer max_allowed_file_size_in_mb, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.allowed_extension_file_types = allowed_extension_file_types;
        this.is_public = is_public;
        this.max_members_count = max_members_count;
        this.max_files_count = max_files_count;
        this.max_allowed_file_size_in_mb = max_allowed_file_size_in_mb;
        this.user = user;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAllowed_extension_file_types() {
        return allowed_extension_file_types;
    }

    public void setAllowed_extension_file_types(String allowed_extension_file_types) {
        this.allowed_extension_file_types = allowed_extension_file_types;
    }

    public boolean isIs_public() {
        return is_public;
    }

    public void setIs_public(boolean is_public) {
        this.is_public = is_public;
    }

    public Integer getMax_members_count() {
        return max_members_count;
    }

    public void setMax_members_count(Integer max_members_count) {
        this.max_members_count = max_members_count;
    }

    public Integer getMax_files_count() {
        return max_files_count;
    }

    public void setMax_files_count(Integer max_files_count) {
        this.max_files_count = max_files_count;
    }

    public Integer getMax_allowed_file_size_in_mb() {
        return max_allowed_file_size_in_mb;
    }

    public void setMax_allowed_file_size_in_mb(Integer max_allowed_file_size_in_mb) {
        this.max_allowed_file_size_in_mb = max_allowed_file_size_in_mb;
    }

    public Set<File> getFiles() {
        if (files == null) {
            return new HashSet<>();
        }
        return files;
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<User> getUsers() {
        if (users == null) {
            users = new HashSet<>();
        }
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id == group.id && is_public == group.is_public && Objects.equals(name, group.name) && Objects.equals(description, group.description) && Objects.equals(allowed_extension_file_types, group.allowed_extension_file_types) && Objects.equals(max_members_count, group.max_members_count) && Objects.equals(max_files_count, group.max_files_count) && Objects.equals(max_allowed_file_size_in_mb, group.max_allowed_file_size_in_mb) && Objects.equals(user, group.user) && Objects.equals(files, group.files) && Objects.equals(users, group.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "GroupRepository{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", allowed_extension_file_types='" + allowed_extension_file_types + '\'' +
                ", is_public=" + is_public +
                ", max_members_count=" + max_members_count +
                ", max_files_count=" + max_files_count +
                ", max_allowed_file_size_in_mb=" + max_allowed_file_size_in_mb +
                ", user=" + user +
                ", files=" + files +
                ", users=" + users +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }


}
