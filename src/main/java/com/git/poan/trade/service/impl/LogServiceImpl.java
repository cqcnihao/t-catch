package com.git.poan.trade.service.impl;

import com.alibaba.fastjson.JSON;
import com.git.poan.trade.bean.SinglePairPOJO;
import com.git.poan.trade.service.LogService;
import com.git.poan.trade.util.HttpUtil;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @Author: panbenxing
 * @Date: 2019/4/2
 * @Description:
 */
@Service
public class LogServiceImpl implements LogService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "allPair")
    private List<String> allPair;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ListOperations<String,Double> listOperations;

    @Autowired
    private ExecutorService executorService;


    @Scheduled(cron = "0/7 * * * * ?")
    public void log() {
        OkHttpClient singleClient = HttpUtil.getSingleClient();
        List<Future<SinglePairPOJO>> futureList = new ArrayList<>(allPair.size());
        for (String pair : allPair) {
            Future<SinglePairPOJO> pojoFuture = executorService.submit(() -> {
                SinglePairPOJO singlePair = HttpUtil.getSinglePair(singleClient, pair);
                singlePair.setCoinName(pair);
                return singlePair;
            });
            futureList.add(pojoFuture);
        }


        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {

            for (Future<SinglePairPOJO> singlePairPOJOFuture : futureList) {
                try {
                    SinglePairPOJO singlePairPOJO = singlePairPOJOFuture.get();
    //                logger.debug("----------------{}", JSON.toJSONString(singlePairPOJO));
                    String coinName = singlePairPOJO.getCoinName();
                    if (StringUtils.isEmpty(coinName)) {
                        continue;
                    }
                    listOperations.rightPush(coinName,singlePairPOJO.getLast());
                    listOperations.trim(coinName,0,100);
                } catch (InterruptedException | ExecutionException e) {
                    continue;
                }
            }
            return null;
        });

    }


}
