package marketplace.exception;

import lombok.Getter;

@Getter
public enum ErrorType {
    NONEXISTENT_ARTICLE("There is no product with this article."),
    DUPLICATE("Duplicate article.");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

}
