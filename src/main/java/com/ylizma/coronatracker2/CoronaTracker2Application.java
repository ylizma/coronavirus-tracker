package com.ylizma.coronatracker2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoronaTracker2Application {

    public static void main(String[] args) {
        SpringApplication.run(CoronaTracker2Application.class, args);
    }

}
