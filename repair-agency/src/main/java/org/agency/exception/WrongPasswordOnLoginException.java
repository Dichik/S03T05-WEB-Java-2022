package org.agency.exception;

public class WrongPasswordOnLoginException extends Exception {

    public WrongPasswordOnLoginException(String message) {
        super(message);
    }

}
