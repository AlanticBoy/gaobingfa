package com.fusong.demo.demo1;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

/**
 * @Author:
 * @Description:在Disruptor3.0以后的版本最好使用Event Publisher或者Event Translator来发布事件。
 * @Date:Created in  20:38 2018/4/16
 * @ModefiedBy:
 */
public class ActionEventProducerWithTranslator {
    private RingBuffer<ActionEvent> ringBuffer;

    public ActionEventProducerWithTranslator(RingBuffer<ActionEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }
     /*虽然目前我不知道这样写有什么好处，不过它终归是有好处的*/
    private static final EventTranslatorOneArg<ActionEvent, String> TRANSLATOR = new EventTranslatorOneArg<ActionEvent, String>() {
        @Override
        public void translateTo(ActionEvent event, long sequence, String arg0) {
         event.setName(arg0);
        }
    };

    public void publishEvent(String val){
        ringBuffer.publishEvent(TRANSLATOR,val);
    }

}
