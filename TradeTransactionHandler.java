package com.fusong.demo.demo2;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import java.util.UUID;



public class TradeTransactionHandler implements EventHandler<TradeTransaction>,WorkHandler<TradeTransaction> {
    @Override
    public void onEvent(TradeTransaction event, long sequence, boolean endOfBatch) throws Exception {
        /*调用下面的event*/
        this.onEvent(event);
    }

    /*这个方法做具体的消费逻辑*/
    @Override
    public void onEvent(TradeTransaction event) throws Exception {
    event.setId(UUID.randomUUID().toString());
        System.out.println(event.getId());
    }
}
