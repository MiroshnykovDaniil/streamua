package com.example.twitchapi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class TwitchApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TwitchApiApplication.class, args);
    }

}
