package com.git.poan.trade.service.impl;

import com.git.poan.trade.bean.SinglePairPOJO;
import com.git.poan.trade.service.LogService;
import com.git.poan.trade.util.HttpUtil;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: panbenxing
 * @Date: 2019/4/2
 * @Description:
 */
@Service
public class LogServiceImpl implements LogService {


    @Autowired
    private List<String> allPair;


    public void log() {
        OkHttpClient singleClient = HttpUtil.getSingleClient();
        for (String pair : allPair) {
            SinglePairPOJO singlePair = HttpUtil.getSinglePair(singleClient, pair);
            System.out.println(pair+":"+singlePair.getLast());
        }

        //https://data.gateio.co/api2/1/ticker/usdt_cny
        //https://data.gateio.co/api2/1/ticker/zsc_eth
    }


}
