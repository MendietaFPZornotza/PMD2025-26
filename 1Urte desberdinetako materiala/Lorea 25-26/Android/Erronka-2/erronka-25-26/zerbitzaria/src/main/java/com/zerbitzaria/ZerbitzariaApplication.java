package com.zerbitzaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;

@EnableAsync
@SpringBootApplication
public class ZerbitzariaApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ZerbitzariaApplication.class, args);
    }
}