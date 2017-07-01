package com.inetgoes.kfqbrokers.service;

import com.inetgoes.kfqbrokers.utils.L;

/**
 * Created by czz on 2015/12/11.
 */
public class TestTask implements Runnable {
    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        L.LogI(name + " executed OK");
    }
}
