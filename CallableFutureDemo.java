package com.fusong.thread;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @Author:
 * @Description:
 * @Date:Created in  15:07 2018/4/14
 * @ModefiedBy:
 */
public class CallableFutureDemo {
    static class Task implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            TimeUnit.SECONDS.sleep(new Random().nextInt(6));
            return new Random().nextInt(5000);
        }
    }

    public static void main(String[] args) throws ExecutionException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        /*
        用CompletionService的好处：CompletionService实现了生产者提交任务和消费者获取结果的解耦，生产者和消费者
                都不用关心任务的完成顺序，它保证消费者一定是按照任务的完成先后顺序来获取结果*/
        CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(threadPool);
        for (int i = 0; i < 10; i++) {
            completionService.submit(new Task());
        }

        for (int i = 0; i < 10; i++) {
            try {
                Future<Integer> future = completionService.take();
                /*future.get()方法用来获取结果*/
                System.out.println(" 得到结果   " + future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threadPool.shutdown();
    }
}
