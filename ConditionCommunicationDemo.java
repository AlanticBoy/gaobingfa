package com.fusong.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2018/4/4.
 */
public class ConditionCommunicationDemo {
    private static Bussiness bussiness = new Bussiness();

    public static void main(String[] args) {

        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200);
                        bussiness.main();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bussiness.second();
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bussiness.third();
                }
            }).start();
        }
    }

    static class Bussiness {
        private Lock lock = new ReentrantLock();
        private Condition notifyofMain = lock.newCondition();//main通知second
        private Condition notifyofSecond = lock.newCondition();//second通知third
        private Condition notifyofThird = lock.newCondition();//third通知first
        private int status = 1;//当前正在执行任务标志

        /*下面只介绍两个方法工作原理*/
        public void main() {
            /*上锁，防止其它线程进来*/
            lock.lock();
            while (status != 1) {
                try {
                    /*如果当前status不是1，那么老大就得等*/
                    notifyofMain.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                for (int i = 0; i < 5; i++) {
                    System.out.println(" main is dealing loop of  " + i);
                }
                /*老大执行任务后，老二执行，status置为2*/
                status = 2;
                /*通知老二执行*/
                notifyofSecond.signal();
            } finally {
                /*释放锁*/
                lock.unlock();
            }
        }

        public void second() {
            /*上锁，防止其它线程进来*/
            lock.lock();
            while (status != 2) {
                try {
                    /*当前status不是2的话，老二就等*/
                    notifyofSecond.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                for (int i = 0; i < 3; i++) {
                    System.out.println(" second is dealing loop of  " + i);
                }
                /*老二执行任务完，老三执行，status置为3*/
                status = 3;
                /*通知老三执行*/
                notifyofThird.signal();
            } finally {
                /*释放锁*/
                lock.unlock();
            }
        }

        public void third() {
            lock.lock();
            while (status != 3) {
                try {
                    notifyofThird.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                for (int i = 0; i < 2; i++) {
                    System.out.println(" third is dealing loop of  " + i);
                }
                status = 1;
                notifyofMain.signal();
            } finally {
                lock.unlock();
            }
        }
    }
}
