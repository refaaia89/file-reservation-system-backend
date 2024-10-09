package fifth.year.backendinternetapplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "users_username_unique", columnNames = "username"),
                @UniqueConstraint(name = "users_email_unique", columnNames = "email"),
        }
)
public class User extends Auditable implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 100, nullable = false, name = "username")
    private String name;

    @Column(nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    private Set<Token> tokens;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<File> files;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<CheckProcess> reservations;

    @ManyToMany(mappedBy = "users",cascade = {CascadeType.MERGE})
    private Set<Group> groups;

    public User() {
    }

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.grantedAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public Set<Token> getTokens() {
        if (tokens == null) {
            tokens=new HashSet<>();
        }
        return tokens;
    }

    public void setTokens(Set<Token> tokens) {
        this.tokens = tokens;
    }

    public Set<Group> getGroups() {
        if (groups == null) {
            groups=new HashSet<>();
        }
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public Set<File> getFiles() {
        if (files == null) {
            files=new HashSet<>();
        }
        return files;
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(getUsername(), getUsername());
    }

    public Set<CheckProcess> getReservations() {
        return reservations;
    }

    public void setReservations(Set<CheckProcess> reservations) {
        if (reservations == null) {
            reservations=new HashSet<>();
        }
        this.reservations = reservations;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", tokens=" + tokens +
                ", files=" + files +
                ", groups=" + groups +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
