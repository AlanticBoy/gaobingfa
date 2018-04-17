package com.fusong.thread;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2018/4/3.
 */
public class LockDemo {
    private static Outputer outputer = new Outputer();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        CompletionService<String> completionService = new ExecutorCompletionService<String>(threadPool);
        for (int i = 0; i < 10; i++) {
            completionService.submit(new Callable<String>() {
                                         @Override
                                         public String call() throws Exception {
                                             Thread.sleep(2000);
                                             return outputer.output("she rr y ");
                                             // return "Sherlock holmous";
                                         }
                                     }
            );
            completionService.submit(new Callable<String>() {
                                         @Override
                                         public String call() throws Exception {
                                             Thread.sleep(2000);
                                             return outputer.output("liu de hua");
                                             // return "Sherlock holmous";
                                         }
                                     }
            );
        }

        for (int i = 0; i < 20; i++) {
            try {
                Future<String> future = completionService.take();
                System.out.println(" 得到 结果  " + future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }


        threadPool.shutdown();
    }

    static class Outputer {
        /*这里用Lock能起到与synchronized一样的效果
        * 但是看起来比synchronized6更好理解
        * */
        private Lock lock = new ReentrantLock();
        StringBuffer buffer = new StringBuffer();
        String seq = "";

        public String output(String content) {
            lock.lock();
            try {
                int len = content.length();
                for (int i = 0; i < len; i++) {
                    // System.out.print(content.charAt(i));
                    buffer.append(content.charAt(i));
                }
            } finally {
                //try起来的原因是万一一个线程进去了然后挂了或者抛异常了，那么这个锁根本没有释放
                seq = new String(buffer);
                buffer.setLength(0);
                lock.unlock();

            }
            return seq;
        }
    }
}



