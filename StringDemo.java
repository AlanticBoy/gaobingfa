package com.fusong.demo.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newCachedThreadPool;

/**
 * @Author:
 * @Description:
 * @Date:Created in  18:09 2018/4/16
 * @ModefiedBy:
 */
public class StringDemo {
    /*队列中的元素*/
    static class Element {
        private int value;

        public int get() {
            return value;
        }

        public void set(int value) {
            this.value = value;
        }
    }

    public static void main(String[] args) throws InterruptedException {
      /*生产者的线程工厂*/
        ExecutorService threadPool = Executors.newCachedThreadPool();
       /*RingBuffer的生产工厂，初始化ringbuffer时使用*/
        EventFactory<Element> eventFactory = new EventFactory<Element>() {
            @Override
            public Element newInstance() {
                return new Element();
            }
        };
        /*处理Event的Handler*/
        EventHandler<Element> eventHandler = new EventHandler<Element>() {
            @Override
            public void onEvent(Element event, long sequence, boolean endOfBatch) throws Exception {
                System.out.println("Element: " + event.get());
            }
        };
        /*指定等待策略*/
        /*Disruptor默认的等待策略是BlockingWaitStrategy，这个策略的内部使用一个锁和条件变量来控制线程的执行和等待（Java基本的同步方法），
        BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现；
        *YieldingWaitStrategy 的性能是最好的，适合用于低延迟的系统。这种策略在减低系统延迟的同时也会增加CPU运算量。
        * YieldingWaitStrategy策略会循环等待sequence增加到合适的值。循环中调用Thread.yield()允许其他准备好的线程执行。
        * 在要求极高性能且事件处理线数小于 CPU 逻辑核心数的场景中，推荐使用YieldingWaitStrategy策略。例如，CPU开启超线程的时候。
        */
        /*阻塞策略*/
        BlockingWaitStrategy strategy = new BlockingWaitStrategy();
        /*指定RingBuffer的大小*/
        int size = 16;
         /*创建Disruptor，采用单生产模式*/
        Disruptor<Element> disruptor = new Disruptor<Element>(eventFactory, size, threadPool, ProducerType.SINGLE, strategy);
        /*设置EventHandler,用于接受disruptor.getRingBuffer().publish(sequence)这个位置的元素的值*/
        disruptor.handleEventsWith(eventHandler);
        /*启动Disruptor线程*/
        disruptor.start();

        RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();
        for (int k = 0; k < 20; k++) {
           /*获取下一个可用位置的下标，sequence有顺序的意思*/
            long sequence = ringBuffer.next();
            /*注意，这里一定要加try/finally，使用try/finnally保证事件一定会被发布
            * 如果我们使用RingBuffer.next()获取一个事件槽，那么一定要发布对应的事件。
            * 如果不能发布事件，那么就会引起Disruptor状态的混乱。
            * 尤其是在多个事件生产者的情况下会导致事件消费者失速，从而不得不重启应用才能会恢复。*/
            try {
            /*得到一个元素，该元素可以使用这个位置*/
                Element element = ringBuffer.get(sequence);
              /*给该元素赋值*/
                element.set(k);
            } finally {
               /*这样EventHandler就可以读取元素的值了*/
                ringBuffer.publish(sequence);
            }
            TimeUnit.MILLISECONDS.sleep(200);

        }
        /*关闭线程池*/
        disruptor.shutdown();
        threadPool.shutdown();

    }

}
