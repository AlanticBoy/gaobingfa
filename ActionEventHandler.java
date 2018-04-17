package com.fusong.demo.demo1;


import com.lmax.disruptor.EventHandler;

/**
 * @Author:
 * @Description:定义事件处理器，即事件消费者。用于取出发布在RingBuffer数据槽里的数据。
 * 实现接口，EventHandler<T>
 *
 * @Date:Created in  20:06 2018/4/16
 * @ModefiedBy:
 */
public class ActionEventHandler implements EventHandler<ActionEvent> {
    @Override
    public void onEvent(ActionEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(" 接到数据  "+event.getName());
    }
}
