package fifth.year.backendinternetapplication.dto.response.main;

import java.util.Objects;

public class SuccessResponse extends Response  {
    private String message;
    private Object data;

    public SuccessResponse(String message, Object data) {
        super(true);
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuccessResponse that = (SuccessResponse) o;
        return Objects.equals(message, that.message) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, data);
    }

    @Override
    public String toString() {
        return "SuccessResponse{" +
                " status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
