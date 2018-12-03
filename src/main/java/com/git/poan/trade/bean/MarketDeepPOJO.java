package com.git.poan.trade.bean;

import lombok.Data;

import java.util.List;

@Data
public class MarketDeepPOJO {


    List<List> asks;

    List<List> bids;


    public List<List> getAsks() {
        return asks;
    }

    public void setAsks(List<List> asks) {
        this.asks = asks;
    }

    public List<List> getBids() {
        return bids;
    }

    public void setBids(List<List> bids) {
        this.bids = bids;
    }
}
