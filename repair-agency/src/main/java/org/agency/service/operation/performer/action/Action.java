package org.agency.service.operation.performer.action;

public interface Action {
// FIXME research https://www.tutorialspoint.com/can-enum-implements-an-interface-in-java
     static Action getByName(String name) {
        throw new RuntimeException("To implement exception...");
    }

    String getName();



}
