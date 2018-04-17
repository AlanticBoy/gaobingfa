package com.fusong.thread;

import sun.plugin2.jvm.CircularByteBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author:
 * @Description:
 * @Date:Created in  21:12 2018/4/13
 * @ModefiedBy:
 */
public class CopyOnWriteArrayListDemo {
    static class ReadClass<E> implements Runnable {
        private List<E> list;

        public ReadClass(List<E> list) {
            this.list = list;
        }

        @Override
        public void run() {
            for (E e : list) {
                System.out.println(" read Thread " + e);
            }
        }
    }

    static class WriteClass<E> implements Runnable {
        private List<E> list;
        private E e;

        public WriteClass(List<E> list, E e) {
            this.list = list;
            this.e = e;
        }

        @Override
        public void run() {
            this.list.add(e);
        }
    }

    public static void main(String[] args) {
        /*1.初始化CopyOnWriteArrayList*/
        List<String> list = new ArrayList<>();
        list.add("sherlock");
        list.add("sherry");
        list.add("conan");
        CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>(list);
        /*模拟多线程对copyOnWriteArrayList进行读写操作，如果不报错java.util.ConcurrentModificationException
                那就说明copyOnWriteArrayList是线程安全的*/
        /*那我以后用线程池模拟多线程，尽量少用for(int i=0;i<5;i++){ new Thread...}*/
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        threadPool.execute(new ReadClass<String>(copyOnWriteArrayList));
        threadPool.execute(new ReadClass<String>(copyOnWriteArrayList));
        threadPool.execute(new WriteClass<String>(copyOnWriteArrayList, "白羊座"));
        threadPool.execute(new ReadClass<String>(copyOnWriteArrayList));
        threadPool.execute(new WriteClass<String>(copyOnWriteArrayList, "射手座"));
        threadPool.execute(new ReadClass<String>(copyOnWriteArrayList));
        threadPool.execute(new WriteClass<String>(copyOnWriteArrayList, "水瓶座"));

    threadPool.shutdown();
    }
}
