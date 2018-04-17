package com.fusong.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2018/4/4.
 */
public class BoundedQueues<T> {
    public List<T> q; //这个列表用来存队列的元素
    private int maxSize; //队列的最大长度
    private Lock lock = new ReentrantLock();

    private Condition addConditoin = lock.newCondition();
    private Condition removeConditoin = lock.newCondition();
    public BoundedQueues(int size) {
        q = new ArrayList<>(size);
        maxSize = size;
    }
    public void add(T e) {
        lock.lock();
        try {
            while (q.size() == maxSize) {
                addConditoin.await();
            }
            q.add(e);
            removeConditoin.signal(); //执行了添加操作后唤醒因队列空被阻塞的删除操作
        } catch (InterruptedException e1) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
    public T remove() {
        lock.lock();
        try {
            while (q.size() == 0) {
                removeConditoin.await();
            }
            T e = q.remove(0);
            addConditoin.signal(); //执行删除操作后唤醒因队列满而被阻塞的添加操作
            return e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlock();
        }
    }
}
