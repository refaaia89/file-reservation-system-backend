package fifth.year.backendinternetapplication.dto.response.main;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Response {
    protected boolean status;

    public Response(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
