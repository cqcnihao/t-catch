package com.git.poan.trade.service.impl;

import com.alibaba.fastjson.JSON;
import com.git.poan.trade.service.AnalyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @Author: panbenxing
 * @Date: 2019/4/4
 * @Description:
 */
@Service
public class AnalyServiceImpl implements AnalyService {

    @Resource
    private ListOperations<String,Double> listOperations;

    @Resource(name = "allPair")
    private List<String> allPair;

    public void listAll() {

        for (String pair : allPair) {
            List<Double> range = listOperations.range(pair, 0, -1);
            System.out.println("score:"+pair+"--- value:"+ JSON.toJSONString(range));
        }

    }

}
