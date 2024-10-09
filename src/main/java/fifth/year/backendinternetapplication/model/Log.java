package fifth.year.backendinternetapplication.model;

import fifth.year.backendinternetapplication.model.enums.HttpActionLog;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "logs")
public class Log extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HttpActionLog type;

    @Column(nullable = false)
    private String  data;
    @Column(name = "user_id")
    private Long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Log() {
    }

    public Log(HttpActionLog type, String data, Long userId) {
        this.type = type;
        this.data = data;
        this.userId = userId;
    }

    public HttpActionLog getType() {
        return type;
    }

    public void setType(HttpActionLog type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Log log = (Log) o;
        return id == log.id && type == log.type && Objects.equals(data, log.data) && Objects.equals(userId, log.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, data, userId);
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", type=" + type +
                ", data='" + data + '\'' +
                ", userId=" + userId +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
