package com.cloud.miaosha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MainApplication{

    public static void main(String[] args) throws Exception {

        SpringApplication.run(MainApplication.class, args);

    }

}
