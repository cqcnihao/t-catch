package com.git.poan.trade.mapper;

import com.git.poan.trade.entity.YieldCoin;
import java.util.List;

public interface YieldCoinMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YieldCoin record);

    YieldCoin selectByPrimaryKey(Integer id);

    List<YieldCoin> selectAll();

    int updateByPrimaryKey(YieldCoin record);
}