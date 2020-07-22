package theatrical.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PlayType {
    TRAGEDY("tragedy"), COMEDY("comedy");

    private String tragedy;

    PlayType(String tragedy) {
        this.tragedy = tragedy;
    }

    @JsonValue
    public String getTragedy() {
        return tragedy;
    }
}
