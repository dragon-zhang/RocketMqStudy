package com.example.demo;

import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangzicheng
 * @date 2021/02/09
 */
public class DelayTimeProducerV2 {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    public static void main(String[] args) throws Exception {
        TransactionMQProducer producer = new TransactionMQProducer("test_group");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        System.out.println("producer started !");

        Message message = new Message("test",
                //tag用于过滤消息
                "ssss",
                (sdf.format(new Date()) + " DelayTimeProducerV2 say hello").getBytes());

        DelayTimeSender sender = new DelayTimeSender(producer);
        sender.addListener(System.out::println);
        sender.sendDelayMessage(message, 3000, TimeUnit.MILLISECONDS);
        sender.shutdown();
    }
}
