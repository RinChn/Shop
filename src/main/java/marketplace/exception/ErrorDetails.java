package marketplace.exception;

import lombok.Builder;

import java.sql.Timestamp;

@Builder
public class ErrorDetails {
    public String exceptionName;
    public String message;
    public Timestamp timestamp;
}
