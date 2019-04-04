package com.git.poan.trade.controller;

import com.alibaba.fastjson.JSON;
import com.git.poan.trade.service.AnalyMarketDeepSerivce;
import com.git.poan.trade.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class MyController {

    @Autowired
    private AnalyMarketDeepSerivce analyMarketDeepSerivce;

    @Autowired
    private LogService logService;

    @ResponseBody
    @RequestMapping("")
    public String hello() {
        logService.log();
        return "<h1>Hello</h1>";
    }

    @RequestMapping("/start")
    public void start() throws InterruptedException {
        List<String> resultList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {

            List<String> bidBigger = analyMarketDeepSerivce.getBidBigger();
            resultList.addAll(bidBigger);
            System.out.println("获取中...");
            Thread.sleep(3000);
        }

        Map<String, Long> map = resultList.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));


        System.out.println(JSON.toJSONString(map));
    }
}
