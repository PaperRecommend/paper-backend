package com.example.paper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.paper.dao")
@SpringBootApplication
public class PaperApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaperApplication.class, args);
    }

}
