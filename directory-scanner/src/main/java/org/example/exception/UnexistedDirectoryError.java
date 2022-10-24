package org.example.exception;

public class UnexistedDirectoryError extends Exception {

    public static final String DEFAULT_ERROR_MESSAGE = "Directory was not found, please specify the correct one.";

    public UnexistedDirectoryError() {
        super(DEFAULT_ERROR_MESSAGE);
    }
    public UnexistedDirectoryError(String message) {
        super(message);
    }
}