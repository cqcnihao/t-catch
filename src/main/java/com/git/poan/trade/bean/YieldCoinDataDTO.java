package com.git.poan.trade.bean;

import java.util.Date;
import java.util.List;

public class YieldCoinDataDTO {


    List<CoinPrice> coinPrices;

    public List<CoinPrice> getCoinPrices() {
        return coinPrices;
    }

    public void setCoinPrices(List<CoinPrice> coinPrices) {
        this.coinPrices = coinPrices;
    }
}
