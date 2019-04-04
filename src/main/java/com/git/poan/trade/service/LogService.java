package com.git.poan.trade.service;

/**
 * @Author: panbenxing
 * @Date: 2019/4/2
 * @Description: 将所有币种，当前时间戳对应价格记录到redis中，key为币种名称
 */
public interface LogService {

    void log();
}
