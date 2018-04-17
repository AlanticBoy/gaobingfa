package com.fusong.demo.demo2;

import com.lmax.disruptor.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/*使用原生API创建一个简单的生产者和消费者*/
public class TradeTransactionMain {
    public static void main(String[] args) throws InterruptedException {
        /*指定RingBuffer的大小*/
        int BUFFER_SIZE = 1024;
        /*
        *createSingleProducer,创建单生产者的RingBuffer
        *第一个参数叫EventFactory，从名字上理解就是“事件工厂”，其实它的职责就是产生数据填充RingBuffer的区块。
         * 第二个参数是RingBuffer的大小，它必须是2的指数倍 目的是为了将求模运算转为&运算提高效率
         * 第三个参数是RingBuffer的生产都在没有可用区块的时候(可能是消费者（或者说是事件处理器） 太慢了)的等待策略
          */
        RingBuffer<TradeTransaction> ringBuffer = RingBuffer.createSingleProducer(new EventFactory<TradeTransaction>() {
            @Override
            public TradeTransaction newInstance() {
                return new TradeTransaction();
            }
        }, BUFFER_SIZE, new YieldingWaitStrategy());
        //创建线程池
        ExecutorService executors = Executors.newFixedThreadPool(5);
        /*创建SequenceBarrier,协调消费者与生产者, 消费者链的先后顺序. 阻塞后面的消费者(没有Event可消费时)*/
        SequenceBarrier barrier = ringBuffer.newBarrier();
        WorkHandler<TradeTransaction> handler = new TradeTransactionHandler();
        /*使用WorkerPool辅助创建消费者*/
        WorkerPool<TradeTransaction> workerPool = new WorkerPool<TradeTransaction>(ringBuffer, barrier, new IgnoreExceptionHandler(), handler);
        workerPool.start(executors);
        // 生产10个数据
        for (int i = 0; i < 10; i++) {
            long seq = ringBuffer.next();
            ringBuffer.get(seq).setPrice(i);
            ringBuffer.publish(seq);
        }
        Thread.sleep(1000); //等上1秒，等消费都处理完成
        workerPool.halt(); //通知事件(或者说消息)处理器 可以结束了（并不是马上结束!!!）
        executors.shutdown();
    }
}
