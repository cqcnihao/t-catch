package com.git.poan.trade.bean;

import java.util.Date;

/**
 * @Author: panbenxing
 * @Date: 2018/12/3
 * @Description:
 */
public class CoinPrice{

    private Date date;
    private Double price;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
