package com.git.poan.trade.service.impl;

import com.git.poan.trade.service.StrategyService;
import org.springframework.stereotype.Service;

/**
 * @Author: panbenxing
 * @Date: 2019/4/2
 * @Description: 针对redis中的值进行不同时段的统计
 *
 */
@Service
public class StrategyServiceImpl implements StrategyService {

    private void sell_1() {
        // todo 判断当前购买的币 ，在每（1，2，3，4，5）分钟判断一次购买时的涨幅，如果有3个点的涨幅，优先卖出

        //
    }

}
