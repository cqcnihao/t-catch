package com.git.poan.trade.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: panbenxing
 * @Date: 2018/12/3
 * @Description:
 */
public class ThreadPool {
    
    private static ExecutorService executorService;
    //todo 使用spring管理
    public static ExecutorService getPool(){
        executorService = Executors.newSingleThreadExecutor();
        return executorService;
    }
    
}
