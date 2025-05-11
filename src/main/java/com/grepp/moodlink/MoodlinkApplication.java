package com.grepp.moodlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MoodlinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoodlinkApplication.class, args);
    }

}
