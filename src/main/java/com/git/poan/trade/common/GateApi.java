package com.git.poan.trade.common;

public enum GateApi {

    ALL_PAIR("https://data.gateio.io/api2/1/pairs"),

    MARKET_DEEP("https://data.gateio.io/api2/1/orderBook"),

    TICKER("https://data.gateio.io/api2/1/ticker/"),

    SINGLE_PAIR("https://data.gateio.io/api2/1/ticker/%s_%s"),

    //
    ;


    private final String url;

    GateApi(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }
}
