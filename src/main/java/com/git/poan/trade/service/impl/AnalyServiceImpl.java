package com.git.poan.trade.service.impl;

import com.alibaba.fastjson.JSON;
import com.git.poan.trade.bean.SinglePairPOJO;
import com.git.poan.trade.service.AnalyService;
import com.git.poan.trade.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: panbenxing
 * @Date: 2019/4/4
 * @Description:
 */
@EnableScheduling
@Service
public class AnalyServiceImpl implements AnalyService {

    @Resource
    private ListOperations<String, Double> listOperations;

    @Resource(name = "allPair")
    private List<String> allPair;

    @Resource
    private ZSetOperations<String, String> zSetOperations;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 每五分钟对，buy中的coin进行查询，并以此记录此时价格
     */
    @Scheduled(cron = "0/8 * * * * ?")
    public void tryBuy() {

        for (String pair : allPair) {
            List<Double> range = listOperations.range(pair, 0, -1);
            if (range.size() <= 9) {
                return;
            }
            Double nowPrice = range.get(0);

            Double second7Ago = range.get(1);

            Double second14Ago = range.get(2);

            Double second21Ago = range.get(3);

            Double second28Ago = range.get(4);


            Double second35Ago = range.get(5);

            Double second63Ago = range.get(9);

            // 等差数列即线性增长
            double change7 = (nowPrice - second7Ago) / second7Ago;
            double change14 = (nowPrice - second14Ago) / second14Ago;
            double change21 = (nowPrice - second28Ago) / second28Ago;
            double change28 = (nowPrice - second28Ago) / second28Ago;
            double change35 = (nowPrice - second35Ago) / second35Ago;
            double change63 = (nowPrice - second63Ago) / second63Ago;

            if (change7 >= 0.02) { // 1%的涨幅？？？
                // 记录该币种的名字，另起任务过后获取其10分钟后的价格
                buy(7, pair, nowPrice);
            } else if (change14 >= 0.025) { //
                // 记录该币种的名字，另起任务过后获取其10分钟后的价格
                buy(14, pair, nowPrice);

            } else if (change21 >= 0.028) {
                // 记录该币种的名字，另起任务过后获取其10分钟后的价格
                buy(21, pair, nowPrice);

            } else if (change28 >= 0.032) { //
                // 记录该币种的名字，另起任务过后获取其10分钟后的价格
                buy(28, pair, nowPrice);

            } else if (change35 >= 0.038) {
                // 记录该币种的名字，另起任务过后获取其10分钟后的价格
                buy(35, pair, nowPrice);

            } else if (change63 >= 0.041) {
                // 记录该币种的名字，另起任务过后获取其10分钟后的价格
                buy(63, pair, nowPrice);

            }


        }

    }


    private void buy(Integer change, String coin, Double price) {
        logger.info("-----------buy-[{}] at price:[{}], pump in [{}] second", coin, price, change);
        ZSetOperations.TypedTuple<String> coinPrice = new DefaultTypedTuple<>(coin, price);
        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        tuples.add(coinPrice);
        zSetOperations.add("buy", tuples);

    }


    /**
     * 每五分钟对，buy中的coin进行查询，并以此记录此时价格
     */
//    @Scheduled(cron = "0 0/5 * * * ?")
    @Scheduled(cron = "0/10 * * * * ?")
    public void log() {

        Set<String> coins = zSetOperations.range("buy", 0, 1);
        for (String coin : coins) {
            SinglePairPOJO singlePair = HttpUtil.getSinglePair(HttpUtil.getSingleClient(), coin);
            // 左push，价格从旧到新排序
            listOperations.rightPush("buy" + coin, singlePair.getLast());
            listOperations.trim("buy" + coin, 0, 9);
            List<Double> range = listOperations.range("buy" + coin, 0, -1);
            logger.info("already buy {} ,price now is :{}", coin, JSON.toJSONString(range));

        }

    }


}
