package com.grepp.moodlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.grepp.moodlink.app.model")
public class MoodlinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoodlinkApplication.class, args);
    }

}
