package com.fusong.thread;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2018/4/3.
 */
public class CallableAndFuture {
    /*这是没有CompletionService情况下的提交单个Callable任务*/
    /*public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        *//*注意，threadPool有execute()和submit()方法，其中execute()方法是没有返回值的*//*
        Future<String> future = threadPool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "thank you";
            }
        });
        try {
            System.out.println(" 得到结果 " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        threadPool.shutdown();
    }*/
    /*CompletionService用于提交一组Callable任务*/
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        /*CompletionService里的泛型值要和它管理的Callable方法里的泛型值相同*/
        CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(threadPool);
       /*创建10个任务*/
       for (int i=0;i<10;i++){
           final  int seq=i;
           /*如此循环下来，CompletionService提交了10个任务*/
           completionService.submit(new Callable<Integer>() {
               @Override
               public Integer call() throws Exception {
                   Thread.sleep(new Random().nextInt(6000));
                   int num=new Random().nextInt(5000);
                   return num;
               }
           });
       }

        //获取结果，哪个结果先返回就先获得
        for(int i=0;i<10;i++){
            try {
                /*take方法返回已经完成Callable任务的Future对象*/
              Future<Integer> future=completionService.take();
                System.out.println(" 得到结果  "+future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }


        threadPool.shutdown();
    }
}
