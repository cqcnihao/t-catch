package com.git.poan.trade;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import static java.util.Map.Entry.comparingByValue;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.git.poan.trade.*")
@MapperScan("com.git.poan.trade.mapper")
public class ServerMain extends SpringBootServletInitializer implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public static void main(String[] args) {
        SpringApplication.run(ServerMain.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("initial...");
    }
}
