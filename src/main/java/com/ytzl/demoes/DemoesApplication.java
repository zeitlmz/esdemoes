package com.ytzl.demoes;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ytzl.demoes.dao")
public class DemoesApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoesApplication.class, args);
    }

}
