package marketplace.exception;

import lombok.Getter;

@Getter
public enum ErrorType {
    NONEXISTENT_ARTICLE("There is no product with this article."),
    DUPLICATE("Duplicate article."),
    ERROR_FILE_SAVED("Failed to save workbook to file"),
    NONEXISTENT_FILE_NAME("There is no document with this name."),
    DAMAGED_FILE_CONTENT("Couldn't decode internal file data for download");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

}
