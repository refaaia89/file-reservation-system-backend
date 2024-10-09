package fifth.year.backendinternetapplication.model.enums;

public enum HttpActionLog {
    REQUEST("Request"),
    RESPONSE("Response"),
    EXCEPTION("Exception")
    ;
    private final String type;

    HttpActionLog(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
