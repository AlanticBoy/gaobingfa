package com.fusong.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author:
 * @Description:测试CountDownLatch的用处
 * @Date:Created in  20:54 2018/4/13
 * @ModefiedBy:
 */
public class CountDownLatchDemo {
    /*定义数值为2的CountDownLatch*/
    private static CountDownLatch countDownLatch = new CountDownLatch(2);

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
                System.out.println(" 第一次减减 ");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
                System.out.println(" 第二次减减 ");
            }
        }).start();
        try {/*
            countDownLatch还未减到0时，主线程处于等待状态。直到减到0执行。
            countDownLatch.await(4, TimeUnit.SECONDS);设置等待时间，一旦到时间了
                    主线程开始执行，以防子线程卡死*/
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" 执行主线程内容 ");
    }
}
