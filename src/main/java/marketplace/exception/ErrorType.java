package marketplace.exception;

import lombok.Getter;

@Getter
public enum ErrorType {
    NONEXISTENT_ARTICLE("There is no product with this article"),
    UNREGISTERED_MAIL("The user with this email has not been registered yet"),
    NOT_ENOUGH_PRODUCTS("There is not enough product in the warehouse"),
    UNIDENTIFIED_USER("First, log in to your account"),
    NONEXISTEN_ORDER("There is no order with this number in your profile."),
    SET_STATUS_FOR_CANCELLED_ORDER("You cannot change the status of a cancelled order, select another one."),
    SET_NOT_CREATED_ORDER("You can only change an order with the CREATED status. " +
            "Request a status change or select another order."),
    OPEN_ORDER_ALREADY_EXISTS("There is already an open order in your profile, cancel or complete it first"),
    NONEXISTENT_PRODUCT_IN_ORDER("There is no such product in this order."),
    DUPLICATE("Duplicate"),
    ERROR_FILE_SAVED("Failed to save workbook to file"),
    INVALID_FILE_CONTENT("Couldn't convert file contents to save"),
    NONEXISTEN_FILE("There is no report with this name"),
    INVALID_EXCHANGE_RATE_FILE("Invalid exchange rate file");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

}
