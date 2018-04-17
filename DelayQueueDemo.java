package com.fusong.thread;

import com.fusong.util.StringUtil;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @Author:
 * @Description:模拟去网吧上网，当上机达到一定时间后，会自动下机 测试DelayedQueue的使用:DelayQueue是带有延迟时间的Queue，之中的元素只有当其指定的延迟时间到了
 * 才能在队列中取出该元素.DelayQueue中的元素必须实现Delayed接口，DelayQueue是没有大小限制的队列
 * @Date:Created in  9:38 2018/4/14
 * @ModefiedBy:
 */
public class DelayQueueDemo {
    /**
    　　* @Description: $定义一个POJO类，让其实现Delayed借口，重写getDelay和compareTo方法
         getDelay作用是给定的时间-当前系统的时间。
    　　* @param ${tags}
    　　* @return ${return_type}
    　　* @throws
    　　* @author
    　　* @date 2018/4/14 10:10
    　　*/
    static class WangMin implements Delayed {

        private String id;
        private String name;
        private long endtime;
        private TimeUnit unit = TimeUnit.SECONDS;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public WangMin(String id, String name, long endtime) {
            this.id = id;
            this.name = name;
            this.endtime = endtime;

        }

        @Override
        public long getDelay(TimeUnit unit) {
            return endtime - System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed o) {
            WangMin wangMin = (WangMin) o;
            return this.getDelay(unit) - wangMin.getDelay(unit) > 0 ? 1 : 0;
        }
    }

    static class Wangba implements Runnable {

        private DelayQueue<WangMin> queue = new DelayQueue<>();
        private boolean isExist = true;

        public Wangba(DelayQueue<WangMin> queue) {
            this.queue = queue;
        }

        public void desc(WangMin wangMin) {
            System.out.println("识别号 " + wangMin.getId() + "   用户  " + wangMin.getName() + " 将要下机");
        }

        @Override
        public void run() {
            while (isExist) {
                try {
                    if (queue.size() <= 0) {
                        System.out.println(" 队列中没有元素了 ");
                        isExist = false;
                    }
                    WangMin wangMin = queue.take();
                    desc(wangMin);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void main(String[] args) {
        DelayQueue<WangMin> queue = new DelayQueue<>();
        queue.add(new WangMin("123", "sherry", 2000 + System.currentTimeMillis()));
        queue.add(new WangMin("456", "conan", 5000 + System.currentTimeMillis()));
        queue.add(new WangMin("789", "holmous", 8000 + System.currentTimeMillis()));

        Wangba wangba = new Wangba(queue);
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(wangba);
        service.shutdown();
       /* System.out.println(" kai shi ji shi ");
        while (true){
            System.out.println(" current seconds  "+new Date().getSeconds());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
}
