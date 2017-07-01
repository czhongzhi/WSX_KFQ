package com.inetgoes.kfqbrokers.service;

import com.inetgoes.kfqbrokers.utils.L;

import java.util.LinkedList;

/**
 * Created by czz on 2015/12/11.
 * 消息队列
 */
public class MsgQueue {
    public static final int START = 0;  //
    public static final int RUNING = 1; // 运行中
    public static final int FINISH = 2; // 完成

    private final int nThreads; //线程池大小

    private final MsgWorker[] threads ; //用数组实现线程池

    private final LinkedList queue;  //任务队列

    public MsgQueue(int nThreads){
        this.nThreads = nThreads;
        queue = new LinkedList();
        threads = new MsgWorker[nThreads];


         for(int i = 0; i < nThreads;i++){
            threads[i] = new MsgWorker();
            threads[i].start(); //启动所有工作线程
        }
    }

    public void execute(Runnable r){
        synchronized (queue){
            queue.addLast(r);
            queue.notify();
        }
    }




    /**
     * 工作线程类
     */
    private class MsgWorker extends Thread{
        @Override
        public void run() {
            Runnable r;
            while (true){
                synchronized (queue){
                    while (queue.isEmpty()){//如果任务队列中没有任务，等待
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    r = (Runnable) queue.removeFirst();//有任务时，取出任务
                }
                try{
                    r.run();
                }catch (RuntimeException e){
                    // You might want to log something here
                    L.LogE(" MsgWorker-->  You might want to log something here ");
                }
            }
        }
    }

}


