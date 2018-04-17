package com.fusong.thread;

import com.fusong.POJO.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author:
 * @Description:
 * @Date:Created in  16:37 2018/4/14
 * @ModefiedBy:
 */
public class Master {
    // 1.应该有一个装任务的集合ConcurrentLinkedQueue
    private ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<>();
    // 2.使用普通的HashMap去承装所有的Worker对象
    private Map<String, Thread> taskMap = new HashMap<>();
    // 3.要用一个ConcurrentHashMap去承装每一个Worker执行任务的结果集合，并发执行
    private Map<String, Object> resultMap = new ConcurrentHashMap<>();

    public Master(Worker worker, int workerCount) {
        // 创建worker对Maset中任务队列的引用,用于任务的领取
        worker.setQueue(this.queue);
        // 创建worker对Maset中结果集合的引用，用于任务的提交
        worker.setResultMap(this.resultMap);
        // 循环为多个worker创建线程
        for (int i = 0; i < workerCount; i++) {
            taskMap.put(Integer.toString(i), new Thread(worker, Integer.toString(i)));
        }
    }

    /*检查是否所有的子任务都结束了*/
    public boolean isComplete() {
        for (Map.Entry<String, Thread> entry : taskMap.entrySet()) {
            if (entry.getValue().getState() != Thread.State.TERMINATED) {
                return false;
            }
        }
        return true;
    }

    /*把任务加入队列*/
    public void submit(Object task) {
        queue.add(task);
    }

    /*得到结果集*/
    public Map<String, Object> getResultMap() {
        return resultMap;
    }
    /*执行任务*/
    public void execute() {
        for (Map.Entry<String, Thread> entry : taskMap.entrySet()) {
            entry.getValue().start();
        }
    }
}

class Worker implements Runnable {
    /*任务队列，用于取得任务*/
    private ConcurrentLinkedQueue<Object> queue;
    /*用与存放结果集*/
    private Map<String, Object> resultMap;

    public void setQueue(ConcurrentLinkedQueue<Object> queue) {
        this.queue = queue;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    /*子任务处理的逻辑，包含具体处理步骤*/
    public Object handle(Object task) {
        return task;
    }

    @Override
    public void run() {
        while (true) {
            /*获取子任务*/
            Object task = queue.poll();
            if (task == null)
                break;
            /*处理子任务，并获取结果*/
            Object task1 = handle(task);
            resultMap.put(Integer.toString(task.hashCode()), task1);
        }
    }
}

class PlusWorker extends Worker {
    @Override
    public Object handle(Object task) {
        Integer i =(Integer)task;
        return i;
    }
}

class Main {

    public static void main(String[] args) {
        //固定使用5个Worker，并指定Worker，相当于启动5个线程处理
        Master master=new Master(new PlusWorker(),5);
        //提交100个子任务
        for (int i=0;i<100;i++){
            master.submit(i);
        }
        int re= 0;
        master.execute();
        //保存最终结算结果
        Map<String ,Object> resultMap =master.getResultMap();
        while (resultMap.size()>0||!master.isComplete()){
            Set<String> keys = resultMap.keySet();
            String key =null;
            for(String k:keys){
                key=k;
                break;
            }
            Integer i =null;
            if(key!=null){
                i=(Integer)resultMap.get(key);
            }
            if(i!=null){
                //最终结果
                re+=i;
            }
            if(key!=null){
                //移除已经被计算过的项
                resultMap.remove(key);
            }
        }
        System.out.println("    re "+re);
    }
}