package com.fusong.demo.demo1;

import com.lmax.disruptor.RingBuffer;

/**
 * @Author:
 * @Description:定义事件源.
 * @Date:Created in  20:12 2018/4/16
 * @ModefiedBy:
 */
public class ActionEventProducer {
    private final RingBuffer<ActionEvent> ringBuffer;

    public ActionEventProducer(RingBuffer<ActionEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /*
        生产者发布数据给消费者，每调用一次发布一次事件。
        它的参数会通过事件给消费者*/
    public void publishEvent(String value) {
        /*获取下一个可用位置的下标*/
        long seqcence = ringBuffer.next();
        try {
            /*通过seqcence得到一个元素*/
            ActionEvent event = ringBuffer.get(seqcence);
            /*给该元素赋值*/
            event.setName(value);
        } finally {
            /*发布到RingBuffer中，ActionEventHandler可以接收这个值*/
         ringBuffer.publish(seqcence);
        }
    }
}
