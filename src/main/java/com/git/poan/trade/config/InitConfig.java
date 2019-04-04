package com.git.poan.trade.config;

import com.alibaba.fastjson.JSON;
import com.git.poan.trade.common.GateApi;
import com.git.poan.trade.util.HttpUtil;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Configuration
public class InitConfig {
    private final List<String> exclude = Arrays.asList("BTC_USDT", "ETH_USDT",
            "LTC_USDT", "BCH_USDT", "ETC", "QTUM");
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean(name = "allPair")
    public List<String> allPair() {
        Request request = new Request.Builder()
                .url(GateApi.ALL_PAIR.getUrl())
                .build();
        OkHttpClient client = HttpUtil.getSingleClient();
        Call call = client.newCall(request);


        try {
            Response response = call.execute();
            ResponseBody body = response.body();
            if (body == null) {
                logger.error("gata Api err!!");
                return null;
            }
            String res = body.string();//
            List<String> list = JSON.parseObject(res, List.class);
            list = list.stream()
                    .filter(coin -> {
                        if (exclude.contains(coin)||!coin.endsWith("_USDT")) {
                            return false;
                        }
//                        if (coin.endsWith("_USDT")) {
//                            return true;
//                        }
                        return true;
                    })
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
            return list;
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }


    @Bean
    public ExecutorService threadPool(){
        return Executors.newFixedThreadPool(50);
    }

}
