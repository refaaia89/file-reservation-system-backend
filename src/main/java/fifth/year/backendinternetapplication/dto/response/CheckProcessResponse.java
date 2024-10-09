package fifth.year.backendinternetapplication.dto.response;

import fifth.year.backendinternetapplication.model.File;
import fifth.year.backendinternetapplication.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class CheckProcessResponse
{
    long checkProcessId;
    LocalDateTime checkedInAt;
    LocalDateTime checkedOutAt;
    Boolean isCheckedOutAtTime;
    UserResponse user;
    FileResponse file;
    LocalDateTime created_at;
    LocalDateTime updated_at;

    public CheckProcessResponse() {
    }

    public CheckProcessResponse(long checkProcessId, LocalDateTime checkedInAt, LocalDateTime checkedOutAt, Boolean isCheckedOutAtTime, User user, File file, LocalDateTime created_at, LocalDateTime updated_at) {
        this.checkProcessId = checkProcessId;
        this.checkedInAt = checkedInAt;
        this.checkedOutAt = checkedOutAt;
        this.isCheckedOutAtTime = isCheckedOutAtTime;
        this.user = new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole() == null ? null : user.getRole().getName(), user.getCreated_at(), user.getUpdated_at());
        this.file = new FileResponse(file.getId(), file.getName(), file.isIsCheckedIn(), file.getChecked_in_until_time(), file.getUser() == null ? null : file.getUser().getName(), file.getGroup().getName(),file.getUpdated_by(),  file.getCreated_at(), file.getUpdated_at());
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public long getCheckProcessId() {
        return checkProcessId;
    }

    public void setCheckProcessId(long checkProcessId) {
        this.checkProcessId = checkProcessId;
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

    public Boolean getCheckedOutAtTime() {
        return isCheckedOutAtTime;
    }

    public void setCheckedOutAtTime(Boolean checkedOutAtTime) {
        isCheckedOutAtTime = checkedOutAtTime;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public FileResponse getFile() {
        return file;
    }

    public void setFile(FileResponse file) {
        this.file = file;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckProcessResponse that = (CheckProcessResponse) o;
        return checkProcessId == that.checkProcessId && Objects.equals(checkedInAt, that.checkedInAt) && Objects.equals(checkedOutAt, that.checkedOutAt) && Objects.equals(isCheckedOutAtTime, that.isCheckedOutAtTime) && Objects.equals(user, that.user) && Objects.equals(file, that.file) && Objects.equals(created_at, that.created_at) && Objects.equals(updated_at, that.updated_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkProcessId, checkedInAt, checkedOutAt, isCheckedOutAtTime, user, file, created_at, updated_at);
    }

    @Override
    public String toString() {
        return "CheckProcessResponse{" +
                "checkProcessId=" + checkProcessId +
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
