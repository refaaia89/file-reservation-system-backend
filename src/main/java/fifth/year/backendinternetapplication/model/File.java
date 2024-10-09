package fifth.year.backendinternetapplication.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "files")
@EntityListeners(AuditingEntityListener.class)
public class File extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_checked_in")
    private boolean isCheckedIn;

    private LocalDateTime checked_in_until_time;

    @Version
    private Long version;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    protected long created_by;

    @LastModifiedBy
    @Column(insertable = false)
    protected long updated_by;

    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "file", cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<CheckProcess> reservations;

    public File(String name,User user,Group group) {
        this.name = name;
        this.user = user;
        this.group = group;
    }

    public File() {
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

    public boolean isIsCheckedIn() {
        return isCheckedIn;
    }

    public void setIsCheckedIn(boolean isCheckedIn) {
        this.isCheckedIn = isCheckedIn;
    }

    public LocalDateTime getChecked_in_until_time() {
        return checked_in_until_time;
    }

    public void setChecked_in_until_time(LocalDateTime checked_in_until_time) {
        this.checked_in_until_time = checked_in_until_time;
    }

    public User getUser() {
        /*if (user == null) {
            user =new User();
        }*/
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        /*if (group == null) {
            group=new Group();
        }*/
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public long getCreated_by() {
        return created_by;
    }

    public void setCreated_by(long created_by) {
        this.created_by = created_by;
    }

    public long getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(long updated_by) {
        this.updated_by = updated_by;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return id == file.id && isCheckedIn == file.isCheckedIn && created_by == file.created_by && updated_by == file.updated_by && Objects.equals(name, file.name) && Objects.equals(checked_in_until_time, file.checked_in_until_time) && Objects.equals(user, file.user) && Objects.equals(group, file.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", is_checked_in=" + isCheckedIn +
                ", checked_in_until_time=" + checked_in_until_time +
                ", created_by=" + created_by +
                ", updated_by=" + updated_by +
                ", user=" + user +
                ", group=" + group +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }


}
