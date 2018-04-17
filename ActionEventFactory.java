package com.fusong.demo.demo1;

import com.lmax.disruptor.EventFactory;

/**
 * @Author:
 * @Description:由于需要让Disruptor为我们创建事件，我们需要声明事件工厂来实例化事件对象。
 * Disruptor通过事件工厂（ActionEventFactory）在RingBuffer中预创建ActionEvent实例，一个事件(ActionEvent)
 * 实际被用作一个数据槽，发布者发布之前，先从RingBuffer获得一个ActionEvent实例，然后向该实例中填充数据。之后发布到RingBuffer中
 *
 * @Date:Created in  19:58 2018/4/16
 * @ModefiedBy:
 */

public class ActionEventFactory implements EventFactory<ActionEvent>{
    @Override
    public ActionEvent newInstance() {
        return new ActionEvent();
    }
}
