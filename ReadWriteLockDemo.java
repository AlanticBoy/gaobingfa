package com.fusong.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Administrator on 2018/4/4.
 */
public class ReadWriteLockDemo {
    /*缓存的Map*/
    private Map<String, Object> map = new HashMap<>();
    /*读写锁对象*/
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    /*从缓存获取数据*/
    public Object getData(String key) {
        lock.readLock().lock();/*加上读锁，对写的线程互斥*/
        Object value = null;

        try {
            value = map.get(key);/*尝试从缓存获取数据*/
            if (value == null) {
                /*如果发现目标值为null，释放掉读锁，同时上写锁*/
                lock.readLock().unlock();
                lock.writeLock().lock();
                try {
                    /*这步经过深入考虑，再次获取目标值*/
                    value = map.get(key);
                    if (value == null) {
                       /*如果目标值仍然为空，从数据库读取数据*/
                        value = new Random().nextInt(10000) + "test";
                        map.put(key, value);
                        System.out.println("db completed!");
                    }
                    lock.readLock().lock();//再次对读进行锁住，以防止写的操作，造成数据错乱
                } finally {
                  /*
                         * 先加读锁再释放写锁读作用：
                         * 防止在hong se hang 出多个线程获得写锁进行写的操作，所以在写锁还没有释放前要上读锁
                         */
                    lock.writeLock().unlock();
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return value;
    }
}
