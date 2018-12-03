package com.git.poan.trade.service.impl;

import com.git.poan.trade.bean.MarketDeepPOJO;
import com.git.poan.trade.service.AnalyMarketDeepSerivce;
import com.git.poan.trade.util.HttpUtil;
import io.netty.util.internal.ConcurrentSet;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AnalyMarketDeepSerivceImpl implements AnalyMarketDeepSerivce {


    @Autowired
    private List<String> allPair;

    @Autowired
    private ExecutorService threadPool;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 获取委买单整体大于委卖单的交易对
    @Override
    public List<String> getBidBigger() {

        List<String> pairs = new ArrayList<>();

        OkHttpClient client = HttpUtil.getSingleClient();


        ConcurrentHashMap<String, Future<MarketDeepPOJO>> featureMap = new ConcurrentHashMap<>();

        for (String pair : allPair) {
            Future<MarketDeepPOJO> marketDeepPOJOFuture = threadPool.submit(
                    () -> HttpUtil.getAllMarketDeep(client, pair));
            featureMap.put(pair, marketDeepPOJOFuture);
        }

        for (Map.Entry<String, Future<MarketDeepPOJO>> futureEntry : featureMap.entrySet()) {

            String pair = futureEntry.getKey();
            Future<MarketDeepPOJO> pojoFuture = futureEntry.getValue();

            try {
                MarketDeepPOJO marketDeepPOJO = pojoFuture.get();
                // ask 卖单
                List<List> asks = marketDeepPOJO.getAsks();
                // bid买单
                List<List> bids = marketDeepPOJO.getBids();
                if (bidMoreAsk(-1, bids, asks)) {
                    pairs.add(pair);
                }

            } catch (Exception  e) {
//                e.printStackTrace();
                continue;
            }

        }

        return pairs;

    }


    // todo 获取市场深度时分析top5的卖家买家的出价变化程度，如果没有频繁波动，可能是挂单或着拖单
    private boolean bidMoreAsk(Integer top, List<List> bids, List<List> asks) {
        // asks的顺序颠倒过来，以便处理
        asks = asks.stream().collect(
                Collectors.collectingAndThen(Collectors.toList(), l -> {
                    Collections.reverse(l);
                    return l;
                })
        );


        double askSum = 0, bidSum = 0;

        // 获取全部
        if (top == -1) {
            for (List ask : asks) {
                double singlePrice = Double.valueOf(ask.get(0) + "") * Double.valueOf(ask.get(1) + "");
                askSum += singlePrice;
            }

            for (List bid : bids) {
                double singlePrice = Double.valueOf(bid.get(0) + "") * Double.valueOf(bid.get(1) + "");
                bidSum += singlePrice;
            }
        }
        if (bidSum >= 1.22 * askSum && bidSum <= 1.439 * askSum)
            return true;


        return false;
    }
}
