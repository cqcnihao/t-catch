package com.git.poan.trade.controller;

import com.alibaba.fastjson.JSON;
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
    private LogService logService;

    @ResponseBody
    @RequestMapping("")
    public String hello() {
        logService.log();
        return "<h1>Hello</h1>";
    }

}
