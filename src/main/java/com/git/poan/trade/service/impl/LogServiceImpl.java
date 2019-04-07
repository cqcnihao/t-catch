package com.git.poan.trade.service.impl;

import com.git.poan.trade.bean.SinglePairPOJO;
import com.git.poan.trade.service.AnalyService;
import com.git.poan.trade.service.LogService;
import com.git.poan.trade.util.HttpUtil;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @Author: panbenxing
 * @Date: 2019/4/2
 * @Description:
 */
@Service
@EnableScheduling
public class LogServiceImpl implements LogService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "allPair")
    private List<String> allPair;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private ListOperations<String,Double> listOperations;


    @Autowired
    private ExecutorService executorService;

    private final static int count_limit = 49;




//    @Scheduled(cron = "0/10 * * * * ?")

    @Scheduled(cron = "0/2 * * * * ?")
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

                    /**
                     * 左push，则最新push的再第一个，即2为第一个；币价最新的排第一个
                     * lpush list 0 1 2
                     * 当最新的币价入队列时
                     * lpush list 3
                     * 将最老的币价除去
                     * ltrim list 0 2
                     *
                     */

                    listOperations.leftPush(coinName,singlePairPOJO.getLast());
                    listOperations.trim(coinName,0,count_limit);
                } catch (InterruptedException | ExecutionException e) {
                    continue;
                }
            }
            return null;
        });



    }


}
