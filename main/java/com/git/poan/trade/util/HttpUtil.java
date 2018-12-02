package com.git.poan.trade.util;

import com.alibaba.fastjson.JSON;
import com.git.poan.trade.bean.MarketDeepPOJO;
import com.git.poan.trade.bean.SinglePairPOJO;
import com.git.poan.trade.bean.Ticker;
import com.git.poan.trade.common.GateApi;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HttpUtil {

    private HttpUtil() {
    }

    private static final List<String> exclude = Arrays.asList("BTC_USDT", "ETH_USDT",
            "LTC_USDT");


    public static OkHttpClient getSingleClient() {
        return Client.client;

    }

    private static class Client {
        private static OkHttpClient client = new OkHttpClient();

    }


    // 工具方法：
    public static MarketDeepPOJO getAllMarketDeep(OkHttpClient client, String pair) {
        Request request = new Request.Builder()
                .url(GateApi.MARKET_DEEP.getUrl() + "/" + pair)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String res = response.body().string();
            return JSON.parseObject(res, MarketDeepPOJO.class);
        } catch (IOException e) {
            return null;
        }

    }

    public static SinglePairPOJO getSinglePair(OkHttpClient client,String pair){

        pair = String.format(GateApi.SINGLE_PAIR.getUrl(), pair, "usdt");
        System.out.println(pair);
        Request request = new Request.Builder()
                .url(pair)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String res = response.body().string();
            return JSON.parseObject(res, SinglePairPOJO.class);
        } catch (IOException e) {
            return null;
        }
    }

    public static Ticker getTicker(String pair) {

        Request request = new Request.Builder()
                .url(GateApi.TICKER.getUrl() + pair)
                .build();
        Call call = getSingleClient().newCall(request);
        try {
            Response response = call.execute();
            String res = response.body().string();
            return JSON.parseObject(res, Ticker.class);
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        SinglePairPOJO ocn = getSinglePair(HttpUtil.getSingleClient(), "ocn");
        Double last = ocn.getLast();
        System.out.println(last);
    }

}
