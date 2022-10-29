package org.example.exception;

public class UnableToWriteToFileError extends Exception {

    private String record;

    public UnableToWriteToFileError(String message, String record) {
        super(message);
        this.record = record;
    }

    public String getRecord() {
        return this.record;
    }

}