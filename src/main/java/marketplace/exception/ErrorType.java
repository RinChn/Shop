package marketplace.exception;

import lombok.Getter;

@Getter
public enum ErrorType {
    NONEXISTENT_ARTICLE("There is no product with this article."),
    DUPLICATE("Duplicate article."),
    ERROR_FILE_SAVED("Failed to save workbook to file");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

}
