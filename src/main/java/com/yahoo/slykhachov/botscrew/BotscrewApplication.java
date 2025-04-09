package com.yahoo.slykhachov.botscrew;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotscrewApplication {
    static {
        // Force the property to be set early
        System.setProperty("java.awt.headless", "false");
    }

    public static void main(String[] args) {
        SpringApplication.run(BotscrewApplication.class, args);
    }

}
