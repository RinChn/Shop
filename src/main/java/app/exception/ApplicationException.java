package app.exception;

public class ApplicationException extends RuntimeException {
    public ApplicationException(ErrorType errorType) {
        super(errorType.getMessage());
    }
}
