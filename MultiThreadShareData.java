package com.fusong.thread;

/**
 * Created by Administrator on 2018/4/1.
 */
public class MultiThreadShareData {
    private static int data = 10;

    public static void main(String[] args) {
        MutilRunableA mutilRunableA = new MutilRunableA(data);
        MutilRunableB mutilRunableB = new MutilRunableB(data);
        /*for循环真的可以模拟多个线程，例如本次模拟出2*2个线程
        * 可以通过Thread.currentThread().getName()判断*/
       /* Thread-0  current data 11
        Thread-3  current data 9
        Thread-2  current data 12
        Thread-1  current data 8*/
        for (int i = 0; i < 2; i++) {
            new Thread(mutilRunableA).start();
            new Thread(mutilRunableB).start();
        }
    }

    static class MutilRunableA implements Runnable {
        private int data;

        public MutilRunableA(int data) {
            this.data = data;
        }

        public synchronized void incrData() {
            data++;
            System.out.println(Thread.currentThread().getName() + "  current data " + data);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                incrData();
            }
        }
    }

    static class MutilRunableB implements Runnable {
        private int data;

        public MutilRunableB(int data) {
            this.data = data;
        }

        public synchronized void decrData() {
            data--;
            System.out.println(Thread.currentThread().getName() + "  current data " + data);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                decrData();
            }
        }
    }
}
