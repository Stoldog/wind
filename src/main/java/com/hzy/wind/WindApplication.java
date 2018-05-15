package com.hzy.wind;

import com.hzy.wind.launcher.ChatLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WindApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatLauncher.class, args);
    }
}
