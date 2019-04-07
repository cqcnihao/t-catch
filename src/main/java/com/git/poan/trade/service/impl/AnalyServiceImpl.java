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
     * 每三秒分析一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void tryBuy() {

        int end = 6;
        double pump = 0;
        double expect = 0.024;
        for (String pair : allPair) {
            // 取前三十秒的数据

            List<Double> range = listOperations.range(pair, 0, -1);
            if (range.size() < end) {
                return;
            }

            // fixme 如果该币种的交易量过低 ，则不考虑（小盘不好跟）
            SinglePairPOJO singlePair = HttpUtil.getSinglePair(HttpUtil.getSingleClient(), pair);
            if (singlePair == null) {
                continue;
            }
            if (singlePair.getBaseVolume() * singlePair.getLast() <= 90*10000) {
                continue;
            }



            for (int i = 0; i < range.size() - 1; i++) {
                double raise = (range.get(i) - range.get(i+1))/range.get(i+1);
                if (raise > 0) // 去噪音
                    pump += raise ;
            }
            if (pump >= expect) {
                buy(pair, range.get(0));
            }



        }

    }


//    @Scheduled(cron = "0/10 * * * * ?")
    public void tryBuy10() {

        for (String pair : allPair) {
            List<Double> range = listOperations.range(pair, 0, -1);
            if (range.size() <= 6) {
                return;
            }

            // fixme 如果该币种的交易量过低 ，则不考虑（小盘不好跟）
            SinglePairPOJO singlePair = HttpUtil.getSinglePair(HttpUtil.getSingleClient(), pair);
            if (singlePair == null) {
                continue;
            }
            if (singlePair.getBaseVolume() * singlePair.getLast() <= 80*10000) {
                continue;
            }

            Double nowPrice = range.get(0);

            Double second10Ago = range.get(1);

            Double second20Ago = range.get(2);

            Double second30Ago = range.get(3);

            Double second40Ago = range.get(4);


            Double second50Ago = range.get(5);


            // 等差数列即线性增长  [1.4,1.2,1,1,0.9,0.8]
            double change10 = (nowPrice - second10Ago) / second10Ago;
            double change20 = (second10Ago - second20Ago) / second20Ago;
            double change30 = (second20Ago - second30Ago) / second30Ago;
            double change40 = (second30Ago - second40Ago) / second40Ago;
            double change50 = (second40Ago - second50Ago) / second50Ago;


            double expect = 0.008;

            int pump = 0;
            int change = 0;
            if (change10 >= expect) { // 1%的涨幅？？？
                // 记录该币种的名字，另起任务过后获取其10分钟后的价格
                change=7;
                pump++;
            }  if (change20 >= expect) { //
                // 记录该币种的名字，另起任务过后获取其10分钟后的价格
                change=14;
                pump++;
            }  if (change30 >= expect) {
                // 记录该币种的名字，另起任务过后获取其10分钟后的价格
                change=21;
                pump++;


            }  if (change40 >= expect) { //
                // 记录该币种的名字，另起任务过后获取其10分钟后的价格
                change=28;
                pump++;

            }  if (change50 >= expect) {
                // 记录该币种的名字，另起任务过后获取其10分钟后的价格
                change=35;
                pump++;

            }
            if (pump >=3) {
                buy(pair, nowPrice);
            }



        }

    }


    private void buy(String coin, Double price) {
        logger.info("-----------buy-[{}] at price:[{}]", coin, price);
        ZSetOperations.TypedTuple<String> coinPrice = new DefaultTypedTuple<>(coin, price);
        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        tuples.add(coinPrice);
        zSetOperations.add("buy", tuples);

    }


    /**
     * 每五分钟对，buy中的coin进行查询，并以此记录此时价格
     */
    @Scheduled(cron = "0 0/5 * * * ?")
//    @Scheduled(cron = "0/10 * * * * ?")
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
