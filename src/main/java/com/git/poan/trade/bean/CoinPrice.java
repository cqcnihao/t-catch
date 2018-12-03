package com.git.poan.trade.bean;

import java.util.Date;

/**
 * @Author: panbenxing
 * @Date: 2018/12/3
 * @Description:
 */
public class CoinPrice{

    private String date;
    private Double price;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
