package com.github.maciejpalczak.springtodolist.exceptions;

public class NoTaskWithGivenIdException extends Exception {
    public NoTaskWithGivenIdException() {
        super();
    }

    public NoTaskWithGivenIdException(String message) {
        super(message);
    }

    public NoTaskWithGivenIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoTaskWithGivenIdException(Throwable cause) {
        super(cause);
    }
}
