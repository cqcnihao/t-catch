package com.git.poan.trade.job;

import com.git.poan.trade.bean.Ticker;
import com.git.poan.trade.entity.YieldCoin;
import com.git.poan.trade.mapper.YieldCoinMapper;
import com.git.poan.trade.service.AnalyMarketDeepSerivce;
import com.git.poan.trade.util.HttpUtil;
import com.git.poan.trade.util.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @Author: panbenxing
 * @Date: 2018/12/3
 * @Description:
 */

@Component
@Configurable
@EnableScheduling
public class GenerateYieldCoin {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AnalyMarketDeepSerivce analyMarketDeepSerivce;

    @Autowired
    private YieldCoinMapper yieldCoinMapper;

    //每1分钟执行一次
    @Scheduled(cron = "0 */15 *  * * * ")
    public void reportCurrentByCron(){

        ExecutorService pool = ThreadPool.getPool();
        pool.execute(() -> {
            List<String> resultList = new ArrayList<>();

            for (int i = 0; i < 100; i++) {

                List<String> bidBigger = analyMarketDeepSerivce.getBidBigger();
                resultList.addAll(bidBigger);
                System.out.println("获取中...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Map<String, Long> map = resultList.stream()
                    .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

            Map<String, Long> finalMap = new LinkedHashMap<>();

            map.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue()
                            .reversed()).forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));


            if (CollectionUtils.isEmpty(map)) {
                logger.info("此次没有结果，，，，");
            }

            for (Map.Entry<String, Long> entry : map.entrySet()) {
                String pair = entry.getKey();
                Long count = entry.getValue();


                Ticker ticker = HttpUtil.getTicker(pair);
                if (ticker == null) {
                    logger.error("{}'s ticker is null", pair);
                    continue;
                }
                Double last = ticker.getLast();
                Double quotoVolume = ticker.getQuoteVolume();

//                if (last * quotoVolume < 20d * 10000) {
//                    continue;
//                }

                logger.info("pair-[{}], 流通量-[{}$],排名:[{}]", pair, last * quotoVolume, count);
                if (count < 10) {
                    // todo 记录到数据库
                    YieldCoin yieldCoin = new YieldCoin();
                    yieldCoin.setCoin(pair);
                    yieldCoin.setStatus(1);
                    yieldCoinMapper.insert(yieldCoin);
                }


            }

        });

    }


}
