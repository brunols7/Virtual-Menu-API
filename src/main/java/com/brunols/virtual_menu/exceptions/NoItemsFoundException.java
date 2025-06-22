package com.brunols.virtual_menu.exceptions;

public class NoItemsFoundException extends RuntimeException {
    public NoItemsFoundException(String message) {
        super(message);
    }
}
