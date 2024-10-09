package fifth.year.backendinternetapplication.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "check_processes")
public class CheckProcess extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, updatable = false, name = "checked_in_at")
    private LocalDateTime checkedInAt;
    @Column(name = "checked_out_at")
    private LocalDateTime checkedOutAt;

    @Column(name = "is_checked_out_at_time")
    private Boolean isCheckedOutAtTime;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "file_id")
    private File file;

    public CheckProcess(User user, File file) {
        this.user = user;
        this.file = file;
    }

    public CheckProcess() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCheckedInAt() {
        return checkedInAt;
    }

    public void setCheckedInAt(LocalDateTime checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    public LocalDateTime getCheckedOutAt() {
        return checkedOutAt;
    }

    public void setCheckedOutAt(LocalDateTime checkedOutAt) {
        this.checkedOutAt = checkedOutAt;
    }

    public Boolean isCheckedOutAtTime() {
        return isCheckedOutAtTime;
    }

    public void setCheckedOutAtTime(Boolean checkedOutAtTime) {
        isCheckedOutAtTime = checkedOutAtTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckProcess that = (CheckProcess) o;
        return id == that.id && isCheckedOutAtTime == that.isCheckedOutAtTime && Objects.equals(checkedInAt, that.checkedInAt) && Objects.equals(checkedOutAt, that.checkedOutAt) && Objects.equals(user, that.user) && Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, checkedInAt, checkedOutAt, isCheckedOutAtTime, user, file);
    }

    @Override
    public String toString() {
        return "CheckProcess{" +
                "id=" + id +
                ", checkedInAt=" + checkedInAt +
                ", checkedOutAt=" + checkedOutAt +
                ", isCheckedOutAtTime=" + isCheckedOutAtTime +
                ", user=" + user +
                ", file=" + file +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
