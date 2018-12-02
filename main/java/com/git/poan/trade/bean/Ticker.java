package com.git.poan.trade.bean;

import lombok.Data;

@Data
public class Ticker {

    private Double quoteVolume;

    private Double baseVolume;

    private Double last;


    public Double getQuoteVolume() {
        return quoteVolume;
    }

    public void setQuoteVolume(Double quoteVolume) {
        this.quoteVolume = quoteVolume;
    }

    public Double getBaseVolume() {
        return baseVolume;
    }

    public void setBaseVolume(Double baseVolume) {
        this.baseVolume = baseVolume;
    }

    public Double getLast() {
        return last;
    }

    public void setLast(Double last) {
        this.last = last;
    }
}
