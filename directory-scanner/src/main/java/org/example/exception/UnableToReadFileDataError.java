package org.example.exception;

public class UnableToReadFileDataError extends Exception {

    public static final String DEFAULT_ERROR_MESSAGE = "Unable to read data from the file ";

    public UnableToReadFileDataError(String message) {
        super(message);
    }

    public UnableToReadFileDataError(String message, String fileName) {
        super(DEFAULT_ERROR_MESSAGE + fileName + "Error message: " + message);
    }

}