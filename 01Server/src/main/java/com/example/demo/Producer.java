package com.example.demo;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * @author zhangzicheng
 * @date 2021/02/07
 */
public class Producer {
    public static void main(String[] args) throws Exception {
        TransactionMQProducer producer = new TransactionMQProducer("test_group");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        System.out.println("producer started !");

        Message message = new Message("test",
                //tag用于过滤消息
                "ssss",
                "hello".getBytes());

        SendResult result = producer.send(message);
        System.out.println(result);
    }
}
