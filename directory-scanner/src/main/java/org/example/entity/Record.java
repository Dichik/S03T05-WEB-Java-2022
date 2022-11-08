package org.example.entity;

import java.util.List;

public class Record {

    private final List<String> messages;
    private final String path;

    public Record(List<String> messages, String path) {
        this.messages = messages;
        this.path = path;
    }

    public List<String> getMessages() {
        return messages;
    }

    public String getPath() {
        return path;
    }
}