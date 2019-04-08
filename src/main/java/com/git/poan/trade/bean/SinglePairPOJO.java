package com.git.poan.trade.bean;

import lombok.Data;

@Data
public class SinglePairPOJO {

    private Double last;

    private transient String coinName;

    private Double quoteVolume;

    private Double baseVolume;

    private Double percentChange;


}
