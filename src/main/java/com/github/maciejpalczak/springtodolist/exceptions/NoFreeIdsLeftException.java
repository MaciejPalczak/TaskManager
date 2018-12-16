package com.github.maciejpalczak.springtodolist.exceptions;

public class NoFreeIdsLeftException extends Exception {
    public NoFreeIdsLeftException() {
        super();
    }

    public NoFreeIdsLeftException(String message) {
        super(message);
    }

    public NoFreeIdsLeftException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoFreeIdsLeftException(Throwable cause) {
        super(cause);
    }
}
