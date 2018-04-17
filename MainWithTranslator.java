package com.fusong.demo.demo1;

import com.fusong.util.RandomUtil;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author:
 * @Description:
 * @Date:Created in  20:27 2018/4/16
 * @ModefiedBy:
 */
public class MainWithTranslator {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        ActionEventFactory eventFactory = new ActionEventFactory();
        int ringbuffersize = 1024 * 1024;
        Disruptor<ActionEvent> disruptor = new Disruptor<ActionEvent>(eventFactory, ringbuffersize, threadPool);
        ActionEventHandler handler = new ActionEventHandler();
        disruptor.handleEventsWith(handler);
        disruptor.start();
        RingBuffer<ActionEvent> ringBuffer = disruptor.getRingBuffer();
        ActionEventProducerWithTranslator translator = new ActionEventProducerWithTranslator(ringBuffer);
        for (int i = 0; i < 20; i++) {
            translator.publishEvent(RandomUtil.randomString(8));
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        disruptor.shutdown();
        threadPool.shutdown();
    }
}
