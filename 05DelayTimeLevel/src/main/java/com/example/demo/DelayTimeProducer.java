package com.example.demo;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

/**
 * @author zhangzicheng
 * @date 2021/02/08
 */
public class DelayTimeProducer {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static void main(String[] args) throws Exception {
        TransactionMQProducer producer = new TransactionMQProducer("test_group");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        System.out.println("producer started !");

        Message message = new Message("test",
                //tag用于过滤消息
                "ssss",
                "DelayTimeProducer say hello".getBytes());
        message.setDelayTimeLevel(1);

        SendResult result = producer.send(message);
        System.out.println(sdf.format(new Date()) + " " + result);
        producer.shutdown();
    }
}
