package com.fusong.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class BlockingQueueCharacter {
    public static void main(String[] args) {
        BlockingQueue<String> queue = new ArrayBlockingQueue<String>(5);
        try {
            queue.put("sherry");
            queue.put("conna");
            queue.put("sherlock");
            queue.put("holmou");
            queue.put("kid");
            /*poll和take方法取完元素后删除*/
         /*   System.out.println(" pool 1  "+queue.poll());
            System.out.println(" pool 2  "+queue.poll());
            System.out.println(" size  "+queue.size());*/
         /*使用peek，每次都是取队列第一个元素，且取完后不删除该元素*/
            System.out.println("  peek 1  " + queue.peek());
            System.out.println("  peek 2  " + queue.peek());
            System.out.println("  peek 3  " + queue.peek());
            System.out.println(" zise   " + queue.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
