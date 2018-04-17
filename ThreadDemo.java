package com.fusong.thread;

/**
 * Created by Administrator on 2018/3/31.
 */
public class ThreadDemo {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                        System.out.println(" Runnable  " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });/* {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                        System.out.println(" Thread  " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };*/
        thread.start();
    }
}
