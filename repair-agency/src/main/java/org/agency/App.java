package org.agency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TODO notifications
 * TODO to cache everything that is possible (password, data, etc.)
 * TODO default login and password for admin
 * TODO add possibility to check feedback about ticket (as a manager?)
 */
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
