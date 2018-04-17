package com.fusong.thread;

import com.fusong.util.RandomUtil;
import com.fusong.util.StringUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author:
 * @Description:
 * @Date:Created in  8:54 2018/4/14
 * @ModefiedBy:
 */
public class BlockingQueueDemo {

    static class Customer implements Runnable {
        private BlockingQueue<String> queue;

        public Customer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                    Thread.sleep(200);
                    String data = queue.take();
                    System.out.println(" customer取出数据 " + data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Producer implements Runnable {
        private BlockingQueue<String> queue;
        private String data;

        public Producer(BlockingQueue<String> queue, String data) {
            this.queue = queue;
            this.data = data;
        }

        @Override
        public void run() {
            try {
                    Thread.sleep(200);
                    queue.put(data);
                    System.out.println(" Producer 盛产数据 "+data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        BlockingQueue<String> queue=new ArrayBlockingQueue<String>(10);
        ExecutorService threadPool= Executors.newFixedThreadPool(5);
        for (int i=0;i<10;i++){
            threadPool.execute(new Producer(queue, RandomUtil.randomString(6)));
            threadPool.execute(new Customer(queue));
        }
        threadPool.shutdown();

    }

}
