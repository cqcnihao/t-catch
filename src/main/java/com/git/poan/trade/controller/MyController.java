package com.git.poan.trade.controller;

import com.alibaba.fastjson.JSON;
import com.git.poan.trade.service.AnalyService;
import com.git.poan.trade.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class MyController {


    @Autowired
    private LogService logService;

    @Autowired
    private AnalyService analyService;

    @ResponseBody
    @RequestMapping("")
    public String hello() {
//        logService.log();
        analyService.listAll();
        return "<h1>Hello</h1>";
    }

}
