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
public class MainWithoutTranslator {
    public static void main(String[] args) {
        ExecutorService threadPool=Executors.newCachedThreadPool();
        /*事件工厂，作为Disruptor的参数*/
        EventFactory<ActionEvent> eventFactory=new ActionEventFactory();
        /*定义ringBuffer的大小，必须是2的N次方*/
        int ringBufferSize=1024*1024;
        /*定义Disruptor*/
        Disruptor<ActionEvent> disruptor=new Disruptor<ActionEvent>(eventFactory,ringBufferSize,threadPool);
        /*定义事件消费者*/
        EventHandler<ActionEvent> eventHandler=new ActionEventHandler();
        disruptor.handleEventsWith(eventHandler);
        /*启动Disruptor，启动所有线程*/
         disruptor.start();
         /*从disruptor中获得RingBuffer*/
        RingBuffer<ActionEvent> ringBuffer=disruptor.getRingBuffer();
        ActionEventProducer producer=new ActionEventProducer(ringBuffer);

        for (int k=0;k<20;k++){
            producer.publishEvent(RandomUtil.randomString(8));
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /*关闭disruptor和threadPool*/
        disruptor.shutdown();
        threadPool.shutdown();
    }
}
