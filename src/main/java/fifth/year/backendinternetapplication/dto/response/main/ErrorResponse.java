package fifth.year.backendinternetapplication.dto.response.main;

import java.util.Objects;

public class ErrorResponse extends Response {

    private String error;
    private Object errors;

    public ErrorResponse(String error, Object errors) {
        super(false);
        this.error = error;
        this.errors = errors;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponse that = (ErrorResponse) o;
        return Objects.equals(error, that.error) && Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, errors);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error='" + error + '\'' +
                ", errors=" + errors +
                ", status=" + status +
                '}';
    }
}
