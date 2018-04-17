package com.fusong.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2018/3/31.
 */
/*
public class ThreadSharedData {
    public static int data = 0;

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    data = new Random().nextInt();
                    System.out.println(Thread.currentThread().getName()
                            + " has put data :" + data);
                    new ThreadA().getData();
                    new ThreadB().getData();
                }
            }).start();
        }
    }

    static class ThreadA {
        public void getData() {
            System.out.println("A from " + Thread.currentThread().getName()
                    + " get data : " + data);
        }
    }

    static class ThreadB {
        public void getData() {
            System.out.println("B from " + Thread.currentThread().getName()
                    + " get data : " + data);
        }
    }
}
*/

/*
public class ThreadSharedData {
    */
/*定义一个Map用于存储Thread对象和Thread对象对应的值*//*

    private static Map<Thread, Integer> map = new HashMap<>();
    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int data=new Random().nextInt();
                    map.put(Thread.currentThread(),data);
                    new ThreadA().getData();
                    new ThreadB().getData();
                }
            }).start();
        }
    }
    static class ThreadA {
        public void getData() {
            int data = map.get(Thread.currentThread());
            System.out.println("A from " + Thread.currentThread().getName()
                    + " get data :" + data);
        }
    }
    static class ThreadB {
        public void getData() {
            int data = map.get(Thread.currentThread());
            System.out.println("B from " + Thread.currentThread().getName()
                    + " get data :" + data);
        }
    }
}
*/



public class ThreadSharedData {
    /*定义ThreadLocal，ThreadLocal可以看作一个集合。
    * ThreadLocal的缺点是，每次只能存一类值，如果存多类的话，需要定义多个ThreadLocal
    * */
    private static ThreadLocal<Integer> local = new ThreadLocal<>();

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int data = new Random().nextInt();
                    /*ThreadLocal存的时候与当前线程有关*/
                    local.set(data);
                    new ThreadA().getData();
                    new ThreadB().getData();
                }
            }).start();
        }
    }
    static class ThreadA {
        public void getData() {
            /*ThreadLocal取值的时候也与当前线程有关*/
            int data = local.get();
            System.out.println("A from " + Thread.currentThread().getName()
                    + " get data :" + data);
        }
    }

    static class ThreadB {
        public void getData() {
            int data = local.get();
            System.out.println("B from " + Thread.currentThread().getName()
                    + " get data :" + data);
        }
    }
}
