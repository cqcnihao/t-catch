package com.git.poan.trade.controller;

import com.git.poan.trade.service.AnalyService;
import com.git.poan.trade.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
        analyService.tryBuy();
        return "<h1>Hello</h1>";
    }

}
