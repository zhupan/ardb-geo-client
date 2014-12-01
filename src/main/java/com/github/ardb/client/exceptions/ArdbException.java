package com.github.ardb.client.exceptions;


public class ArdbException extends RuntimeException {

    public ArdbException(String message) {
        super(message);
    }

    public ArdbException(Throwable e) {
        super(e);
    }

}
