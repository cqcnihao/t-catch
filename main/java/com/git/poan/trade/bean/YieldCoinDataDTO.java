package com.git.poan.trade.bean;

import java.util.List;

public class YieldCoinDataDTO {


    List<CoinPrice> coinPrices;


    class CoinPrice{

        private String coin;
        private Double price;

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }
    }


    public List<CoinPrice> getCoinPrices() {
        return coinPrices;
    }

    public void setCoinPrices(List<CoinPrice> coinPrices) {
        this.coinPrices = coinPrices;
    }
}
