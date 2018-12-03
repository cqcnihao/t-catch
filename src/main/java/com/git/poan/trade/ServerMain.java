package com.git.poan.trade;

import com.alibaba.fastjson.JSON;
import com.git.poan.trade.bean.Ticker;
import com.git.poan.trade.service.AnalyMarketDeepSerivce;
import com.git.poan.trade.util.HttpUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.git.poan.trade.*")
@MapperScan("com.git.poan.trade.mapper")
public class ServerMain extends SpringBootServletInitializer implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private AnalyMarketDeepSerivce analyMarketDeepSerivce;

    public static void main(String[] args) {
        SpringApplication.run(ServerMain.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("initial...");
    }
}
