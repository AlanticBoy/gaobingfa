package com.fusong.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/4/2.
 */
public class ThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        /*输出结果：pool-1-thread-2 is dealing task of  7
         pool-1-thread-2 is dealing task of  7
         pool-1-thread-2 is dealing task of  7
         pool-1-thread-2 is dealing task of  8
         pool-1-thread-2 is dealing task of  8
         其中，pool-1-thread-3代表有三个线程，执行10个任务*/
        for (int i = 0; i < 10; i++) {
            final int task = i;
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 5; j++) {
                        System.out.println(Thread.currentThread().getName() + " is dealing task of  " + task);
                    }
                }
            });
        }
        /*上述代码执行完毕后，程序没有结束。因为池子中有三个线程存在，所以不会结束*/
        threadPool.shutdown();/*当线程池中的线程执行完所有任务，所有线程处于空闲状态时，干掉所有线程，程序自动结束*/
        //threadPool.shutdownNow();  //立即把池子中所有线程干掉，无论任务是否干完
    }
}
